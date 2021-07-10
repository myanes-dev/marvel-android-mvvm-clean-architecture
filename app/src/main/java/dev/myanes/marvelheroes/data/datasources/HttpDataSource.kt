package dev.myanes.marvelheroes.data.datasources

import dev.myanes.marvelheroes.domain.Either
import dev.myanes.marvelheroes.domain.Result
import dev.myanes.marvelheroes.domain.models.FakeHeroes
import dev.myanes.marvelheroes.domain.models.Hero
import kotlinx.coroutines.delay

class HttpDataSource : Remote {
    override suspend fun getLatestHeroes(): Either<Result.Error, List<Hero>> {
        delay(2000)
        return Either.Right(FakeHeroes.LIST_ITEMS)
    }

    override suspend fun getHeroDetail(): Either<Result.Error, Hero> {
        TODO("Not yet implemented")
    }
}