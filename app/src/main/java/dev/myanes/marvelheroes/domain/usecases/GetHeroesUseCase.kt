package dev.myanes.marvelheroes.domain.usecases

import dev.myanes.marvelheroes.domain.Either
import dev.myanes.marvelheroes.domain.Result
import dev.myanes.marvelheroes.domain.models.FakeHeroes
import dev.myanes.marvelheroes.domain.models.Hero
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class GetHeroesUseCase() {
    suspend operator fun invoke(): Either<Result.Error, List<Hero>> =
        withContext(Dispatchers.IO) {
            delay(2000)
            Either.Right(FakeHeroes.LIST_ITEMS)
        }
}