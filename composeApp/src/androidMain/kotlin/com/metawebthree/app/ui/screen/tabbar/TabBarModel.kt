package com.metawebthree.app.ui.screen.tabbar

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metawebthree.app.model.DataStoreConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed class TabBarIntent {
    data class ToggleResponsive(val responsive: Boolean?): TabBarIntent()
    data class ToggleLandscape(val landscape: Boolean?): TabBarIntent()
    object Show : TabBarIntent()
    object Hide : TabBarIntent()
    object Toggle : TabBarIntent()
}

@HiltViewModel
class TabBarModel @Inject constructor(private val dataStore: DataStore<Preferences>) : ViewModel() {
    var isVisible = MutableStateFlow(true)
        private set
    private var _isResponseScreenOrientation = MutableStateFlow(DataStoreConstants.RESPONSIVE_DEFAULT)
    val isResponseScreenOrientation = _isResponseScreenOrientation.asStateFlow()
    private var _isLandscape = MutableStateFlow(false)
    val isLandscape = _isLandscape.asStateFlow()
    init {
        viewModelScope.launch {
            initDataStore()
        }
    }

    suspend fun initDataStore() = withContext(Dispatchers.IO) {
        dataStore.data.map {
            it[DataStoreConstants.IS_RESPONSIVE] ?: DataStoreConstants.RESPONSIVE_DEFAULT
        }.first().takeIf { it }?.let {
            _isResponseScreenOrientation.run {
                value = true
            }
            _isLandscape.run {
                value = true
            }
        }?:let {
            _isResponseScreenOrientation.run {
                value = false
            }
            _isLandscape.run {
                value = false
            }
        }
    }

    fun sendIntent(intent: TabBarIntent) {
        when (intent) {
            is TabBarIntent.ToggleResponsive -> _isResponseScreenOrientation.run {
                value = intent.responsive ?: !_isResponseScreenOrientation.value
                viewModelScope.launch(Dispatchers.IO) {
                    dataStore.edit {
                        it[DataStoreConstants.IS_RESPONSIVE] = value
                    }
                }
            }
            is TabBarIntent.ToggleLandscape -> _isLandscape.run {
                value = intent.landscape ?: !_isLandscape.value
            }
            TabBarIntent.Show -> isVisible.value = true
            TabBarIntent.Hide -> isVisible.value = false
            TabBarIntent.Toggle -> isVisible.value = !isVisible.value
        }
    }
}