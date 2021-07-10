package dev.myanes.marvelheroes.domain


/**
 * Custom type to support two possible fixed types (Left or Right). Inspired by arrow-kt
 * see: https://arrow-kt.io/docs/0.10/apidocs/arrow-core-data/arrow.core/-either/
 */
sealed class Either<L, R> {

    class Left<L, R>(val error: L) : Either<L, R>() {
        override fun toString(): String = "Left $error"
    }

    class Right<L, R>(val success: R) : Either<L, R>() {
        override fun toString(): String = "Right $success"
    }

    fun fold(error: (L) -> Unit, success: (R) -> Unit) {
        when (this) {
            is Left -> error(this.error)
            is Right -> success(this.success)
        }
    }

}
