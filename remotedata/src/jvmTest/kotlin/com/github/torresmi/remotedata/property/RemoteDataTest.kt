package com.github.torresmi.remotedata.property

import com.github.torresmi.remotedata.RemoteData
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll

class RemoteDataTest : DescribeSpec({
    
    describe("construction") {

        it("can be created with data using succeed creator") {
            checkAll(Arb.int()) { data ->
                RemoteData.succeed(data) shouldBe RemoteData.Success(data)
            }
        }

        it("can be created with error data fail creator") {
            checkAll(Arb.int()) { error ->
                RemoteData.fail(error) shouldBe RemoteData.Failure(error)
            }
        }
    }

    describe("current state conditionals") {

        describe("RemoteData is success") {

            it("returns true upon isSuccess") {
                checkAll(successGen()) { sut ->
                    sut.isSuccess() shouldBe true
                }
            }

            it("returns false upon other checks") {
                checkAll(successGen()) { sut ->
                    sut.isLoading() shouldBe false
                    sut.isNotAsked() shouldBe false
                    sut.isFailure() shouldBe false
                }
            }
        }

        describe("RemoteData is failure") {

            it("returns true upon isFailure") {
                checkAll(failureGen()) { sut ->
                    sut.isFailure() shouldBe true
                }
            }

            it("returns false upon other checks") {
                checkAll(failureGen()) { sut ->
                    sut.isLoading() shouldBe false
                    sut.isNotAsked() shouldBe false
                    sut.isSuccess() shouldBe false
                }
            }
        }

        describe("RemoteData is loading") {

            it("returns true upon isLoading") {
                RemoteData.Loading.isLoading() shouldBe true
            }

            it("returns false upon other checks") {
                val sut = RemoteData.Loading
                sut.isNotAsked() shouldBe false
                sut.isSuccess() shouldBe false
                sut.isFailure() shouldBe false
            }
        }

        describe("RemoteData is not asked") {

            it("returns true upon isNotAsked") {
                RemoteData.NotAsked.isNotAsked() shouldBe true
            }

            it("returns false upon other checks") {
                val sut = RemoteData.NotAsked
                sut.isSuccess() shouldBe false
                sut.isLoading() shouldBe false
                sut.isFailure() shouldBe false
            }
        }
    }
})

private fun successGen() = Arb.int().map { RemoteData.succeed(it) }

private fun failureGen() = Arb.int().map { RemoteData.fail(it) }
