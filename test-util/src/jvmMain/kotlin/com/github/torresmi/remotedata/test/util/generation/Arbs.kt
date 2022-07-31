package com.github.torresmi.remotedata.test.util.generation

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.edgecases
import io.kotest.property.arbitrary.next

fun <A> Arb<A>.plusEdgecases(vararg edgecases: A): Arb<A> {
    val allEdgeCases = this.edgecases()
        .plus(edgecases)
        .toList()

    return arbitrary(allEdgeCases) { rs ->
        this@plusEdgecases.next(rs)
    }
}
