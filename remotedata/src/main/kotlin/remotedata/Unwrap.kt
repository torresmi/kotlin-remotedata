package remotedata

/**
 * Attempt to get the value out of the [RemoteData] if it was successful.
 *
 * This should be used with caution, as [getOrElse] or unwrapping with a 'when' statement help
 * handle all other cases better than a nullable type.
 */
fun <A : Any, E : Any> RemoteData<E, A>.getOrNull(): A? = when (this) {
    is RemoteData.Success -> data
    else -> null
}

/**
 * Return either the [RemoteData.Success] data or a provided [default].
 */
fun <A : Any, E : Any> RemoteData<E, A>.getOrElse(default: A): A = getOrNull() ?: default

/**
 * Return either the [RemoteData.Success] data or a provided [default].
 */
inline fun <A : Any, E : Any> RemoteData<E, A>.getOrElse(default: () -> A): A = when (this) {
    is RemoteData.Success -> data
    else -> default()
}
