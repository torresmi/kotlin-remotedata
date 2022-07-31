package com.github.torresmi.remotedata

import io.kotest.matchers.shouldBe
import kotlin.js.JsName
import kotlin.test.Test

class TransformTest {

    val mapper = { a: Int -> a + 1 }
    val flatMapper = { _: Int -> RemoteData.succeed(1) }
    val dataMapper = RemoteData.Success<Int, DataMapper>(mapper)

    @Test
    @JsName("mapping_NotAsked_keepsState")
    fun `mapping NotAsked keeps the NotAsked state`() {
        val sut = RemoteData.NotAsked
        sut.map(mapper) shouldBe sut
    }

    @Test
    @JsName("mapping_Loading_keepsState")
    fun `mapping Loading keeps the Loading state`() {
        val sut = RemoteData.Loading
        sut.map(mapper) shouldBe sut
    }

    @Test
    @JsName("mapping_Failure_keepsState")
    fun `mapping Failure keeps the Failure state`() {
        val sut = RemoteData.fail(0)
        sut.map(mapper) shouldBe sut
    }

    @Test
    @JsName("mapping_Success_updatesValue")
    fun `mapping Success invokes the mapper for a result`() {
        val sut = RemoteData.succeed(0)
        sut.map(mapper) shouldBe RemoteData.succeed(1)
    }

    @Test
    @JsName("errorMapping_NotAsked_keepsState")
    fun `error mapping NotAsked keeps the NotAsked state`() {
        val sut = RemoteData.NotAsked
        sut.mapError(mapper) shouldBe sut
    }

    @Test
    @JsName("errorMapping_Loading_keepsState")
    fun `error mapping Loading keeps the Loading state`() {
        val sut = RemoteData.Loading
        sut.mapError(mapper) shouldBe sut
    }

    @Test
    @JsName("errorMapping_Failure_updatesValue")
    fun `error mapping Failure invokes the mapper for a result`() {
        val sut = RemoteData.fail(0)
        sut.mapError(mapper) shouldBe RemoteData.fail(1)
    }

    @Test
    @JsName("errorMapping_Success_keepsState")
    fun `error mapping Success keeps the Success state`() {
        val sut = RemoteData.succeed(0)
        sut.mapError(mapper) shouldBe sut
    }

    @Test
    @JsName("mapBoth_NotAsked_keepsState")
    fun `mapping both NotAsked keeps the NotAsked state`() {
        val sut = RemoteData.NotAsked
        sut.mapBoth(mapper, mapper) shouldBe sut
    }

    @Test
    @JsName("mapBoth_Loading_keepsState")
    fun `mapping both Loading keeps the Loading state`() {
        val sut = RemoteData.Loading
        sut.mapBoth(mapper, mapper) shouldBe sut
    }

    @Test
    @JsName("mapBoth_Failure_updatesValue")
    fun `mapping both Failure invokes the failure mapper for a result`() {
        val sut = RemoteData.fail(0)
        val failureMapper = { a: Int -> a + 2 }
        sut.mapBoth(failureMapper, mapper) shouldBe RemoteData.fail(2)
    }

    @Test
    @JsName("mapBoth_Success_updatesValue")
    fun `mapping both Success invokes the mapper for a result`() {
        val sut = RemoteData.succeed(0)
        val successMapper = { a: Int -> a + 2 }
        sut.mapBoth(mapper, successMapper) shouldBe RemoteData.succeed(2)
    }

    @Test
    @JsName("flatMap_NotAsked_keepsState")
    fun `flatMapping NotAsked keeps the NotAsked state`() {
        val sut = RemoteData.NotAsked
        sut.flatMap(flatMapper) shouldBe sut
    }

    @Test
    @JsName("flatMap_Loading_keepsState")
    fun `flatMapping Loading keeps the Loading state`() {
        val sut = RemoteData.Loading
        sut.flatMap(flatMapper) shouldBe sut
    }

    @Test
    @JsName("flatMap_Failure_keepsState")
    fun `flatMapping Failure keeps the Failure state`() {
        val sut = RemoteData.fail(0)
        sut.flatMap(flatMapper) shouldBe sut
    }

    @Test
    @JsName("flatMap_Success_updatesValue")
    fun `flatMapping Success invokes the mapper for a result`() {
        val sut = RemoteData.succeed(0)
        sut.flatMap(flatMapper) shouldBe RemoteData.succeed(1)
    }

    @Test
    @JsName("andMap_NotAsked_keepsState")
    fun `andMapping NotAsked keeps the NotAsked state`() {
        val sut = RemoteData.NotAsked
        dataMapper andMap sut shouldBe sut
    }

    @Test
    @JsName("andMap_Loading_keepsState")
    fun `andMapping Loading keeps the Loading state`() {
        val sut = RemoteData.Loading
        dataMapper andMap sut shouldBe sut
    }

    @Test
    @JsName("andMap_Failure_keepsState")
    fun `andMapping Failure keeps the Failure state`() {
        val sut = RemoteData.fail(0)
        dataMapper andMap sut shouldBe sut
    }

    @Test
    @JsName("andMap_Success_updatesValue")
    fun `andMapping Success invokes the mapper for a result`() {
        val sut = RemoteData.succeed(0)
        dataMapper andMap sut shouldBe RemoteData.succeed(1)
    }
}

private typealias DataMapper = (Int) -> Int
