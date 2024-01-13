package com.bregandert.filmsearch.di

import com.bregandert.filmsearch.di.modules.DatabaseModule
import com.bregandert.filmsearch.di.modules.DomainModule
import com.bregandert.filmsearch.di.modules.RemoteModule
import com.bregandert.filmsearch.viewmodel.FavoriteFragmentViewModel
import com.bregandert.filmsearch.viewmodel.HomeFragmentViewModel
import com.bregandert.filmsearch.viewmodel.SettingsFragmentViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
//    Внедряем все модули, нужные для этого компонента
    modules = [
        RemoteModule::class,
        DatabaseModule::class,
        DomainModule::class
    ]
)

interface AppComponent {
//    метод, чтобы внедрять зависимости HomeFragmentViewModel
    fun inject(homeFragmentViewModel: HomeFragmentViewModel)
    fun inject(favoriteFragmentViewModel: FavoriteFragmentViewModel)
    fun inject(settingsFragmentViewModel: SettingsFragmentViewModel)
}