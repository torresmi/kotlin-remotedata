package remotedata

/**
 * Data type for representing all possible states when loading
 * data from a remote source.
 *
 * @param E the type of error possible.
 * @param A the type of data to attempt to load.
 */
sealed class RemoteData<out E : Any, out A : Any> {

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
    data class Success<out E : Any, out A : Any>(val data: A) : RemoteData<E, A>()

    /**
     * The [RemoteData] operation failed with [error].
     */
    data class Failure<out E : Any, out A : Any>(val error: E) : RemoteData<E, A>()

    fun isNotAsked(): Boolean = this is NotAsked

    fun isLoading(): Boolean = this is Loading

    fun isSuccess(): Boolean = this is Success

    fun isFailure(): Boolean = this is Failure

    companion object {

        /**
         * Helper to create a [RemoteData.Success] class with [data].
         */
        fun <A : Any> succeed(data: A): RemoteData<Nothing, A> = RemoteData.Success(data)

        /**
         * Helper to create a [RemoteData.Failure] class with [error].
         */
        fun <E : Any> fail(error: E): RemoteData<E, Nothing> = RemoteData.Failure(error)
    }
}


/**
 * Convert this type into a [RemoteData.Success].
 */
fun <A : Any> A.success(): RemoteData<Nothing, A> = RemoteData.Success(this)

/**
 * Convert this type into a [RemoteData.Failure].
 */
fun <E : Any> E.failure(): RemoteData<E, Nothing> = RemoteData.Failure(this)
