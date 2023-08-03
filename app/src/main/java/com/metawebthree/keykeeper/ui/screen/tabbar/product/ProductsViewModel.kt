package com.metawebthree.keykeeper.ui.screen.tabbar.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

sealed class ProductIntent() {
    data class ToggleHoursSystem(val to24: Boolean? = null) : ProductIntent()
    data class ToggleLoadingView(val loading: Boolean?) : ProductIntent()
}

class ProductsViewModel : ViewModel() {
    private var _dateState = MutableStateFlow(DateState())
    val dateState = _dateState.asStateFlow()
    private var _timeState = MutableStateFlow(TimeState())
    val timeState = _timeState.asStateFlow()
    private var _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()
    var stayUpdate = false

    init {
        initDateTime()
        keepUpdateDateTime()
    }

    private fun initDateTime() {
        LocalDateTime.now().apply {
            updateDate(this)
            _timeState.apply {
                value = value.copy(hour = hour, minute = minute, second = second)
            }
            _uiState.apply {
                value =
                    value.copy(meridiemType = if (hour > 12) MeridiemType.AM else MeridiemType.PM)
            }
        }
    }

    private fun keepUpdateDateTime() {
        stayUpdate = true
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                if (stayUpdate)
                    updateTime()
                else
                    break
                delay(1000)
            }
        }
    }

    fun stopUpdateDateTime() {
        stayUpdate = false
    }

    private fun updateDate(localDateTime: LocalDateTime?) {
        (localDateTime ?: LocalDateTime.now()).apply {
            _dateState.apply {
                value = value.copy(
                    date = "$year/$monthValue/$dayOfMonth",
                    week = value.getWeekName(dayOfWeek.value)
                )
            }
        }
    }

    private fun updateTime() {
        _timeState.apply {
            var hour = value.hour
            var minute = value.minute
            var second = value.second
            second++
            if (second >= 60) {
                second %= 60
                minute++
            }
            if (minute >= 60) {
                minute %= 60
                hour++
            }
            if (hour >= 24) {
                hour %= 24
            }
            value = value.copy(second = second, minute = minute, hour = hour)
        }
        _uiState.apply {
            value.meridiemType =
                if (_timeState.value.hour > 12) MeridiemType.AM else MeridiemType.PM
        }
    }

    fun sendIntent(intent: ProductIntent) {
        when (intent) {
            is ProductIntent.ToggleHoursSystem -> {
                _uiState.run {
                    value = value.copy(is24hours = intent.to24 ?: !value.is24hours)
                }
            }

            is ProductIntent.ToggleLoadingView -> {
                _uiState.apply {
                    value = value.copy(loading = intent.loading ?: !value.loading)
                }
            }

            else -> {
                throw RuntimeException("Unknown Produ ct Intent${intent}")
            }
        }
    }
}