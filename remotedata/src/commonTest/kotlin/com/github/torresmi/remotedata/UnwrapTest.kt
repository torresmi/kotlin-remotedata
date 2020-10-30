package com.github.torresmi.remotedata

import io.kotest.assertions.fail
import io.kotest.matchers.shouldBe
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object UnwrapTest : Spek({

    describe("get values") {

        context("is NotAsked") {
            val sut = RemoteData.NotAsked

            it("returns null") {
                sut.getOrNull() shouldBe null
            }
        }

        context("is Loading") {
            val sut = RemoteData.Loading

            it("returns null") {
                sut.getOrNull() shouldBe null
            }
        }

        context("is Failure") {
            val sut = RemoteData.Failure<Int, Int>(0)

            it("returns null") {
                sut.getOrNull() shouldBe null
            }
        }

        context("RemoteData is a success") {
            val value = 0
            val sut = RemoteData.Success<Int, Int>(value)

            it("provides the success value") {
                sut.getOrNull() shouldBe value
            }
        }
    }

    describe("get values or default") {
        val default = 0

        context("is NotAsked") {
            val sut = RemoteData.NotAsked

            it("returns null") {
                sut.getOrElse(default) shouldBe default
                sut.getOrElse { default } shouldBe default
            }
        }

        context("is Loading") {
            val sut = RemoteData.Loading

            it("returns null") {
                sut.getOrElse(default) shouldBe default
                sut.getOrElse { default } shouldBe default
            }
        }

        context("is Failure") {
            val sut = RemoteData.Failure<Int, Int>(0)

            it("returns null") {
                sut.getOrElse(default) shouldBe default
                sut.getOrElse { default } shouldBe default
            }
        }

        context("RemoteData is a success") {
            val value = 1
            val sut = RemoteData.Success<Int, Int>(value)

            it("provides the success value") {
                sut.getOrElse(default) shouldBe value
                sut.getOrElse { default } shouldBe value
            }

            it("does not invoke else function") {

                sut.getOrElse { fail("should not be called") } shouldBe value
            }
        }
    }
})
