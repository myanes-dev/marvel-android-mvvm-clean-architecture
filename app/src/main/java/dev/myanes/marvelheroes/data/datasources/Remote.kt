package dev.myanes.marvelheroes.data.datasources

import dev.myanes.marvelheroes.domain.Either
import dev.myanes.marvelheroes.domain.Result
import dev.myanes.marvelheroes.domain.models.Hero

interface Remote {
    suspend fun getLatestHeroes(): Either<Result.Error, List<Hero>>
    suspend fun getHeroDetail(id: String): Either<Result.Error, Hero>
}