package com.example.foodsearch.presentation.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class BookFragmentViewModel

    : ViewModel() {
    // Переменная LiveData хранит позицию активной вкладки
    private val _currentTabPosition = MutableLiveData<Int>()
    val currentTabPosition: LiveData<Int> = _currentTabPosition

    // Функция для установки новой позиции вкладки
    fun setCurrentTabPosition(newPosition: Int) {
        _currentTabPosition.value = newPosition
    }


}