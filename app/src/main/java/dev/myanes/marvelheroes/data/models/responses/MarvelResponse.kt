package dev.myanes.marvelheroes.data.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class MarvelResponse<T>(
    val code: Int,
    val status:  String,
    val data : T
)
