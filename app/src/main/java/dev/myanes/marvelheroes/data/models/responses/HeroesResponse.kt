package dev.myanes.marvelheroes.data.models.responses

import dev.myanes.marvelheroes.data.models.dto.HeroDto
import kotlinx.serialization.Serializable

@Serializable
data class HeroesResponse(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results : List<HeroDto>
)
