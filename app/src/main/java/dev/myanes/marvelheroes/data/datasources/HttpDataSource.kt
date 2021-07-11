package dev.myanes.marvelheroes.data.datasources

import dev.myanes.marvelheroes.data.models.dto.toDomainModel
import dev.myanes.marvelheroes.data.models.responses.HeroesResponse
import dev.myanes.marvelheroes.data.models.responses.MarvelResponse
import dev.myanes.marvelheroes.domain.Either
import dev.myanes.marvelheroes.domain.Result
import dev.myanes.marvelheroes.domain.md5
import dev.myanes.marvelheroes.domain.models.Hero
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.lang.Exception
import java.util.*

class HttpDataSource : Remote {

    private val privateKey: String = "9a93e2643ebdad46ce5d7644574bfc2871ccfad4"
    private val publicKey: String = "92be89874988ec1bd409cb1b1407e768"
    private val baseURL: String = "https://gateway.marvel.com:443/v1/public"

    private val client = HttpClient {
        defaultRequest {
            val endpointBuilder =
                URLBuilder(urlString = baseURL)
            url {
                protocol = endpointBuilder.protocol
                host = endpointBuilder.host
                encodedPath = endpointBuilder.encodedPath + encodedPath
                port = endpointBuilder.port
            }
            val ts = Date().time.toString()
            val hash = "$ts$privateKey$publicKey".md5
            parameter("apikey", publicKey)
            parameter("ts", ts)
            parameter("hash", hash)
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer(
                kotlinx.serialization.json.Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    override suspend fun getLatestHeroes(): Either<Result.Error, List<Hero>> {
        return try {
            val response = client.get<MarvelResponse<HeroesResponse>> {
                url{
                    path("characters")
                }
                parameter("orderBy", "-modified")
            }
            Either.Right(response.data.results.map { it.toDomainModel() })
        } catch (e: Exception) {
            Either.Left(Result.Error.UnKnown)
        }
    }

    override suspend fun getHeroDetail(id: String): Either<Result.Error, Hero> {
        return try {
            val response = client.get<MarvelResponse<HeroesResponse>> {
                url{
                    path("characters", id)
                }
            }
            Either.Right(response.data.results[0]?.toDomainModel())
        } catch (e: Exception) {
            Either.Left(Result.Error.UnKnown)
        }
    }
}