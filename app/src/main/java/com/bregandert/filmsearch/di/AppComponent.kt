package com.bregandert.filmsearch.di

import com.bregandert.filmsearch.di.modules.DatabaseModule
import com.bregandert.filmsearch.di.modules.DomainModule
import com.bregandert.filmsearch.viewmodel.DetailsFragmentViewModel
import com.bregandert.filmsearch.viewmodel.FavoriteFragmentViewModel
import com.bregandert.filmsearch.viewmodel.HomeFragmentViewModel
import com.bregandert.filmsearch.viewmodel.SettingsFragmentViewModel
import com.bregandert.retrofit.RemoteProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(

    dependencies = [RemoteProvider::class],
//    Внедряем все модули, нужные для этого компонента
    modules = [
        DatabaseModule::class,
        DomainModule::class
    ]
)

interface AppComponent {
//    метод, чтобы внедрять зависимости HomeFragmentViewModel
    fun inject(homeFragmentViewModel: HomeFragmentViewModel)
    fun inject(detailsFragmentViewModel: DetailsFragmentViewModel)
    fun inject(favoriteFragmentViewModel: FavoriteFragmentViewModel)
    fun inject(settingsFragmentViewModel: SettingsFragmentViewModel)
}