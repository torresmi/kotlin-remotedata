package remotedata

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import remotedata.generation.remoteDataNonSuccess

class UnwrapTest : DescribeSpec({

    describe("get values") {

        describe("RemoteData is a success") {

            it("provides the success value") {
                checkAll(Arb.int()) { value ->
                    value.success().getOrNull() shouldBe value
                }
            }
        }

        describe("RemoteData is not a success") {

            it("returns null") {
                checkAll(nonSuccessGen()) { sut ->
                    sut.getOrNull() shouldBe null
                }
            }
        }
    }

    describe("get values or default") {

        describe("RemoteData is a success") {

            it("provides the success value") {
                checkAll(Arb.int(), Arb.int()) { value, default ->
                    val data = value.success()

                    data.getOrElse(default) shouldBe value
                    data.getOrElse { default } shouldBe value
                }
            }

            it("does not invoke else function") {
                checkAll(Arb.int()) { value ->
                    value.success().getOrElse { throw IllegalAccessError() }
                }
            }
        }

        describe("RemoteData is not a success") {

            it("returns default") {
                checkAll(nonSuccessGen(), Arb.int()) { sut, default ->
                    sut.getOrElse(default) shouldBe default
                    sut.getOrElse { default } shouldBe default
                }
            }
        }
    }
})

private fun nonSuccessGen() = Arb.remoteDataNonSuccess(Arb.int())
