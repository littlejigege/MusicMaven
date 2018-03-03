package com.qg.musicmaven.utils

import android.database.Cursor
import android.database.MatrixCursor
import android.support.annotation.LayoutRes
import android.support.v4.widget.CursorAdapter
import android.support.v7.widget.SearchView

/**
 * Created by jimiji on 2017/12/17.
 */

fun SearchView.setData(data: Array<Array<String>>, @LayoutRes res: Int, ids: IntArray, matcher: String.(queryStr: String) -> Boolean, matchPos: Int = 0, clickShowPos: Int = 0, onSubmit: (String) -> Boolean, onTextChange: (String) -> Boolean) {
    val COLUMNS = mutableListOf<String>("_id")
    ids.forEachIndexed { index, _ -> COLUMNS.add(index.toString()) }
    val cursor = MatrixCursor(COLUMNS.toTypedArray())
    val FROM = COLUMNS.subList(1, COLUMNS.size).toTypedArray()

    val datas = data.filter { strings: Array<String> ->
        strings[matchPos].matcher("")
    }

    datas.forEachIndexed { i, row ->
        val out = row.toMutableList()
        out.add(0, i.toString())
        cursor.addRow(out)
    }

    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            onSubmit(query)
            return false
        }

        override fun onQueryTextChange(newText: String): Boolean {
            onTextChange(newText)
            val newCursor = MatrixCursor(COLUMNS.toTypedArray())

            val newDatas = data.filter { strings: Array<String> ->
                strings[matchPos].matcher(newText ?: "")
            }

            newDatas.forEachIndexed { i, row ->
                val out = row.toMutableList()
                out.add(0, i.toString())
                newCursor.addRow(out)
            }
            suggestionsAdapter.changeCursor(newCursor)
            return true
        }
    })

    suggestionsAdapter = android.support.v4.widget.SimpleCursorAdapter(
            context,
            res, cursor,
            FROM,
            ids,
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)

    setOnSuggestionListener(object : SearchView.OnSuggestionListener {
        override fun onSuggestionSelect(position: Int): Boolean = false

        override fun onSuggestionClick(position: Int): Boolean {
            setQuery((suggestionsAdapter.getItem(position) as Cursor).getString(clickShowPos + 1), false)
            return true
        }
    })

}

fun SearchView.setData(data: Array<String>, onSubmit: (String) -> Boolean, onTextChange: (String) -> Boolean, matcher: (String.(queryStr: String) -> Boolean) = { contains(it) }) {
    val datas = mutableListOf<Array<String>>()
    data.forEach { e -> datas.add(arrayOf(e)) }
    setData(datas.toTypedArray(), android.R.layout.simple_list_item_1, intArrayOf(android.R.id.text1), matcher, onSubmit = onSubmit, onTextChange = onTextChange)
}

