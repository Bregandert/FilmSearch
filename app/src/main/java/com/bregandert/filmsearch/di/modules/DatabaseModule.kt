package com.bregandert.filmsearch.di.modules

import com.bregandert.filmsearch.data.MainRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideRepository() = MainRepository()
//    @Binds
//    @Singleton
//    abstract fun getRepository(repository: MainRepository) : FilmRepositoryInterface


}

//interface FilmRepositoryInterface
//class MainRepository @Inject constructor() : FilmRepositoryInterface