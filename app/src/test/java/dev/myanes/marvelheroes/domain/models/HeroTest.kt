package dev.myanes.marvelheroes.domain.models


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