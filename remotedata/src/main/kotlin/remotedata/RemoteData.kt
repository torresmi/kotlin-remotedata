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
 * Transform the data of a [RemoteData] value with [f]
 */
inline fun <A : Any, B : Any, E : Any> RemoteData<E, A>.map(f: (A) -> B): RemoteData<E, B> =
        mapBoth(
                { it },
                { f(it) }
        )

/**
 * Transform the error of a [RemoteData] value with [f].
 */
inline fun <A : Any, E : Any, F : Any> RemoteData<E, A>.mapError(f: (E) -> F): RemoteData<F, A> =
        mapBoth(
                { f(it) },
                { it }
        )

/**
 * Apply both [failure] and [success] mapping functions to this [RemoteData]. Combines [map] and [mapError]
 * into one.
 */
inline fun <A : Any, B : Any, C : Any, E : Any> RemoteData<E, A>.mapBoth(
        failure: (E) -> C,
        success: (A) -> B
): RemoteData<C, B> =
        when (this) {
            is RemoteData.NotAsked -> this
            is RemoteData.Loading -> this
            is RemoteData.Success -> RemoteData.Success(success(data))
            is RemoteData.Failure -> RemoteData.Failure(failure(error))
        }

/**
 * Applies the function from the [RemoteData.Success] data value to [other] data value.
 * Any other state other than [RemoteData.Success] with be returned, with left precedence.
 */
infix fun <A : Any, B : Any, F : Any> RemoteData<F, (A) -> B>.andMap(other: RemoteData<F, A>): RemoteData<F, B> =
        when (this) {
            is RemoteData.Success -> other.map(data)
            is RemoteData.Failure -> RemoteData.Failure(error)
            is RemoteData.Loading -> this
            is RemoteData.NotAsked -> this
        }

/**
 * Chain the current [RemoteData] with a [f] that returns the next [RemoteData].
 * Only continues the chain if it is currently a [RemoteData.Success].
 */
inline fun <A : Any, B : Any, E : Any> RemoteData<E, A>.flatMap(f: (A) -> RemoteData<E, B>): RemoteData<E, B> =
        when (this) {
            is RemoteData.Success -> f(data)
            is RemoteData.Failure -> RemoteData.Failure(error)
            is RemoteData.Loading -> this
            is RemoteData.NotAsked -> this
        }

/**
 * Convert this type into a [RemoteData.Success].
 */
fun <A : Any> A.success(): RemoteData<Nothing, A> = RemoteData.Success(this)

/**
 * Convert this type into a [RemoteData.Failure].
 */
fun <E : Any> E.failure(): RemoteData<E, Nothing> = RemoteData.Failure(this)

/**
 * Attempt to get the value out of the [RemoteData] if it was successful.
 *
 * This should be used with caution, as [getOrElse] or unwrapping with a 'when' statement help
 * handle all other cases better than a nullable type.
 */
fun <A : Any, E : Any> RemoteData<E, A>.get(): A? =
        when (this) {
            is RemoteData.Success -> data
            else -> null
        }

/**
 * Return either the [RemoteData.Success] data or a provided [default].
 */
fun <A : Any, E : Any> RemoteData<E, A>.getOrElse(default: A): A = getOrElse { default }

/**
 * Return either the [RemoteData.Success] data or a provided [default].
 */
fun <A : Any, E : Any> RemoteData<E, A>.getOrElse(default: () -> A): A =
        when (this) {
            is RemoteData.Success -> data
            else -> default()
        }

/**
 * Combines all the [RemoteData] data values into a [Pair] if both are successful.
 * Any state other than [RemoteData.Success] with be returned, with left precedence.
 */
fun <A : Any, B : Any, E : Any> RemoteData<E, A>.append(other: RemoteData<E, B>): RemoteData<E, Pair<A, B>> =
        flatMap { a -> other.map { b -> a to b } }

/**
 * Combines all the [RemoteData] data values into a [Triple] if all are successful.
 * Any state other than [RemoteData.Success] with be returned, with left precedence.
 */
fun <A : Any, B : Any, C : Any, E : Any> RemoteData<E, A>.append(
        other1: RemoteData<E, B>,
        other2: RemoteData<E, C>
): RemoteData<E, Triple<A, B, C>> = append(other1).flatMap { (a, b) -> other2.map { c -> Triple(a, b, c) } }

/**
 * Combine with [other] using [f] if both are [RemoteData.Success].
 * Any other state other than [RemoteData.Success] with be returned, with left precedence.
 */
inline fun <A: Any, B : Any, C : Any, E : Any> RemoteData<E, A>.mergeWith(
        other: RemoteData<E, B>,
        crossinline f: (A, B) -> C
): RemoteData<E, C> = append(other).map { (a, b) -> f(a,b) }

/**
 * Combine with 2 other [RemoteData] values with [f] if all are [RemoteData.Success]
 * Any other state other than [RemoteData.Success] with be returned, with left precedence.
 */
inline fun <A: Any, B : Any, C : Any, D : Any, E : Any> RemoteData<E, A>.mergeWith(
        other1: RemoteData<E, B>,
        other2: RemoteData<E, C>,
        crossinline f: (A, B, C) -> D
): RemoteData<E, D> = append(other1, other2).map { (a, b, c) -> f(a, b, c) }

/**
 * Combine with 3 other [RemoteData] values with [f] if all are [RemoteData.Success].
 * Any other state other than [RemoteData.Success] with be returned, with left precedence.
 */
inline fun <A: Any, B : Any, C : Any, D : Any, E : Any, F : Any> RemoteData<F, A>.mergeWith(
        other1: RemoteData<F, B>,
        other2: RemoteData<F, C>,
        other3: RemoteData<F, D>,
        crossinline f: (A, B, C, D) -> E
): RemoteData<F, E> =
        append(other1, other2).flatMap { (a, b, c) -> other3.map { d -> f(a, b, c, d) } }
