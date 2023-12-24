package com.bregandert.filmsearch.di.modules

import android.content.Context
import com.bregandert.filmsearch.data.MainRepository
import com.bregandert.filmsearch.data.TmdbApi
import com.bregandert.filmsearch.domain.Interactor
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule(val context: Context) {

        @Provides
        fun provideContext() = context

        @Provides
        @Singleton
        fun provideInteractor(repository: MainRepository, tmdbApi: TmdbApi) =
        Interactor(repo = repository, retrofitService = tmdbApi)

}

