package com.github.torresmi.remotedata

import io.kotest.matchers.shouldBe
import kotlin.js.JsName
import kotlin.test.Test

class CombineTest {
    val mapper = { a: Int, b: Int -> a + b }

    @Test
    @JsName("merging_NotAsked_keepsState")
    fun `merging NotAsked keeps the state`() {
        with(RemoteData.NotAsked) {
            mergeWith(RemoteData.NotAsked, mapper) shouldBe this
            mergeWith(RemoteData.Loading, mapper) shouldBe this
            mergeWith(RemoteData.Success(0), mapper) shouldBe this
            mergeWith(RemoteData.Failure(0), mapper) shouldBe this
        }
    }

    @Test
    @JsName("merging_Loading_keepsState")
    fun `merging Loading keeps the state`() {
        with(RemoteData.Loading) {
            mergeWith(RemoteData.NotAsked, mapper) shouldBe this
            mergeWith(RemoteData.Loading, mapper) shouldBe this
            mergeWith(RemoteData.Success(0), mapper) shouldBe this
            mergeWith(RemoteData.Failure(0), mapper) shouldBe this
        }
    }

    @Test
    @JsName("merging_Failure_keepsState")
    fun `merging Failure keeps the state`() {
        with(RemoteData.fail(0)) {
            mergeWith(RemoteData.NotAsked, mapper) shouldBe this
            mergeWith(RemoteData.Loading, mapper) shouldBe this
            mergeWith(RemoteData.Success(0), mapper) shouldBe this
            mergeWith(RemoteData.Failure(0), mapper) shouldBe this
        }
    }

    @Test
    @JsName("merging_Success_keepsOtherState")
    fun `merging Success with other states keeps the other state`() {
        with(RemoteData.succeed(0)) {
            mergeWith(RemoteData.NotAsked, mapper) shouldBe RemoteData.NotAsked
            mergeWith(RemoteData.Loading, mapper) shouldBe RemoteData.Loading
            mergeWith(RemoteData.Failure(0), mapper) shouldBe RemoteData.Failure(0)
        }
    }

    @Test
    @JsName("merging_SuccessWithAnotherSuccess_merges")
    fun `merging Success with another Success merges with the mapper`() {
        val other = RemoteData.succeed(1)
        RemoteData.succeed(0).mergeWith(other, mapper) shouldBe RemoteData.succeed(1)
    }
}
