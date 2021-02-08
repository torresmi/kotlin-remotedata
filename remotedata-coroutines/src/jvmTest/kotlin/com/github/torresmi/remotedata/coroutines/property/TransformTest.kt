package com.github.torresmi.remotedata.coroutines.property

import com.github.torresmi.remotedata.RemoteData
import com.github.torresmi.remotedata.coroutines.*
import com.github.torresmi.remotedata.map
import com.github.torresmi.remotedata.test.util.generation.remoteData
import com.github.torresmi.remotedata.test.util.generation.remoteDataNonSuccess
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class TransformTest : DescribeSpec({

    describe("mapping success") {

        describe("RemoteData is a success") {

            it("should be a success with the value returned from mapping") {
                checkAll(successGen(), Arb.string()) { sut, newValue ->
                    sut.mapAsync { newValue } shouldBe RemoteData.Success(newValue)
                }
            }
        }

        describe("RemoteData is not a success") {
            val mapper: suspend (Int) -> Int = { throw IllegalAccessError("mapper should not be invoked") }

            it("does not invoke the mapper") {
                checkAll(nonSuccessGen()) { sut ->
                    sut.mapAsync(mapper)
                }
            }

            it("does not change the state") {
                checkAll(nonSuccessGen()) { sut ->
                    sut.mapAsync(mapper) shouldBe sut
                }
            }
        }
    }

    describe("mapping error") {
        val mapper: suspend (Int) -> Int = { throw IllegalAccessError("mapper should not be invoked") }

        describe("RemoteData is a success") {

            it("does not invoke the mapper") {
                checkAll(successGen()) { sut ->
                    sut.mapErrorAsync(mapper)
                }
            }

            it("does not change the state") {
                checkAll(successGen()) { sut ->
                    sut.mapErrorAsync(mapper) shouldBe sut
                }
            }
        }

        describe("RemoteData is not a success") {

            it("should be a failure with the value returned from the mapper") {
                checkAll(failureGen(), Arb.string()) { sut, newValue ->
                    sut.mapErrorAsync { newValue } shouldBe RemoteData.Failure(newValue)
                }
            }
        }
    }

    describe("mapping both") {

        describe("RemoteData is a success") {

            it("does not invoke the failure mapper") {
                checkAll(successGen()) { sut ->
                    sut.mapBothAsync({ IllegalAccessError() }, { it })
                }
            }

            it("should map the success") {
                checkAll(successGen(), Arb.string()) { sut, newValue ->
                    sut.mapBothAsync({ IllegalAccessError() }, { newValue }) shouldBe RemoteData.Success(newValue)
                }
            }
        }

        describe("RemoteData is a failure") {

            it("does not invoke the success mapper") {
                checkAll(failureGen()) { sut ->
                    sut.mapBothAsync({ it }, { IllegalAccessError() })
                }
            }

            it("should return a new failure with the mapped result") {
                checkAll(failureGen(), Arb.string()) { sut, newValue ->
                    sut.mapBothAsync({ newValue }, { IllegalAccessError() }) shouldBe RemoteData.Failure(newValue)
                }
            }
        }

        describe("RemoteData is loading") {

            it("does not invoke either of the mappers") {
                RemoteData.Loading.mapBothAsync({ IllegalAccessError() }, { IllegalAccessError() })
            }

            it("should return the same state") {
                val sut = RemoteData.Loading
                sut.mapBothAsync({ IllegalAccessError() }, { IllegalAccessError() }) shouldBe sut
            }
        }

        describe("RemoteData is not asked") {

            it("does not invoke either of the mappers") {
                RemoteData.NotAsked.mapBothAsync({ IllegalAccessError() }, { IllegalAccessError() })
            }

            it("should return the same state") {
                val sut = RemoteData.NotAsked
                sut.mapBothAsync({ IllegalAccessError() }, { IllegalAccessError() }) shouldBe sut
            }
        }
    }

    describe("flatMapping") {
        val nextValueGen = Arb.remoteData(Arb.string(), Arb.string())

        describe("RemoteData is a success") {

            it("returns the RemoteData from the flatMap function") {
                checkAll(successGen(), nextValueGen) { sut, next ->
                    sut.flatMapAsync { next } shouldBe next
                }
            }
        }

        describe("RemoteData is not a success") {
            val flatMapper: suspend (Int) -> RemoteData<String, Int> = {
                throw IllegalAccessError("flatMap function should not be invoked")
            }

            it("does not invoke the flatMap function") {
                checkAll(nonSuccessGen()) { sut ->
                    sut.flatMapAsync(flatMapper)
                }
            }

            it("returns the same state") {
                checkAll(nonSuccessGen()) { sut ->
                    sut.flatMapAsync(flatMapper) shouldBe sut
                }
            }
        }
    }

    describe("andMapping") {
        val mapper: suspend (Int) -> Int = { it + 1 }
        val dataMapper = RemoteData.succeed(mapper)
        val mappingGenNonSuccess = nonSuccessGen().map { it.map { mapper } }

        describe("both RemoteData objects are successful") {

            it("applies the function from the first object to the second for a result") {
                checkAll(Arb.int()) { a ->
                    val expected = RemoteData.succeed(a + 1)
                    dataMapper andMapAsync RemoteData.Success(a) shouldBe expected
                }
            }
        }

        describe("both RemoteData objects are not a success") {

            it("keeps the first state") {
                checkAll(mappingGenNonSuccess, nonSuccessGen()) { a, b ->
                    a andMapAsync b shouldBe a
                }
            }
        }

        describe("first RemoteData object is a success and the second is not a success") {

            it("keeps the second state") {
                checkAll(nonSuccessGen()) { a ->
                    dataMapper andMapAsync a shouldBe a
                }
            }
        }

        describe("first RemoteData object is not a success and the second is a success") {

            it("keeps the first state") {
                checkAll(mappingGenNonSuccess, successGen()) { a, b ->
                    a andMapAsync b shouldBe a
                }
            }
        }
    }
})

private fun successGen() = Arb.int().map { RemoteData.succeed(it) }

private fun failureGen() = Arb.int().map { RemoteData.fail(it) }

private fun nonSuccessGen() = Arb.remoteDataNonSuccess(Arb.int())
