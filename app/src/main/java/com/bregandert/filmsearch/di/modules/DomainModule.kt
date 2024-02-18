package com.bregandert.filmsearch.di.modules

import android.content.Context
import com.bregandert.filmsearch.data.MainRepository
import com.bregandert.filmsearch.data.PreferenceProvider
import com.bregandert.filmsearch.domain.Interactor

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule(val context: Context) {

        @Provides
        fun provideContext() = context

        @Singleton
        @Provides
        fun providePreferences(context: Context) = PreferenceProvider(context)

        @Provides
        @Singleton
        fun provideInteractor(repository: MainRepository, tmdbApi: com.bregandert.retrofit.TmdbApi, preferenceProvider: PreferenceProvider) =
        Interactor(repo = repository, retrofitService = tmdbApi, preferences = preferenceProvider)

}

