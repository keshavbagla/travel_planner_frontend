package com.example.travel_planner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ResourceViewModel<T>(
    private var load: suspend () -> T // <-- changed: was val, now var so refresh() can swap in a new query
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<T>>(UiState.Loading)
    val uiState: StateFlow<UiState<T>> = _uiState.asStateFlow()

    init {
        refresh()
    }
    fun refresh(newLoad: (suspend () -> T)? = null) {
        if (newLoad != null) load = newLoad
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = try {
                UiState.Success(load())
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Something went wrong")
            }
        }
    }
}