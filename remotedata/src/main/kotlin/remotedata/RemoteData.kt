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
     * The [RemoteData] operation loaded successfully with data.
     */
    data class Success<out E : Any, out A : Any>(val data: A) : RemoteData<E, A>()

    /**
     * The [RemoteData] operation failed with the error provided.
     */
    data class Failure<out E : Any, out A : Any>(val error: E) : RemoteData<E, A>()

    /**
     * Transform the data of a [RemoteData.Success] value.
     *
     * @param f mapping function to be applied if this [RemoteData] is a [RemoteData.Success].
     * @return [RemoteData] with the new success type.
     */
    inline fun <B : Any> map(f: (A) -> B): RemoteData<E, B> {
        return mapBoth(
                { it },
                { f(it) }
        )
    }

    /**
     * Transform the error of a [RemoteData.Failure] value.
     *
     * @param f mapping function to be applied if this [RemoteData] is a [RemoteData.Failure].
     * @return [RemoteData] with the new failure type.
     */
    inline fun <F : Any> mapError(f: (E) -> F): RemoteData<F, A> {
        return mapBoth(
                { f(it) },
                { it }
        )
    }

    /**
     * Provide both a success and failure mapping functions. Combines @see [map] and @see [mapError]
     * into one.
     *
     * @param failure mapping function to be applied if this [RemoteData] is a [RemoteData.Failure].
     * @param success mapping function to be applied if this [RemoteData] is a [RemoteData.Success].
     * @return [RemoteData] with the new failure and success types.
     */
    inline fun <B : Any, C : Any> mapBoth(failure: (E) -> C, success: (A) -> B): RemoteData<C, B> {
        return when (this) {
            is NotAsked -> {
                this
            }
            is Loading -> {
                this
            }
            is Success -> {
                Success(success(data))
            }
            is Failure -> {
                Failure(failure(error))
            }
        }
    }

    fun isNotAsked(): Boolean = this is NotAsked

    fun isLoading(): Boolean = this is Loading

    fun isSuccess(): Boolean = this is Success

    fun isFailure(): Boolean = this is Failure

    companion object {

        /**
         * Helper to create a [RemoteData.Success] class with the provided data.
         * @param data the data to initialize with.
         * @return [RemoteData] with the provided data.
         */
        fun <A : Any> succeed(data: A): RemoteData<Nothing, A> = RemoteData.Success(data)

        /**
         * Helper to create a [RemoteData.Failure] class with the provided error.
         * @param error the error to initialize with.
         * @return [RemoteData] with the provided error.
         */
        fun <E : Any> fail(error: E): RemoteData<E, Nothing> = RemoteData.Failure(error)
    }
}

// Helpers

/**
 * Applies the function from the [RemoteData.Success] data value to another [RemoteData] value.
 * The first [RemoteData.Failure] will cause the return value to be that failure.
 * 
 * @param other another [RemoteData] to have the current function value be applied to its value.
 * @return [RemoteData] with the function value applied.
 */
infix fun <A : Any, B : Any, F : Any> RemoteData<F, (A) -> B>.andMap(other: RemoteData<F, A>): RemoteData<F, B> {
    return when (this) {
        is RemoteData.Success -> {
            other.map(data)
        }
        is RemoteData.Failure -> {
            RemoteData.Failure(error)
        }
        is RemoteData.Loading -> {
            this
        }
        is RemoteData.NotAsked -> {
            this
        }
    }
}

/**
 * Chain the current [RemoteData] with a function that returns the next [RemoteData].
 * Only continues the chain if it is currently a [RemoteData.Success].
 *
 * @param f function that returns another @see [RemoteData] value to add to the chain.
 * @return [RemoteData] the latest [RemoteData], with the new one if it was applied.
 */
inline fun <A : Any, B : Any, E : Any> RemoteData<E, A>.flatMap(f: (A) -> RemoteData<E, B>): RemoteData<E, B> {
    return when (this) {
        is RemoteData.Success -> {
            f(data)
        }
        is RemoteData.Failure -> {
            RemoteData.Failure(error)
        }
        is RemoteData.Loading -> {
            this
        }
        is RemoteData.NotAsked -> {
            this
        }
    }
}

/**
 * Convert this type into a [RemoteData.Success].
 * @return [RemoteData] representing a successful [RemoteData] operation.
 */
fun <A : Any> A.success(): RemoteData<Nothing, A> = RemoteData.Success(this)

/**
 * Convert this type into a [RemoteData.Failure].
 * @return [RemoteData] representing a failed [RemoteData] operation.
 */
