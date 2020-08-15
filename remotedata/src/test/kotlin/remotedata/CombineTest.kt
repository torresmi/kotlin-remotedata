package remotedata

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll
import remotedata.generation.remoteDataNonSuccess

class CombineTest : DescribeSpec({

    describe("merging") {
        val mapper = { a: Int, b: Int -> a + b }

        describe("both RemoteData objects are successful") {

            it("applies the map function") {
                checkAll(Arb.int(), Arb.int()) { a, b ->
                    val expected = mapper(a, b).success()
                    a.success().mergeWith(b.success(), mapper) shouldBe expected
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

    describe("appending") {

        describe("both RemoteData objects are successful") {

            it("create a pair of both success values") {
                checkAll(Arb.int(), Arb.int()) { a, b ->
                    val expected = (a to b).success()
                    a.success().append(b.success()) shouldBe expected
                }
            }
        }

        describe("both RemoteData objects are not a success") {

            it("keeps the first state") {
                checkAll(nonSuccessGen(), nonSuccessGen()) { a, b ->
                    a.append(b) shouldBe a
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

        describe("first RemoteData object is not a success and the second is a success") {

            it("keeps the first state") {
                checkAll(nonSuccessGen(), successGen()) { a, b ->
                    a.append(b) shouldBe a
                }
            }
        }
    }
})

private fun successGen() = Arb.int().map { it.success() }

private fun nonSuccessGen() = Arb.remoteDataNonSuccess(Arb.int())
