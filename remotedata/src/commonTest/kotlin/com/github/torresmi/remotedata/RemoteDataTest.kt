package com.github.torresmi.remotedata

import io.kotest.matchers.shouldBe
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object RemoteDataTest : Spek({

    describe("construction") {
        val data = 1
        val error = 0

        it("can be created with data using success extension") {
            data.success() shouldBe RemoteData.Success(data)
        }

        it("can be created with data using succeed creator") {
            RemoteData.succeed(data) shouldBe RemoteData.Success(data)
        }

        it("can be created with error using failure extension") {
            error.failure() shouldBe RemoteData.Failure(error)
        }

        it("can be created with error data fail creator") {
            RemoteData.fail(error) shouldBe RemoteData.Failure(error)
        }
    }

    describe("current state conditionals") {

        context("is NotAsked") {
            val sut = RemoteData.NotAsked

            it("returns true") {
                sut.isNotAsked() shouldBe true
            }

            it("returns false") {
                sut.isLoading() shouldBe false
                sut.isSuccess() shouldBe false
                sut.isFailure() shouldBe false
            }
        }

        context("is Loading") {
            val sut = RemoteData.Loading

            it("returns true") {
                sut.isLoading() shouldBe true
            }

            it("returns false") {
                sut.isNotAsked() shouldBe false
                sut.isSuccess() shouldBe false
                sut.isFailure() shouldBe false
            }
        }

        context("is Success") {
            val sut = RemoteData.Success<Int, Int>(0)

            it("returns true") {
                sut.isSuccess() shouldBe true
            }

            it("returns false") {
                sut.isNotAsked() shouldBe false
                sut.isLoading() shouldBe false
                sut.isFailure() shouldBe false
            }
        }

        context("is Failure") {
            val sut = RemoteData.Failure<Int, Int>(0)

            it("returns true") {
                sut.isFailure() shouldBe true
            }

            it("returns false") {
                sut.isNotAsked() shouldBe false
                sut.isLoading() shouldBe false
                sut.isSuccess() shouldBe false
            }
        }
    }
})
