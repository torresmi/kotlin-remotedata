package com.github.torresmi.remotedata.property

import com.github.torresmi.remotedata.RemoteData
import com.github.torresmi.remotedata.mergeWith
import com.github.torresmi.remotedata.test.util.generation.remoteDataNonSuccess
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll

class CombineTest : DescribeSpec({

    describe("merging") {
        val mapper = { a: Int, b: Int -> a + b }

        describe("both RemoteData objects are successful") {

            it("applies the map function") {
                checkAll(Arb.int(), Arb.int()) { a, b ->
                    val expected = RemoteData.succeed(mapper(a, b))
                    RemoteData.succeed(a).mergeWith(RemoteData.succeed(b), mapper) shouldBe expected
                }
            }
        }

        describe("both RemoteData objects are not a success") {

            it("keeps the first state") {
                checkAll(nonSuccessGen(), nonSuccessGen()) { a, b ->
                    a.mergeWith(b, mapper) shouldBe a
                }
            }
        }

        describe("first RemoteData object is a success and the second is not a success") {

            it("keeps the second state") {
                checkAll(successGen(), nonSuccessGen()) { a, b ->
                    a.mergeWith(b, mapper) shouldBe b
                }
            }
        }

        describe("first RemoteData object is not a success and the second is a success") {

            it("keeps the first state") {
                checkAll(nonSuccessGen(), successGen()) { a, b ->
                    a.mergeWith(b, mapper) shouldBe a
                }
            }
        }
    }
})

private fun successGen() = Arb.int().map { RemoteData.succeed(it) }

private fun nonSuccessGen() = Arb.remoteDataNonSuccess(Arb.int())
