package remotedata

/**
 * Combines all the [RemoteData] data values into a [Pair] if both are successful.
 * Any state other than [RemoteData.Success] with be returned, with left precedence.
 */
fun <A : Any, B : Any, E : Any> RemoteData<E, A>.append(
    other: RemoteData<E, B>
): RemoteData<E, Pair<A, B>> = flatMap { a ->
    other.map { b ->
        a to b
    }
}

/**
 * Combines all the [RemoteData] data values into a [Triple] if all are successful.
 * Any state other than [RemoteData.Success] with be returned, with left precedence.
 */
fun <A : Any, B : Any, C : Any, E : Any> RemoteData<E, A>.append(
    other1: RemoteData<E, B>,
    other2: RemoteData<E, C>
): RemoteData<E, Triple<A, B, C>> =
    append(other1)
        .flatMap { (a, b) ->
            other2.map { c ->
                Triple(a, b, c)
            }
        }

/**
 * Combine with [other] using [merge] if both are [RemoteData.Success].
 * Any other state other than [RemoteData.Success] with be returned, with left precedence.
 */
inline fun <A : Any, B : Any, C : Any, E : Any> RemoteData<E, A>.mergeWith(
    other: RemoteData<E, B>,
    crossinline merge: (A, B) -> C
): RemoteData<E, C> =
    append(other)
        .map { (a, b) ->
            merge(a, b)
        }

/**
 * Combine with 2 other [RemoteData] values with [merge] if all are [RemoteData.Success]
 * Any other state other than [RemoteData.Success] with be returned, with left precedence.
 */
inline fun <A : Any, B : Any, C : Any, D : Any, E : Any> RemoteData<E, A>.mergeWith(
    other1: RemoteData<E, B>,
    other2: RemoteData<E, C>,
    crossinline merge: (A, B, C) -> D
): RemoteData<E, D> =
    append(other1, other2)
        .map { (a, b, c) ->
            merge(a, b, c)
        }

/**
 * Combine with 3 other [RemoteData] values with [merge] if all are [RemoteData.Success].
 * Any other state other than [RemoteData.Success] with be returned, with left precedence.
 */
inline fun <A : Any, B : Any, C : Any, D : Any, E : Any, F : Any> RemoteData<F, A>.mergeWith(
    other1: RemoteData<F, B>,
    other2: RemoteData<F, C>,
    other3: RemoteData<F, D>,
    crossinline merge: (A, B, C, D) -> E
): RemoteData<F, E> =
    append(other1, other2)
        .flatMap { (a, b, c) ->
            other3.map { d ->
                merge(a, b, c, d)
            }
        }
