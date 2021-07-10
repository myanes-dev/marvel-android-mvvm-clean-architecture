package dev.myanes.marvelheroes.data.models.dto

import dev.myanes.marvelheroes.domain.models.Hero
import kotlinx.serialization.Serializable

@Serializable
data class HeroDto(
    val id: String,
    val name: String,
    val description: String,
    val thumbnail: ThumbnailDto
)

@Serializable
data class ThumbnailDto(
    val path: String,
    val extension: String
)

fun HeroDto.toDomainModel() = Hero(
    id = this.id,
    name = this.name,
    description = this.description,
    imageURL = "${this.thumbnail.path}.${this.thumbnail.extension}"
)