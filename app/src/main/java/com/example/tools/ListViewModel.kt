package com.example.tools

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.core.content.edit

class ListViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("multi_tool_lists", Context.MODE_PRIVATE)
    private val delimiter = "|||"

    val leftList = mutableStateListOf<String>()
    val rightList = mutableStateListOf<String>()

    var inputText by mutableStateOf("")
        private set

    var selection by mutableStateOf<Pair<ListSide, Int>?>(null)
        private set

    init {
        loadLists()
    }

    private fun saveLists() {
        prefs.edit {
            putString("left_list", leftList.joinToString(delimiter))
                .putString("right_list", rightList.joinToString(delimiter))
        }
    }

    private fun loadLists() {
        val leftSaved = prefs.getString("left_list", "") ?: ""
        val rightSaved = prefs.getString("right_list", "") ?: ""

        if (leftSaved.isNotEmpty()) {
            leftList.addAll(leftSaved.split(delimiter))
        }
        if (rightSaved.isNotEmpty()) {
            rightList.addAll(rightSaved.split(delimiter))
        }
    }

    fun onInputTextChanged(newText: String) {
        if (newText.length <= 20) {
            inputText = newText
        }
    }

    fun addItem() {
        if (inputText.isNotBlank()) {
            leftList.add(inputText.trim())
            inputText = ""
            selection = null
            saveLists()
        }
    }

    fun deleteSelectedItem() {
        selection?.let { (listSide, index) ->
            if (listSide == ListSide.LEFT && index < leftList.size) leftList.removeAt(index)
            else if (listSide == ListSide.RIGHT && index < rightList.size) rightList.removeAt(index)
            selection = null
            saveLists()
        }
    }

    fun selectItem(side: ListSide, index: Int) {
        selection = Pair(side, index)
    }

    fun moveRight() {
        selection?.let { (_, index) ->
            if (index < leftList.size) {
                val item = leftList.removeAt(index)
                rightList.add(item)
                selection = null
                saveLists()
            }
        }
    }

    fun moveLeft() {
        selection?.let { (_, index) ->
            if (index < rightList.size) {
                val item = rightList.removeAt(index)
                leftList.add(item)
                selection = null
                saveLists()
            }
        }
    }
}