package dev.myanes.marvelheroes.domain


/**
 * Custom return type to handle error and success case. Meant to be used with @Either.
 * Inspired by arrow-kt.
 * See: https://arrow-kt.io/docs/0.10/apidocs/arrow-core-data/arrow.core/-either/
 */
sealed class Result {
    sealed class Error : Result() {
        class Friendly(val msg: String) : Error()
        object UnKnown : Error()
    }

    object Success : Result()

}
