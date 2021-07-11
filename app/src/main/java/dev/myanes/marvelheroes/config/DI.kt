package dev.myanes.marvelheroes.config

import dev.myanes.marvelheroes.data.datasources.HttpDataSource
import dev.myanes.marvelheroes.data.datasources.Remote
import dev.myanes.marvelheroes.data.repositories.HeroRepositoryImpl
import dev.myanes.marvelheroes.domain.repositories.HeroRepository
import dev.myanes.marvelheroes.domain.usecases.GetHeroesUseCase
import dev.myanes.marvelheroes.presentation.screens.herodetail.HeroDetailViewModel
import dev.myanes.marvelheroes.presentation.screens.herolist.HeroListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module


fun getDIModules(): List<Module> {
    return listOf(
        dataModule,
        domainModule,
        presentationModule
    )
}

val dataModule = module {
    // DataSources
    single<Remote> { HttpDataSource() }

    // Repositories
    factory<HeroRepository> {
        HeroRepositoryImpl(remoteDataSource = get())
    }
}

val domainModule = module {
    // UseCases
    factory { GetHeroesUseCase(heroRepository = get()) }
}

val presentationModule = module {
    // ViewModels
    viewModel { HeroListViewModel(getHeroesUseCase = get()) }
    viewModel { HeroDetailViewModel() }
}
