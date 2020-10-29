package com.github.torresmi.remotedata


/**
 * Combine with [other] using [merge] if both are [RemoteData.Success].
 * Any other state other than [RemoteData.Success] with be returned, with left precedence.
 */
inline fun <A, B, C, E> RemoteData<E, A>.mergeWith(
    other: RemoteData<E, B>,
    crossinline merge: (A, B) -> C
): RemoteData<E, C> = flatMap { a ->
    other.map { b ->
        merge(a, b)
    }
}

/**
 * Combine with 2 other [RemoteData] values with [merge] if all are [RemoteData.Success]
 * Any other state other than [RemoteData.Success] with be returned, with left precedence.
 */
inline fun <A, B, C, D, E> RemoteData<E, A>.mergeWith(
    other1: RemoteData<E, B>,
    other2: RemoteData<E, C>,
    crossinline merge: (A, B, C) -> D
): RemoteData<E, D> = flatMap { a ->
    other1.flatMap { b ->
        other2.map { c ->
            merge(a, b, c)
        }
    }
}

/**
 * Combine with 3 other [RemoteData] values with [merge] if all are [RemoteData.Success].
 * Any other state other than [RemoteData.Success] with be returned, with left precedence.
 */
inline fun <A, B, C, D, E, F> RemoteData<F, A>.mergeWith(
    other1: RemoteData<F, B>,
    other2: RemoteData<F, C>,
    other3: RemoteData<F, D>,
    crossinline merge: (A, B, C, D) -> E
): RemoteData<F, E> = flatMap { a ->
    other1.flatMap { b ->
        other2.flatMap { c ->
            other3.map { d ->
                merge(a, b, c, d)
            }
        }
    }
}
