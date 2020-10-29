package com.github.torresmi.remotedata

/**
 * Combines all the [RemoteData] data values into a [Pair] if both are successful.
 * Any state other than [RemoteData.Success] with be returned, with left precedence.
 */
fun <A, B, E> RemoteData<E, A>.append(
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
fun <A, B, C, E> RemoteData<E, A>.append(
    other1: RemoteData<E, B>,
    other2: RemoteData<E, C>
): RemoteData<E, Triple<A, B, C>> =
    append(other1)
        .flatMap { (a, b) ->
            other2.map { c ->
                Triple(a, b, c)
            }
        }
