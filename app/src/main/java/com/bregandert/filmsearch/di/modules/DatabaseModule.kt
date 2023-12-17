package com.bregandert.filmsearch.di.modules

import android.content.Context
import com.bregandert.filmsearch.data.MainRepository
import com.bregandert.filmsearch.data.db.DatabaseHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabaseHelper(context: Context) = DatabaseHelper(context)

    @Provides
    @Singleton
    fun provideRepository(databaseHelper: DatabaseHelper) = MainRepository(databaseHelper)



}