fun <E : Any> E.failure(): RemoteData<E, Nothing> = RemoteData.Failure(this)

/**
 * Attempt to get the value out of the [RemoteData] operation if it was successful.
 *
 * This should be used with caution, as @see [getOrElse] or unwrapping with a 'when' statement help
 * handle all other cases better than a nullable type.
 *
 * @return the value of a successful operation, or null.
 */
fun <A : Any, E : Any> RemoteData<E, A>.get(): A? {
    return when (this) {
        is RemoteData.Success -> {
            data
        }
        else -> {
            null
        }
    }
}

/**
 * Return either the [RemoteData.Success] data or a provided default.
 *
 * @param default data to be returned if the operation is not successful.
 * @return either the data from a successful operation or the default.
 */
fun <A : Any, E : Any> RemoteData<E, A>.getOrElse(default: A): A {
    return getOrElse { default }
}

/**
 * Return either the [RemoteData.Success] data or a provided default.
 *
 * @param default a function that returns the default to be used if the operation is not successful.
 * @return either the data from a successful operation or the default.
 */
fun <A : Any, E : Any> RemoteData<E, A>.getOrElse(default: () -> A): A {
    return when (this) {
        is RemoteData.Success -> {
            data
        }
        else -> {
            default()
        }
    }
}

/**
 * Combines all the [RemoteData] data values into a @see [Pair] if both are successful.
 * Only appends if both are [RemoteData.Success].
 *
 * @param other [RemoteData] value to append.
 * @return [RemoteData] with a [Pair] of both success values if all were successful.
 */
fun <A : Any, B : Any, E : Any> RemoteData<E, A>.append(other: RemoteData<E, B>): RemoteData<E, Pair<A, B>> {
    return flatMap { a -> other.map { b -> a to b } }
}

/**
 * Combines all the [RemoteData] data values into a @see [Triple] if all are successful.
 * Only appends if all are [RemoteData.Success].
 *
 * @param other1 [RemoteData] value to append.
 * @param other2 [RemoteData] value to append.
 * @return [RemoteData] with a [Triple] of all success values if all were successful.
 */
fun <A : Any, B : Any, C : Any, E : Any> RemoteData<E, A>.append(
        other1: RemoteData<E, B>,
        other2: RemoteData<E, C>): RemoteData<E, Triple<A, B, C>> {
    return append(other1).flatMap { (a, b) -> other2.map { c -> Triple(a, b, c) } }
}

/**
 * Combine with another [RemoteData].
 * Only merges if both are [RemoteData.Success].
 *
 * @param other [RemoteData] to merge with.
 * @param f mapping function to be applied if every [RemoteData] is a [RemoteData.Success].
 */
inline fun <A: Any, B : Any, C : Any, E : Any> RemoteData<E, A>.mergeWith(
        other: RemoteData<E, B>,
        crossinline f: (A, B) -> C): RemoteData<E, C> {
    return append(other).map { (a, b) -> f(a,b) }
}

/**
 * Combine with 2 other [RemoteData] values.
 * Only merges if all are [RemoteData.Success].
 *
 * @param other1 [RemoteData] to merge with.
 * @param other2 [RemoteData] to merge with.
 * @param f mapping function to be applied if every [RemoteData] is a [RemoteData.Success].
 */
inline fun <A: Any, B : Any, C : Any, D : Any, E : Any> RemoteData<E, A>.mergeWith(
         other1: RemoteData<E, B>,
         other2: RemoteData<E, C>,
         crossinline f: (A, B, C) -> D): RemoteData<E, D> {
    return append(other1, other2).map { (a, b, c) -> f(a, b, c) }
}

/**
 * Combine with 3 other [RemoteData] values.
 * Only merges if all are [RemoteData.Success].
 *
 * @param other1 [RemoteData] to merge with.
 * @param other2 [RemoteData] to merge with.
 * @param other3 [RemoteData] to merge with.
 * @param f mapping function to be applied if every [RemoteData] is a [RemoteData.Success]
 */
inline fun <A: Any, B : Any, C : Any, D : Any, E : Any, F : Any> RemoteData<F, A>.mergeWith(
        other1: RemoteData<F, B>,
        other2: RemoteData<F, C>,
        other3: RemoteData<F, D>,
        crossinline f: (A, B, C, D) -> E): RemoteData<F, E> {
    return append(other1, other2).flatMap { (a, b, c) -> other3.map { d -> f(a, b, c, d) } }
}
