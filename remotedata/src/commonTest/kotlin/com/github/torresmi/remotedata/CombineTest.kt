package com.github.torresmi.remotedata

import io.kotest.matchers.shouldBe
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object CombineTest : Spek({

    describe("merging") {
        val mapper = { a: Int, b: Int -> a + b }

        context("is NotAsked") {
            val sut = RemoteData.NotAsked

            it("keeps the NotAsked state") {
                sut.mergeWith(RemoteData.NotAsked, mapper) shouldBe sut
                sut.mergeWith(RemoteData.Loading, mapper) shouldBe sut
                sut.mergeWith(RemoteData.Success(0), mapper) shouldBe sut
                sut.mergeWith(RemoteData.Failure(0), mapper) shouldBe sut
            }
        }

        context("is Loading") {
            val sut = RemoteData.Loading

            it("keeps the Loading state") {
                sut.mergeWith(RemoteData.NotAsked, mapper) shouldBe sut
                sut.mergeWith(RemoteData.Loading, mapper) shouldBe sut
                sut.mergeWith(RemoteData.Success(0), mapper) shouldBe sut
                sut.mergeWith(RemoteData.Failure(0), mapper) shouldBe sut
            }
        }

        context("is Success") {
            val sut = RemoteData.Success<Int, Int>(0)

            context("other is not Success") {

                it("keeps the other state") {
                    sut.mergeWith(RemoteData.NotAsked, mapper) shouldBe RemoteData.NotAsked
                    sut.mergeWith(RemoteData.Loading, mapper) shouldBe RemoteData.Loading
                    sut.mergeWith(RemoteData.Failure(1), mapper) shouldBe RemoteData.Failure(1)
                }
            }

            context("both Success") {

                it("merges both values with the mapper") {
                    sut.mergeWith(RemoteData.Success(1), mapper) shouldBe RemoteData.Success(1)
                }
            }
        }

        context("is Failure") {
            val sut = RemoteData.Failure<Int, Int>(0)

            it("keeps the Failure state") {
                sut.mergeWith(RemoteData.NotAsked, mapper) shouldBe sut
                sut.mergeWith(RemoteData.Loading, mapper) shouldBe sut
                sut.mergeWith(RemoteData.Success(0), mapper) shouldBe sut
                sut.mergeWith(RemoteData.Failure(0), mapper) shouldBe sut
            }
        }
    }
})
