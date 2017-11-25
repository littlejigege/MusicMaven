package com.qg.musicmaven.mainpage

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.app.Fragment
import android.content.AsyncQueryHandler
import android.graphics.Camera
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.os.Handler
import android.os.HandlerThread
import android.view.Menu
import android.view.SurfaceHolder
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import com.iflytek.cloud.IdentityVerifier
import com.mobile.utils.permission.Permission
import com.qg.musicmaven.R
import com.qg.musicmaven.BaseActivity
import com.qg.musicmaven.cloudpage.CloudFragment
import com.qg.musicmaven.dreampage.DreamFragment
import com.qg.musicmaven.kugoupage.KugouFragment
import com.qg.musicmaven.settingpage.SettingFragment
import kotlinx.android.synthetic.main.activity_test_main.*
import org.jetbrains.anko.cameraManager
import android.hardware.camera2.CaptureRequest
import android.media.Image
import android.media.ImageReader
import com.mobile.utils.doAfter
import kotlinx.android.synthetic.main.audio_item.*
import kotlinx.coroutines.experimental.delay
import kotlin.concurrent.thread


class TestMainActivity : BaseActivity(), MainPageContract.View, SurfaceHolder.Callback {
    //========================test========================
    private lateinit var handler: Handler
    private lateinit var camera: CameraDevice
    private val CID = "${CameraCharacteristics.LENS_FACING_BACK}"
    private lateinit var previewRequestBuilder: CaptureRequest.Builder
    private val imageReader = ImageReader.newInstance(100, 100, ImageFormat.JPEG, 1)
    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        println("surfaceChanged")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        println("surfaceDestroyed")
    }

    @SuppressLint("MissingPermission")
    override fun surfaceCreated(holder: SurfaceHolder?) {
        println("surfaceCreated")
        Permission.CAMERA.doAfterGet(this) {
            val handlerThread = HandlerThread("")
            handlerThread.start()
            handler = Handler(handlerThread.looper)
            cameraManager.openCamera(CID, stateCallback, handler)
        }
    }

    //========================test========================
    //默认为酷狗碎片
    companion object {
        var fragMentShowingTag = KugouFragment.TAG
    }

    private val presenter by lazy { MainPagePresenter() }
    //四个碎片
    private val kugouFragment by lazy { KugouFragment() }
    private val cloudFragment by lazy { CloudFragment() }
    private val dreamFragment by lazy { DreamFragment() }
    private val settingFragment by lazy { SettingFragment() }
    //========================test========================
    private val stateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            println("onOpened")
            this@TestMainActivity.camera = camera
            previewRequestBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            previewRequestBuilder.addTarget(imageReader.surface)
            camera.createCaptureSession(arrayListOf(imageReader.surface), sessionStateCallback, handler)
        }

        override fun onDisconnected(camera: CameraDevice?) {

        }

        override fun onError(camera: CameraDevice?, error: Int) {

        }
    }

    private val sessionStateCallback = object : CameraCaptureSession.StateCallback() {
        override fun onConfigureFailed(session: CameraCaptureSession?) {

        }

        override fun onConfigured(session: CameraCaptureSession) {
            println("onConfigured")
            val previewRequest = previewRequestBuilder.build()
            session.setRepeatingRequest(previewRequest, null, handler)
            thread {
                while (true) {
                    var image: Image? = null
                    if ({ image = imageReader.acquireLatestImage();image }() != null) {

                        image?.close()
                        println("image")
                    }
                }
            }
        }
    }
    //========================test========================
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_kugou -> {
                presenter.onKugouClick()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_cloud -> {
                presenter.onCloudClick()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dream -> {
                presenter.onDreamClick()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                presenter.onSettingClick()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun animateToolbar() {
        val t = toolbarMain.getChildAt(0)
        if (t != null && t is TextView) {
            val title = t
            title.alpha = 0f
            title.scaleX = 0.8f

            title.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .setStartDelay(300)
                    .setDuration(900).interpolator = AccelerateDecelerateInterpolator()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_main)
        presenter.takeView(this)
        setActionBar(toolbarMain)
        navigationBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //toolbar标题做动画
        animateToolbar()
        //加载第一个显示的碎片
        presenter.onKugouClick()
        surfaceView.holder.addCallback(this)
        surfaceView.keepScreenOn = true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.test_main, menu)
        searchViewMain.setMenuItem(menu!!.findItem(R.id.test_main_search))
        //searchViewMain.setOnQueryTextListener(this)
        return true
    }


    private fun replaceFragment(fragment: Fragment) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(fragmentContainer.id, fragment)
        transaction.commit()
    }

    override fun onDestroy() {
        presenter.dropView()
        super.onDestroy()
    }

    override fun openKugouPage() {
        replaceFragment(kugouFragment)
    }

    override fun openCloudPage() {
        replaceFragment(cloudFragment)
    }

    override fun openDreamPage() {
        replaceFragment(dreamFragment)
    }

    override fun openSettingPage() {
        replaceFragment(settingFragment)
    }
}
