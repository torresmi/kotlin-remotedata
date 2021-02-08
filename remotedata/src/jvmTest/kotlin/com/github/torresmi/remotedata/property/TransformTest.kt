package com.github.torresmi.remotedata.property

import com.github.torresmi.remotedata.*
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import com.github.torresmi.remotedata.generation.remoteData
import com.github.torresmi.remotedata.generation.remoteDataNonSuccess

class TransformationsTest : DescribeSpec({

    describe("mapping") {

        describe("RemoteData is a success") {

            it("should be a success with the value returned from mapping") {
                checkAll(successGen(), Arb.string()) { sut, newValue ->
                    sut.map { newValue } shouldBe RemoteData.succeed(newValue)
                }
            }
        }

        describe("RemoteData is not a success") {
            val mapper: (Int) -> Int = { throw IllegalAccessError("mapper should not be invoked") }

            it("does not invoke the mapper") {
                checkAll(nonSuccessGen()) { sut ->
                    sut.map(mapper)
                }
            }

            it("does not change the state") {
                checkAll(nonSuccessGen()) { sut ->
                    sut.map(mapper) shouldBe sut
                }
            }
        }
    }

    describe("mapping error") {
        val mapper: (Int) -> Int = { throw IllegalAccessError("mapper should not be invoked") }

        describe("RemoteData is a success") {

            it("does not invoke the mapper") {
                checkAll(successGen()) { sut ->
                    sut.mapError(mapper)
                }
            }

            it("does not change the state") {
                checkAll(successGen()) { sut ->
                    sut.mapError(mapper) shouldBe sut
                }
            }
        }

        describe("RemoteData is not a success") {

            it("should be a failure with the value returned from the mapper") {
                checkAll(failureGen(), Arb.string()) { sut, newValue ->
                    sut.mapError { newValue } shouldBe RemoteData.Failure(newValue)
                }
            }
        }
    }

    describe("mapping both") {

        describe("RemoteData is a success") {

            it("does not invoke the failure mapper") {
                checkAll(successGen()) { sut ->
                    sut.mapBoth({ IllegalAccessError() }, { it })
                }
            }

            it("should map the success") {
                checkAll(successGen(), Arb.string()) { sut, newValue ->
                    sut.mapBoth({ IllegalAccessError() }, { newValue }) shouldBe RemoteData.Success(newValue)
                }
            }
        }

        describe("RemoteData is a failure") {

            it("does not invoke the success mapper") {
                checkAll(failureGen()) { sut ->
                    sut.mapBoth({ it }, { IllegalAccessError() })
                }
            }

            it("should return a new failure with the mapped result") {
                checkAll(failureGen(), Arb.string()) { sut, newValue ->
                    sut.mapBoth({ newValue }, { IllegalAccessError() }) shouldBe RemoteData.Failure(newValue)
                }
            }
        }

        describe("RemoteData is loading") {

            it("does not invoke either of the mappers") {
                RemoteData.Loading.mapBoth({ IllegalAccessError() }, { IllegalAccessError() })
            }

            it("should return the same state") {
                val sut = RemoteData.Loading
                sut.mapBoth({ IllegalAccessError() }, { IllegalAccessError() }) shouldBe sut
            }
        }

        describe("RemoteData is not asked") {

            it("does not invoke either of the mappers") {
                RemoteData.NotAsked.mapBoth({ IllegalAccessError() }, { IllegalAccessError() })
            }

            it("should return the same state") {
                val sut = RemoteData.NotAsked
                sut.mapBoth({ IllegalAccessError() }, { IllegalAccessError() }) shouldBe sut
            }
        }
    }

    describe("flatMapping") {
        val nextValueGen = Arb.remoteData(Arb.string(), Arb.string())

        describe("RemoteData is a success") {

            it("returns the RemoteData from the flatMap function") {
                checkAll(successGen(), nextValueGen) { sut, next ->
                    sut.flatMap { next } shouldBe next
                }
            }
        }

        describe("RemoteData is not a success") {
            val flatMapper: (Int) -> RemoteData<String, Int> = {
                throw IllegalAccessError("flatMap function should not be invoked")
            }

            it("does not invoke the flatMap function") {
                checkAll(nonSuccessGen()) { sut ->
                    sut.flatMap(flatMapper)
                }
            }

            it("returns the same state") {
                checkAll(nonSuccessGen()) { sut ->
                    sut.flatMap(flatMapper) shouldBe sut
                }
            }
        }
    }

    describe("andMapping") {
        val mapper: (Int) -> Int = { it + 1 }
        val dataMapper = RemoteData.succeed(mapper)
        val mappingGenNonSuccess = nonSuccessGen().map { it.map { mapper } }

        describe("both RemoteData objects are successful") {

            it("applies the function from the first object to the second for a result") {
                checkAll(Arb.int()) { a ->
                    val expected = RemoteData.succeed(a + 1)
                    dataMapper andMap RemoteData.Success(a) shouldBe expected
                }
            }
        }

        describe("both RemoteData objects are not a success") {

            it("keeps the first state") {
                checkAll(mappingGenNonSuccess, nonSuccessGen()) { a, b ->
                    a andMap b shouldBe a
                }
            }
        }

        describe("first RemoteData object is a success and the second is not a success") {

            it("keeps the second state") {
                checkAll(nonSuccessGen()) { a ->
                    dataMapper andMap a shouldBe a
                }
            }
        }

        describe("first RemoteData object is not a success and the second is a success") {

            it("keeps the first state") {
                checkAll(mappingGenNonSuccess, successGen()) { a, b ->
                    a andMap b shouldBe a
                }
            }
        }
    }
})

private fun successGen() = Arb.int().map { RemoteData.succeed(it) }

private fun failureGen() = Arb.int().map { RemoteData.fail(it) }

private fun nonSuccessGen() = Arb.remoteDataNonSuccess(Arb.int())
