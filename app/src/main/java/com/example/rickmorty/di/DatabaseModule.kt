package com.example.rickmorty.di

import android.content.Context
import androidx.room.Room
import com.example.rickmorty.data.local.AppDatabase
import com.example.rickmorty.data.local.dao.CharacterDao
import com.example.rickmorty.data.local.dao.LocationDao
import com.example.rickmorty.data.local.dao.OriginDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(appContext, AppDatabase::class.java, "rickmorty.db").build()

    @Provides
    fun provideCharacterDao(db: AppDatabase): CharacterDao = db.characterDao()

    @Provides
    fun provideLocationDao(db: AppDatabase): LocationDao = db.locationDao()

    @Provides
    fun provideOriginDao(db: AppDatabase): OriginDao = db.originDao()
}
