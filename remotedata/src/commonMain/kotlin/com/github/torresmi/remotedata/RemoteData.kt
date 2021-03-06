package com.github.torresmi.remotedata

/**
 * Data type for representing all possible states when loading
 * data from a remote source.
 *
 * @param E the type of error possible.
 * @param A the type of data to attempt to load.
 */
sealed class RemoteData<out E, out A> {

    /**
     * The [RemoteData] operation has not started.
     */
    object NotAsked : RemoteData<Nothing, Nothing>() {
        override fun toString(): String = "NotAsked"
    }

    /**
     * The [RemoteData] operation is currently loading.
     */
    object Loading : RemoteData<Nothing, Nothing>() {
        override fun toString(): String = "Loading"
    }

    /**
     * The [RemoteData] operation loaded successfully with [data].
     */
    data class Success<out E, out A>(val data: A) : RemoteData<E, A>()

    /**
     * The [RemoteData] operation failed with [error].
     */
    data class Failure<out E, out A>(val error: E) : RemoteData<E, A>()

    fun isNotAsked(): Boolean = this is NotAsked

    fun isLoading(): Boolean = this is Loading

    fun isSuccess(): Boolean = this is Success

    fun isFailure(): Boolean = this is Failure

    companion object {

        /**
         * Helper to create a [RemoteData.Success] class with [data].
         */
        fun <A> succeed(data: A): RemoteData<Nothing, A> = Success(data)

        /**
         * Helper to create a [RemoteData.Failure] class with [error].
         */
        fun <E> fail(error: E): RemoteData<E, Nothing> = Failure(error)
    }
}
