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

object FakeHeroes {

    val LIST_ITEMS: List<Hero> = buildFakeList()
    val VALID_ITEM: Hero = Hero(
        "1",
        "Felipe",
        "URL_ERROR",
        "Description test Description test Description test Description test Description test "
    )

    private fun buildFakeList(): List<Hero> {
        var list: MutableList<Hero> = mutableListOf<Hero>()
        for (i in 1..10) {
            list.add(Hero(i.toString(), "Name $i", "", ""))
        }
        return list
    }

}
