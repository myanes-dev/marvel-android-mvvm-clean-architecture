package dev.myanes.marvelheroes.domain.models


data class Hero(
    val id: String,
    val name: String,
    val imageURL: String,
    val description: String?,

    // Detail
    val comicsCount: Int? = 0,
    val storiesCount: Int? = 0,
    val eventsCount: Int? = 0,
    val seriesCount: Int? = 0
)
