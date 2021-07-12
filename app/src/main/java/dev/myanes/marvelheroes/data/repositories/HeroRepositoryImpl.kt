package dev.myanes.marvelheroes.data.repositories

import dev.myanes.marvelheroes.data.datasources.Remote
import dev.myanes.marvelheroes.domain.Either
import dev.myanes.marvelheroes.domain.Result
import dev.myanes.marvelheroes.domain.models.Hero
import dev.myanes.marvelheroes.domain.repositories.HeroRepository

class HeroRepositoryImpl(private val remoteDataSource: Remote) : HeroRepository {
    override suspend fun getLatestHeroes(): Either<Result.Error, List<Hero>> =
        remoteDataSource.getLatestHeroes()

    override suspend fun searchHeroesByName(name: String): Either<Result.Error, List<Hero>> =
        remoteDataSource.getHeroesByName(name)

    override suspend fun getHeroDetail(id: String): Either<Result.Error, Hero> =
        remoteDataSource.getHeroDetail(id)

}