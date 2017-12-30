package remotedata

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it

class RemoteDataTest : Spek({

    describe("RemoteData") {

        describe("construction") {

            it("can be created with success data using extension") {
                val data = "test"
                val sut = data.success()
                assertThat(sut).isEqualTo(RemoteData.Success<Nothing, String>(data))
            }

            it("can be created with success data using companion") {
                val data = "test"
                val sut = RemoteData.succeed(data)
                assertThat(sut).isEqualTo(RemoteData.Success<Nothing, String>(data))
            }

            it("can be created with failure data using extension") {
                val error = RuntimeException()
                val sut = error.failure()
                assertThat(sut).isEqualTo(RemoteData.Failure<Throwable, Nothing>(error))
            }

            it("can be created with failure data using companion") {
                val error = RuntimeException()
                val sut = RemoteData.fail(error)
                assertThat(sut).isEqualTo(RemoteData.Failure<Throwable, Nothing>(error))
            }
        }

        describe("merging") {
            val mapper = { a: Int, b: Int -> a + b }

            given("Both RemoteData objects are a success") {

                it("applies the map function") {
                    val a = RemoteData.succeed(1)
                    val b = RemoteData.succeed(2)
                    val expected = RemoteData.succeed(3)

                    assertThat(a.mergeWith(b, mapper)).isEqualTo(expected)
                }
            }

            given("Both RemoteData are not a success") {

                it("keep the first failure") {
                    val failure = RuntimeException()
                    val a = RemoteData.fail(failure)
                    val b = RemoteData.succeed(1)

                    assertThat(a.mergeWith(b, mapper)).isEqualTo(a)
                }
            }

            given("First RemoteData objects is a success") {

                it("keep the first failure") {
                    val failure = RuntimeException()
                    val a = RemoteData.fail(failure)
                    val b = RemoteData.succeed(2)

                    assertThat(a.mergeWith(b, mapper)).isEqualTo(a)
                }
            }

            given("Second RemoteData is not a success") {

                it("keep the first failure") {
                    val failure = RuntimeException()
                    val a = RemoteData.fail(failure)
                    val b = RemoteData.succeed(1)

                    assertThat(b.mergeWith(a, mapper)).isEqualTo(a)
                }
            }
        }

        describe("mapping") {

            given("RemoteData is a success") {

                it("should only map a successful value") {
                    val sut = RemoteData.succeed(1)
                    val expected = 2
                    val result = sut.map { expected }

                    assertThat(result.isSuccess()).isTrue()
                    assertThat(result.get()).isEqualTo(expected)
                }
            }

            given("RemoteData is not a success") {

                it("should not change the data if it failed") {
                    val error = RuntimeException("test")
                    val sut = RemoteData.fail(error)
                    val result = sut.map { 2 }

                    assertThat(result.isFailure()).isTrue()
                    assertThat((result as RemoteData.Failure).error).isEqualTo(error)
                }

                it("should not change the data if it is loading") {
                    val sut = RemoteData.Loading
                    val result = sut.map { 2 }

                    assertThat(result.isLoading()).isTrue()
                }

                it("should not change the data if it was not asked") {
                    val sut = RemoteData.NotAsked
                    val result = sut.map { 2 }

                    assertThat(result.isNotAsked()).isTrue()
                }
            }
        }

        describe("mapping error") {

            given("RemoteData is a success") {

                it("should only map if there is an error") {
                    val sut = RemoteData.succeed(1)
                    val result = sut.mapError { RuntimeException() }

                    assertThat(result.isSuccess()).isTrue()
                    assertThat(result.get()).isEqualTo(1)
                }
            }

            given("RemoteData is not a success") {

                it("should only map the error") {
                    val sut = RemoteData.fail(RuntimeException("one"))
                    val expected = RuntimeException("two")
                    val result = sut.mapError { expected }

                    assertThat(result.isFailure()).isTrue()
                    assertThat((result as RemoteData.Failure).error).isEqualTo(expected)
                }
            }
        }

        describe("mapping both") {

            given("RemoteData is a success") {

                it("should map the success") {
                    val sut = RemoteData.succeed(1)
                    val expected = 2
                    val result = sut.mapBoth({ RuntimeException() }, { expected })

                    assertThat(result.isSuccess()).isTrue()
                    assertThat((result as RemoteData.Success).data).isEqualTo(expected)
                }
            }

            given("RemoteData is not a success") {

                it("should map the failure") {
                    val sut = RemoteData.fail(RuntimeException("one"))
                    val expected = RuntimeException("two")
                    val result = sut.mapBoth({ expected }, { 1 })

                    assertThat(result.isFailure()).isTrue()
                    assertThat((result as RemoteData.Failure).error).isEqualTo(expected)
                }

                it("should not change loading state") {
                    val sut = RemoteData.Loading
                    val result = sut.mapBoth({ 1 }, { 2 })

                    assertThat(result.isLoading()).isTrue()
                }

                it("should not change initial state") {
                    val sut = RemoteData.NotAsked
                    val result = sut.mapBoth({ 1 }, { 2 })

                    assertThat(result.isNotAsked()).isTrue()
                }
            }
        }

        describe("flatMapping") {

            given("RemoteData is a success") {

                it("returns a new RemoteData") {
                    val sut = RemoteData.succeed(1)
                    val expected = RemoteData.succeed(2)
                    val result = sut.flatMap { expected }

                    assertThat(result).isEqualTo(expected)
                }
            }

            given("RemoteData is not a success") {

                it("returns the failure") {
                    val sut = RemoteData.fail(RuntimeException())
                    val result = sut.flatMap { RemoteData.succeed(2) }

                    assertThat(result).isEqualTo(sut)
                }

                it("returns loading") {
                    val sut = RemoteData.Loading
                    val result = sut.flatMap { RemoteData.succeed(2) }

                    assertThat(result).isEqualTo(sut)
                }

                it("returns not asked") {
                    val sut = RemoteData.NotAsked
                    val result = sut.flatMap { RemoteData.succeed(2) }

                    assertThat(result).isEqualTo(sut)
                }
            }
        }

        describe("appending") {

            given("RemoteData is a success") {

                it("create a pair of both success values") {
                    val a = RemoteData.succeed(1)
                    val b = RemoteData.succeed(2)
                    val expected = RemoteData.succeed(Pair(1, 2))

                    assertThat(a.append(b)).isEqualTo(expected)
                }
            }

            given("RemoteData is not a success") {

                it("keep the first failure") {
                    val failure = RuntimeException()
                    val a = RemoteData.fail(failure)
                    val b = RemoteData.succeed(1)

                    assertThat(a.append(b)).isEqualTo(a)
                }
            }
        }

        describe("andMapping") {

            given("RemoteData is a success") {

                it("it should apply the function to map the next item") {
                    val a = RemoteData.succeed({ a: Int -> a + 1 })
                    val b = RemoteData.succeed(2)
                    val expected = RemoteData.succeed(2 + 1)

                    assertThat(a andMap b).isEqualTo(expected)
                }
            }

            given("RemoteData is not a success") {

                it("it should be the first failure") {
                    val a = RemoteData.succeed({ a: Int -> a + 1 })
                    val b = RemoteData.fail(RuntimeException())

                    assertThat(a andMap b).isEqualTo(b)
                }
            }
        }

        describe("retrieving values") {

            given("RemoteData is a success") {

                val success = 1
                val sut = RemoteData.succeed(success)

                it("provides the success value") {
                    assertThat(sut.get()).isEqualTo(success)
                }

                it("provides the success value instead of the default") {
                    assertThat(sut.getOrElse(2)).isEqualTo(success)
                }
            }

            given("RemoteData is not a success") {

                val default = 2

                it("returns null on get") {
                    val sut = RemoteData.fail(RuntimeException())
                    assert(sut.get() == null)
                }

                it("provides the default value if it is a failure") {
                    val sut = RemoteData.fail(RuntimeException())
                    assertThat(sut.getOrElse(default)).isEqualTo(default)
                }

                it("provides the default value if it is loading") {
                    val sut = RemoteData.Loading
                    assertThat(sut.getOrElse(default)).isEqualTo(default)
                }

                it("provides the default value if it is not asked") {
                    val sut = RemoteData.NotAsked
                    assertThat(sut.getOrElse(default)).isEqualTo(default)
                }
            }
        }
    }
})
