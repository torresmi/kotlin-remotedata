package com.github.torresmi.remotedata.property

import com.github.torresmi.remotedata.RemoteData
import com.github.torresmi.remotedata.append
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll
import remotedata.generation.remoteDataNonSuccess

class AppendTest : DescribeSpec({

    describe("appending") {

        describe("all RemoteData objects are successful") {

            it("create a pair of both success values") {
                checkAll(Arb.int(), Arb.int()) { a, b ->
                    val expected = RemoteData.succeed(a to b)
                    RemoteData.succeed(a).append(RemoteData.succeed(b)) shouldBe expected
                }
            }

            it("create a triple of all success values") {
                checkAll(Arb.int(), Arb.int(), Arb.int()) { a, b, c ->
                    val expected = RemoteData.succeed(Triple(a, b, c))
                    RemoteData.succeed(a).append(RemoteData.succeed(b), RemoteData.succeed(c)) shouldBe expected
                }
            }
        }

        describe("all RemoteData objects are not a success") {

            it("keeps the first state") {
                checkAll(nonSuccessGen(), nonSuccessGen(), nonSuccessGen()) { a, b, c ->
                    a.append(b) shouldBe a

                    a.append(b, c) shouldBe a
                }
            }
        }

        describe("first RemoteData object is a success and the second is not a success") {

            it("keeps the second state") {
                checkAll(successGen(), nonSuccessGen()) { a, b ->
                    a.append(b) shouldBe b
                }
            }
        }

        describe("first RemoteData object is a success and the rest are not a success") {

            it("keeps the second state") {
                checkAll(successGen(), nonSuccessGen(), nonSuccessGen()) { a, b, c ->
                    a.append(b, c) shouldBe b
                }
            }
        }

        describe("first two RemoteData objects are a success and the last is not a success") {

            it("keeps the last state") {
                checkAll(successGen(), successGen(), nonSuccessGen()) { a, b, c ->
                    a.append(b, c) shouldBe c
                }
            }
        }

        describe("first RemoteData object is not a success and the second is a success") {

            it("keeps the first state") {
                checkAll(nonSuccessGen(), successGen()) { a, b ->
                    a.append(b) shouldBe a
                }
            }
        }

        describe("first RemoteData object is not a success and the rest are a success") {

            it("keeps the first state") {
                checkAll(nonSuccessGen(), successGen(), successGen()) { a, b, c ->
                    a.append(b, c) shouldBe a
                }
            }
        }
    }
})

private fun successGen() = Arb.int().map { RemoteData.succeed(it) }

private fun nonSuccessGen() = Arb.remoteDataNonSuccess(Arb.int())
