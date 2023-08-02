package com.metawebthree.keykeeper.model

import androidx.datastore.preferences.core.booleanPreferencesKey

object DataStoreConstants {
    const val DATA_STORE_GLOBAL_NAME = "global"
    const val RESPONSIVE_DEFAULT = false
    val IS_RESPONSIVE = booleanPreferencesKey("isResponsive")
}