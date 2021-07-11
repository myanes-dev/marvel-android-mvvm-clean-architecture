package dev.myanes.marvelheroes.domain.usecases

import dev.myanes.marvelheroes.domain.Either
import dev.myanes.marvelheroes.domain.Result
import dev.myanes.marvelheroes.domain.models.Hero
import dev.myanes.marvelheroes.domain.repositories.HeroRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetHeroDetailUseCase(private val heroRepository: HeroRepository) {
    suspend operator fun invoke(id: String): Either<Result.Error, Hero> =
        withContext(Dispatchers.IO) { heroRepository.getHeroDetail(id) }
}