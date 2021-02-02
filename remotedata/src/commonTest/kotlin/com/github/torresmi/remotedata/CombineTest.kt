package com.github.torresmi.remotedata

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class CombineTest {
    val mapper = { a: Int, b: Int -> a + b }

    @Test
    fun `merging NotAsked keeps the state`() {
        with(RemoteData.NotAsked) {
            mergeWith(RemoteData.NotAsked, mapper) shouldBe this
            mergeWith(RemoteData.Loading, mapper) shouldBe this
            mergeWith(RemoteData.Success(0), mapper) shouldBe this
            mergeWith(RemoteData.Failure(0), mapper) shouldBe this
        }
    }

    @Test
    fun `merging Loading keeps the state`() {
        with(RemoteData.Loading) {
            mergeWith(RemoteData.NotAsked, mapper) shouldBe this
            mergeWith(RemoteData.Loading, mapper) shouldBe this
            mergeWith(RemoteData.Success(0), mapper) shouldBe this
            mergeWith(RemoteData.Failure(0), mapper) shouldBe this
        }
    }

    @Test
    fun `merging Failure keeps the state`() {
        with(RemoteData.fail(0)) {
            mergeWith(RemoteData.NotAsked, mapper) shouldBe this
            mergeWith(RemoteData.Loading, mapper) shouldBe this
            mergeWith(RemoteData.Success(0), mapper) shouldBe this
            mergeWith(RemoteData.Failure(0), mapper) shouldBe this
        }
    }

    @Test
    fun `merging Success with other states keeps the other state`() {
        with(RemoteData.succeed(0)) {
            mergeWith(RemoteData.NotAsked, mapper) shouldBe RemoteData.NotAsked
            mergeWith(RemoteData.Loading, mapper) shouldBe RemoteData.Loading
            mergeWith(RemoteData.Failure(0), mapper) shouldBe RemoteData.Failure(0)
        }
    }

    @Test
    fun `merging Success with another Success merges with the mapper`() {
        val other = RemoteData.succeed(1)
        RemoteData.succeed(0).mergeWith(other, mapper) shouldBe RemoteData.succeed(1)
    }
}
