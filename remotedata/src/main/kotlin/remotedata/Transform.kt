package remotedata

/**
 * Transform the data of a [RemoteData] value with [transform]
 */
inline fun <A, B, E> RemoteData<E, A>.map(transform: (A) -> B): RemoteData<E, B> =
    mapBoth({ it }, transform)

/**
 * Transform the error of a [RemoteData] value with [transform].
 */
inline fun <A, E, F> RemoteData<E, A>.mapError(
    transform: (E) -> F
): RemoteData<F, A> = mapBoth(transform, { it })

/**
 * Apply both [failure] and [success] mapping functions to this [RemoteData].
 * Combines [map] and [mapError] into one.
 */
inline fun <A, B, C, E> RemoteData<E, A>.mapBoth(
    failure: (E) -> C,
    success: (A) -> B
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
infix fun <A, B, F> RemoteData<F, (A) -> B>.andMap(
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
inline fun <A, B, E> RemoteData<E, A>.flatMap(
    transform: (A) -> RemoteData<E, B>
): RemoteData<E, B> = when (this) {
    is RemoteData.NotAsked -> this
    is RemoteData.Loading -> this
    is RemoteData.Success -> transform(data)
    is RemoteData.Failure -> RemoteData.Failure(error)
}
