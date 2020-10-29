package com.github.torresmi.remotedata

import io.kotest.matchers.shouldBe
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object TransformTest : Spek({

    describe("mapping") {
        val mapper = { a: Int -> a + 1 }

        context("is NotAsked") {
            val sut = RemoteData.NotAsked

            it("keeps the NotAsked state") {
                sut.map(mapper) shouldBe sut
            }
        }

        context("is Loading") {
            val sut = RemoteData.Loading

            it("keeps the Loading state") {
                sut.map(mapper) shouldBe sut
            }
        }

        context("is Success") {
            val sut = RemoteData.Success<Int, Int>(0)

            it("invokes the mapper for a result") {
                sut.map(mapper) shouldBe RemoteData.Success(1)
            }
        }

        context("is Failure") {
            val sut = RemoteData.Failure<Int, Int>(0)

            it("keeps the Failure state") {
                sut.map(mapper) shouldBe sut
            }
        }
    }

    describe("mapping error") {
        val mapper = { a: Int -> a + 1 }

        context("is NotAsked") {
            val sut = RemoteData.NotAsked

            it("keeps the NotAsked state") {
                sut.mapError(mapper) shouldBe sut
            }
        }

        context("is Loading") {
            val sut = RemoteData.Loading

            it("keeps the Loading state") {
                sut.mapError(mapper) shouldBe sut
            }
        }

        context("is Success") {
            val sut = RemoteData.Success<Int, Int>(0)

            it("keeps the Success state") {
                sut.mapError(mapper) shouldBe sut
            }
        }

        context("is Failure") {
            val sut = RemoteData.Failure<Int, Int>(0)

            it("invokes the mapper for a result") {
                sut.mapError(mapper) shouldBe RemoteData.Failure(1)
            }
        }
    }

    describe("mapping both") {
        val mapper = { a: Int -> a + 1 }

        context("is NotAsked") {
            val sut = RemoteData.NotAsked

            it("keeps the NotAsked state") {
                sut.mapBoth(mapper, mapper) shouldBe sut
            }
        }

        context("is Loading") {
            val sut = RemoteData.Loading

            it("keeps the Loading state") {
                sut.mapBoth(mapper, mapper) shouldBe sut
            }
        }

        context("is Success") {
            val sut = RemoteData.Success<Int, Int>(0)

            it("invokes the success mapper for a result") {
                sut.mapBoth(mapper, mapper) shouldBe RemoteData.Success(1)
            }
        }

        context("is Failure") {
            val sut = RemoteData.Failure<Int, Int>(0)

            it("invokes the failure mapper for a result") {
                sut.mapBoth(mapper, mapper) shouldBe RemoteData.Failure(1)
            }
        }
    }

    describe("flatMapping") {
        val newState = RemoteData.Success<Int, Int>(1)
        val mapper = { a: Int -> newState }

        context("is NotAsked") {
            val sut = RemoteData.NotAsked

            it("keeps the NotAsked state") {
                sut.flatMap(mapper) shouldBe sut
            }
        }

        context("is Loading") {
            val sut = RemoteData.Loading

            it("keeps the Loading state") {
                sut.flatMap(mapper) shouldBe sut
            }
        }

        context("is Success") {
            val sut = RemoteData.Success<Int, Int>(0)

            it("invokes the success mapper for a result") {
                sut.flatMap(mapper) shouldBe RemoteData.Success(1)
            }
        }

        context("is Failure") {
            val sut = RemoteData.Failure<Int, Int>(0)

            it("keeps the Failure state") {
                sut.flatMap(mapper) shouldBe sut
            }
        }
    }

    describe("andMapping") {
        val mapper = { a: Int -> a + 1 }
        val dataMapper = RemoteData.Success<Int, DataMapper>(mapper)

        context("is NotAsked") {
            val sut = RemoteData.NotAsked

            it("keeps the NotAsked state") {
                dataMapper andMap sut shouldBe sut
            }
        }

        context("is Loading") {
            val sut = RemoteData.Loading

            it("keeps the Loading state") {
                dataMapper andMap sut shouldBe sut
            }
        }

        context("is Success") {
            val sut = RemoteData.Success<Int, Int>(0)

            it("invokes the success mapper for a result") {
                dataMapper andMap sut shouldBe RemoteData.Success(1)
            }
        }

        context("is Failure") {
            val sut = RemoteData.Failure<Int, Int>(0)

            it("invokes the failure mapper for a result") {
                dataMapper andMap sut shouldBe sut
            }
        }
    }
})

private typealias DataMapper = (Int) -> Int
