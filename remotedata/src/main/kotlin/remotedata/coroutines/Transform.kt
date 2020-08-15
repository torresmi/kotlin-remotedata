package remotedata.coroutines

import remotedata.RemoteData
import remotedata.map
import remotedata.mapError

/**
 * Transform the data of a [RemoteData] value with [transform]
 */
suspend inline fun <A : Any, B : Any, E : Any> RemoteData<E, A>.map(
    crossinline transform: suspend (A) -> B
): RemoteData<E, B> = mapBoth({ it }, transform)

/**
 * Transform the error of a [RemoteData] value with [transform].
 */
suspend inline fun <A : Any, E : Any, F : Any> RemoteData<E, A>.mapError(
    crossinline transform: suspend (E) -> F
): RemoteData<F, A> = mapBoth(transform, { it })

/**
 * Apply both [failure] and [success] mapping functions to this [RemoteData].
 * Combines [map] and [mapError] into one.
 */
suspend inline fun <A : Any, B : Any, C : Any, E : Any> RemoteData<E, A>.mapBoth(
    crossinline failure: suspend (E) -> C,
    crossinline success: suspend (A) -> B
): RemoteData<C, B> = when (this) {
    is RemoteData.NotAsked -> this
    is RemoteData.Loading -> this
    is RemoteData.Success -> RemoteData.Success(success(data))
    is RemoteData.Failure -> RemoteData.Failure(failure(error))
}

/**
 * Applies the function from the [RemoteData.Success] data value to [other] data value.
 * Any other state other than [RemoteData.Success] with be returned, with left precedence.
 */
suspend infix fun <A : Any, B : Any, F : Any> RemoteData<F, suspend (A) -> B>.andMap(
    other: RemoteData<F, A>
): RemoteData<F, B> = when (this) {
    is RemoteData.NotAsked -> this
    is RemoteData.Loading -> this
    is RemoteData.Success -> other.map(data)
    is RemoteData.Failure -> RemoteData.Failure(error)
}

/**
 * Chain the current [RemoteData] with a [transform] that returns the next [RemoteData].
 * Only continues the chain if it is currently a [RemoteData.Success].
 */
suspend inline fun <A : Any, B : Any, E : Any> RemoteData<E, A>.flatMap(
    crossinline transform: suspend (A) -> RemoteData<E, B>
): RemoteData<E, B> = when (this) {
    is RemoteData.NotAsked -> this
    is RemoteData.Loading -> this
    is RemoteData.Success -> transform(data)
    is RemoteData.Failure -> RemoteData.Failure(error)
}
