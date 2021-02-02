package com.github.torresmi.remotedata

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class RemoteDataTest {

    @Test
    fun `succeed helper creator constructs Success case`() {
        val data = 1
        RemoteData.succeed(data) shouldBe RemoteData.Success(data)
    }

    @Test
    fun `fail helper creator constructs Failure case`() {
        val error = 1
        RemoteData.fail(error) shouldBe RemoteData.Failure(error)
    }

    @Test
    fun `isNotAsked for NotAsked returns true`() {
        RemoteData.NotAsked.isNotAsked() shouldBe true
    }

    @Test
    fun `isLoading for NotAsked returns false`() {
        RemoteData.NotAsked.isLoading() shouldBe false
    }

    @Test
    fun `isSuccess for NotAsked returns false`() {
        RemoteData.NotAsked.isSuccess() shouldBe false
    }

    @Test
    fun `isFailure for NotAsked returns false`() {
        RemoteData.NotAsked.isFailure() shouldBe false
    }

    @Test
    fun `isNotAsked for Loading returns false`() {
        RemoteData.Loading.isNotAsked() shouldBe false
    }

    @Test
    fun `isLoading for Loading returns true`() {
        RemoteData.Loading.isLoading() shouldBe true
    }

    @Test
    fun `isSuccess for Loading returns false`() {
        RemoteData.Loading.isSuccess() shouldBe false
    }

    @Test
    fun `isFailure for Loading returns false`() {
        RemoteData.Loading.isFailure() shouldBe false
    }

    @Test
    fun `isNotAsked for Success returns false`() {
        RemoteData.succeed(0).isNotAsked() shouldBe false
    }

    @Test
    fun `isLoading for Success returns false`() {
        RemoteData.succeed(0).isLoading() shouldBe false
    }

    @Test
    fun `isSuccess for Success returns false`() {
        RemoteData.succeed(0).isSuccess() shouldBe true
    }

    @Test
    fun `isFailure for Success returns false`() {
        RemoteData.succeed(0).isFailure() shouldBe false
    }

    @Test
    fun `isNotAsked for Failure returns false`() {
        RemoteData.fail(0).isNotAsked() shouldBe false
    }

    @Test
    fun `isLoading for Failure returns false`() {
        RemoteData.fail(0).isLoading() shouldBe false
    }

    @Test
    fun `isSuccess for Failure returns false`() {
        RemoteData.fail(0).isSuccess() shouldBe false
    }

    @Test
    fun `isFailure for Failure returns true`() {
        RemoteData.fail(0).isFailure() shouldBe true
    }

}
