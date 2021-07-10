package dev.myanes.marvelheroes.config

import dev.myanes.marvelheroes.domain.usecases.GetHeroesUseCase
import dev.myanes.marvelheroes.presentation.screens.herolist.HeroListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module


fun getDIModules(): List<Module> {
    return listOf(
        domainModule,
        presentationModule
    )
}

val domainModule = module {
    // UseCases
    factory { GetHeroesUseCase() }
}

val presentationModule = module {
    // ViewModels
    viewModel { HeroListViewModel(getHeroesUseCase = get()) }
}
