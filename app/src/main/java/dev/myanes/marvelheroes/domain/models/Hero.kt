package dev.myanes.marvelheroes.domain.models


data class Hero(
    val id: String,
    val name: String,
    val imageURL: String,
    val description: String?
)

object FakeHeros {

    val LIST_ITEMS: List<Hero> = buildFakeList()

    private fun buildFakeList(): List<Hero> {
        var list: MutableList<Hero> = mutableListOf<Hero>()
        for (i in 1..10) {
            list.add(Hero(i.toString(), "Name $i", "", ""))
        }
        return  list
    }

}
