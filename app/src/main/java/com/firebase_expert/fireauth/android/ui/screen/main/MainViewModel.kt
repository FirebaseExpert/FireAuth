package com.firebase_expert.fireauth.android.ui.screen.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase_expert.fireauth.android.domain.repository.MainRepository
import com.firebase_expert.fireauth.android.util.FETCH_SALE_INFO_FAILURE
import com.firebase_expert.fireauth.android.util.FETCH_SALE_INFO_SUCCESS
import com.firebase_expert.fireauth.android.util.TAG
import com.firebase_expert.fireauth.android.util.UNKNOWN_FAILURE
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty1

class MainViewModel(
    private val mainRepo: MainRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<MainEvent>()
    val events = _events.asSharedFlow()

    init {
        getSaleInfo()
    }

    fun getSaleInfo() = viewModelScope.launch {
        try {
            setLoading(MainUiState::isGettingSaleInfo, true)
            val saleInfo = mainRepo.getSaleInfo()
            _uiState.update { state ->
                state.copy(saleInfo = saleInfo)
            }
            _events.emit(MainEvent.ShowToast(FETCH_SALE_INFO_SUCCESS))
        } catch (e: Exception) {
            Log.e(TAG, FETCH_SALE_INFO_FAILURE, e)
            _events.emit(MainEvent.ShowToast(e.message ?: UNKNOWN_FAILURE))
        } finally {
            setLoading(MainUiState::isGettingSaleInfo, false)
        }
    }

    fun setLoading(field: KProperty1<MainUiState, Boolean>, loading: Boolean) = _uiState.update { state ->
        when (field) {
            MainUiState::isGettingSaleInfo -> state.copy(isGettingSaleInfo = loading)
            else -> state
        }
    }
}