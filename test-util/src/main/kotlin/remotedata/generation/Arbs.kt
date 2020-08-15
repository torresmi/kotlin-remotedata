package remotedata.generation

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arb

fun <A> Arb<A>.plusEdgecases(vararg edgecases: A): Arb<A> = arb(this.edgecases().plus(edgecases)) { rs ->
    generate(rs).map { it.value }
}
