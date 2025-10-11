package com.example.foodsearch.presentation.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor() : ViewModel() {
    
    private val _currentTabPosition = MutableStateFlow(0)
    val currentTabPosition: StateFlow<Int> = _currentTabPosition.asStateFlow()

    fun setCurrentTabPosition(newPosition: Int) {
        _currentTabPosition.value = newPosition
    }
}
