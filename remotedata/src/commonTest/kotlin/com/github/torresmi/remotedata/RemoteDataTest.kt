package com.github.torresmi.remotedata

import io.kotest.matchers.shouldBe
import kotlin.js.JsName
import kotlin.test.Test

class RemoteDataTest {

    @Test
    @JsName("succeed_createsSuccess")
    fun `succeed helper creator constructs Success case`() {
        val data = 1
        RemoteData.succeed(data) shouldBe RemoteData.Success(data)
    }

    @Test
    @JsName("fail_createsFailure")
    fun `fail helper creator constructs Failure case`() {
        val error = 1
        RemoteData.fail(error) shouldBe RemoteData.Failure(error)
    }

    @Test
    @JsName("isNotAsked_NotAsked_isTrue")
    fun `isNotAsked for NotAsked returns true`() {
        RemoteData.NotAsked.isNotAsked() shouldBe true
    }

    @Test
    @JsName("isLoading_NotAsked_isFalse")
    fun `isLoading for NotAsked returns false`() {
        RemoteData.NotAsked.isLoading() shouldBe false
    }

    @Test
    @JsName("isSuccess_NotAsked_isFalse")
    fun `isSuccess for NotAsked returns false`() {
        RemoteData.NotAsked.isSuccess() shouldBe false
    }

    @Test
    @JsName("isFailure_NotAsked_isFalse")
    fun `isFailure for NotAsked returns false`() {
        RemoteData.NotAsked.isFailure() shouldBe false
    }

    @Test
    @JsName("isNotAsked_Loading_isFalse")
    fun `isNotAsked for Loading returns false`() {
        RemoteData.Loading.isNotAsked() shouldBe false
    }

    @Test
    @JsName("isLoading_Loading_isTrue")
    fun `isLoading for Loading returns true`() {
        RemoteData.Loading.isLoading() shouldBe true
    }

    @Test
    @JsName("isSuccess_Loading_isFalse")
    fun `isSuccess for Loading returns false`() {
        RemoteData.Loading.isSuccess() shouldBe false
    }

    @Test
    @JsName("isFailure_Loading_isFalse")
    fun `isFailure for Loading returns false`() {
        RemoteData.Loading.isFailure() shouldBe false
    }

    @Test
    @JsName("isNotAsked_Success_isFalse")
    fun `isNotAsked for Success returns false`() {
        RemoteData.succeed(0).isNotAsked() shouldBe false
    }

    @Test
    @JsName("isLoading_Success_isFalse")
    fun `isLoading for Success returns false`() {
        RemoteData.succeed(0).isLoading() shouldBe false
    }

    @Test
    @JsName("isSuccess_Success_isTrue")
    fun `isSuccess for Success returns true`() {
        RemoteData.succeed(0).isSuccess() shouldBe true
    }

    @Test
    @JsName("isFailure_Success_isFalse")
    fun `isFailure for Success returns false`() {
        RemoteData.succeed(0).isFailure() shouldBe false
    }

    @Test
    @JsName("isNotAsked_Failure_isFalse")
    fun `isNotAsked for Failure returns false`() {
        RemoteData.fail(0).isNotAsked() shouldBe false
    }

    @Test
    @JsName("isLoading_Failure_isFalse")
    fun `isLoading for Failure returns false`() {
        RemoteData.fail(0).isLoading() shouldBe false
    }

    @Test
    @JsName("isSuccess_Failure_isFalse")
    fun `isSuccess for Failure returns false`() {
        RemoteData.fail(0).isSuccess() shouldBe false
    }

    @Test
    @JsName("isFailure_Failure_isTrue")
    fun `isFailure for Failure returns true`() {
        RemoteData.fail(0).isFailure() shouldBe true
    }
}
