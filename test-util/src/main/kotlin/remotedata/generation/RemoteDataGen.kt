package remotedata.generation

import io.kotest.property.Arb
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import remotedata.RemoteData

fun <E : Any, A : Any> Arb.Companion.remoteData(failureGen: Arb<E>, successGen: Arb<A>) = Arb.choice(
    successGen.map { RemoteData.succeed(it) },
    failureGen.map { RemoteData.fail(it) }
).plusEdgecases(
    RemoteData.NotAsked,
    RemoteData.Loading,
    RemoteData.succeed(successGen.next()),
    RemoteData.fail(failureGen.next())
)

fun <E : Any> Arb.Companion.remoteDataNonSuccess(failureGen: Arb<E>) = failureGen.map { RemoteData.fail(it) }
    .plusEdgecases(
        RemoteData.NotAsked,
        RemoteData.Loading
    )
