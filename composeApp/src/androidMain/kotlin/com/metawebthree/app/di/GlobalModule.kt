package com.metawebthree.keykeeper.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.metawebthree.keykeeper.model.DataStoreConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object GlobalModule {
    @Provides
    fun getDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DataStoreConstants.DATA_STORE_GLOBAL_NAME)