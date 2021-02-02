package com.github.torresmi.remotedata

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class TransformTest {

    val mapper = { a: Int -> a + 1 }
    val flatMapper = { a: Int -> RemoteData.succeed(1) }
    val dataMapper = RemoteData.Success<Int, DataMapper>(mapper)

    @Test
    fun `mapping NotAsked keeps the NotAsked state`() {
        val sut = RemoteData.NotAsked
        sut.map(mapper) shouldBe sut
    }

    @Test
    fun `mapping Loading keeps the Loading state`() {
        val sut = RemoteData.Loading
        sut.map(mapper) shouldBe sut
    }

    @Test
    fun `mapping Failure keeps the Failure state`() {
        val sut = RemoteData.fail(0)
        sut.map(mapper) shouldBe sut
    }

    @Test
    fun `mapping Success invokes the mapper for a result`() {
        val sut = RemoteData.succeed(0)
        sut.map(mapper) shouldBe RemoteData.succeed(1)
    }

    @Test
    fun `error mapping NotAsked keeps the NotAsked state`() {
        val sut = RemoteData.NotAsked
        sut.mapError(mapper) shouldBe sut
    }

    @Test
    fun `error mapping Loading keeps the Loading state`() {
        val sut = RemoteData.Loading
        sut.mapError(mapper) shouldBe sut
    }

    @Test
    fun `error mapping Failure invokes the mapper for a result`() {
        val sut = RemoteData.fail(0)
        sut.map(mapper) shouldBe RemoteData.fail(1)
    }

    @Test
    fun `error mapping Success keeps the Success state`() {
        val sut = RemoteData.succeed(0)
        sut.map(mapper) shouldBe RemoteData.succeed(1)
    }

    @Test
    fun `mapping both NotAsked keeps the NotAsked state`() {
        val sut = RemoteData.NotAsked
        sut.mapBoth(mapper, mapper) shouldBe sut
    }

    @Test
    fun `mapping both Loading keeps the Loading state`() {
        val sut = RemoteData.Loading
        sut.mapBoth(mapper, mapper) shouldBe sut
    }

    @Test
    fun `mapping both Failure invokes the failure mapper for a result`() {
        val sut = RemoteData.fail(0)
        val failureMapper = { a: Int -> a + 2 }
        sut.mapBoth(failureMapper, mapper) shouldBe RemoteData.fail(2)
    }

    @Test
    fun `mapping both Success invokes the mapper for a result`() {
        val sut = RemoteData.succeed(0)
        val successMapper = { a: Int -> a + 2 }
        sut.mapBoth(mapper, successMapper) shouldBe RemoteData.succeed(2)
    }

    @Test
    fun `flatMapping both NotAsked keeps the NotAsked state`() {
        val sut = RemoteData.NotAsked
        sut.flatMap(flatMapper) shouldBe sut
    }

    @Test
    fun `flatMapping both Loading keeps the Loading state`() {
        val sut = RemoteData.Loading
        sut.flatMap(flatMapper) shouldBe sut
    }

    @Test
    fun `flatMapping both Failure invokes the failure mapper for a result`() {
        val sut = RemoteData.fail(0)
        sut.flatMap(flatMapper) shouldBe RemoteData.succeed(1)
    }

    @Test
    fun `flatMapping both Success invokes the mapper for a result`() {
        val sut = RemoteData.succeed(0)
        sut.flatMap(flatMapper) shouldBe RemoteData.succeed(1)
    }

    @Test
    fun `andMapping both NotAsked keeps the NotAsked state`() {
        val sut = RemoteData.NotAsked
        dataMapper andMap sut shouldBe sut
    }

    @Test
    fun `andMapping both Loading keeps the Loading state`() {
        val sut = RemoteData.Loading
        dataMapper andMap sut shouldBe sut
    }

    @Test
    fun `andMapping both Failure keeps the Failure state`() {
        val sut = RemoteData.fail(0)
        dataMapper andMap sut shouldBe sut
    }

    @Test
    fun `andMapping both Success invokes the mapper for a result`() {
        val sut = RemoteData.succeed(0)
        dataMapper andMap sut shouldBe RemoteData.succeed(1)
    }
}

private typealias DataMapper = (Int) -> Int
