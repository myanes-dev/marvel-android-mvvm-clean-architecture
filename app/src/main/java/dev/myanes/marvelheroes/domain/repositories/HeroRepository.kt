package dev.myanes.marvelheroes.domain.repositories

import dev.myanes.marvelheroes.domain.Either
import dev.myanes.marvelheroes.domain.Result
import dev.myanes.marvelheroes.domain.models.Hero

interface HeroRepository {
    suspend fun getLatestHeroes(): Either<Result.Error, List<Hero>>
    suspend fun getHeroDetail(): Either<Result.Error, Hero>
}