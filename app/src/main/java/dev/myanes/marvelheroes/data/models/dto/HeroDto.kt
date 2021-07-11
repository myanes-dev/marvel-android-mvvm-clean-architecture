package dev.myanes.marvelheroes.data.models.dto

import dev.myanes.marvelheroes.domain.models.Hero
import kotlinx.serialization.Serializable

@Serializable
data class HeroDto(
    val id: String,
    val name: String,
    val description: String,
    val thumbnail: ThumbnailDto,
    val comics: Resource,
    val stories: Resource,
    val events: Resource,
    val series: Resource
)

@Serializable
data class ThumbnailDto(
    val path: String,
    val extension: String
)

@Serializable
data class Resource(
    val available: Int //Total count
)

fun HeroDto.toDomainModel() = Hero(
    id = this.id,
    name = this.name,
    description = this.description,
    imageURL = "${this.thumbnail.path}.${this.thumbnail.extension}",
    comicsCount = this.comics.available,
    seriesCount = this.series.available,
    eventsCount = this.events.available,
    storiesCount = this.stories.available
)