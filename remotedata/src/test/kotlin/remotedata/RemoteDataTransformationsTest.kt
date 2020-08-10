package remotedata

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class RemoteDataTransformationsTest : DescribeSpec({
    
    describe("construction") {

        it("can be created with data using success extension") {
            checkAll(Arb.int()) { data ->
                data.success() shouldBe RemoteData.Success<Nothing, Int>(data)
            }
        }

        it("can be created with data using succeed creator") {
            checkAll(Arb.int()) { data ->
                RemoteData.succeed(data) shouldBe RemoteData.Success<Nothing, Int>(data)
            }
        }

        it("can be created with error using failure extension") {
            checkAll(Arb.int()) { error ->
                error.failure() shouldBe RemoteData.Failure<Int, Nothing>(error)
            }
        }

        it("can be created with error data fail creator") {
            checkAll(Arb.int()) { error ->
                RemoteData.fail(error) shouldBe RemoteData.Failure<Int, Nothing>(error)
            }
        }
    }

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

    describe("mapping success") {

        describe("RemoteData is a success") {

            it("should be a success with the value returned from mapping") {
                checkAll(successGen(), Arb.string()) { sut, newValue ->
                    sut.map { newValue } shouldBe newValue.success()
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
                    sut.mapError { newValue } shouldBe newValue.failure()
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
                    sut.mapBoth({ IllegalAccessError() }, { newValue }) shouldBe newValue.success()
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
                    sut.mapBoth({ newValue }, { IllegalAccessError() }) shouldBe newValue.failure()
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

    describe("andMapping") {
        val mapper: (Int) -> Int = { it + 1 }
        val dataMapper = mapper.success()
        val mappingGenNonSuccess = nonSuccessGen().map { it.map { mapper } }

        describe("both RemoteData objects are successful") {

            it("applies the function from the first object to the second for a result") {
                checkAll(Arb.int()) { a ->
                    val expected = (a + 1).success()
                    dataMapper andMap a.success() shouldBe expected
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

    describe("get values") {

        describe("RemoteData is a success") {

            it("provides the success value") {
                checkAll(Arb.int()) { value ->
                    value.success().get() shouldBe value
                }
            }
        }

        describe("RemoteData is not a success") {

            it("returns null") {
                checkAll(nonSuccessGen()) { sut ->
                    sut.get() shouldBe null
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

private fun successGen() = Arb.int().map { it.success() }

private fun failureGen() = Arb.int().map { it.failure() }

private fun nonSuccessGen() = Arb.remoteDataNonSuccess(Arb.int())
