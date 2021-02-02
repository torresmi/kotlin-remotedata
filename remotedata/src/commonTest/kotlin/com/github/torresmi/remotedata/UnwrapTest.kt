package com.github.torresmi.remotedata

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class UnwrapTest2 {

    @Test
    fun `get value from NotAsked returns null`() {
        RemoteData.NotAsked.getOrNull() shouldBe null
    }

    @Test
    fun `get value or null from Loading returns null`() {
        RemoteData.Loading.getOrNull() shouldBe null
    }

    @Test
    fun `get value or null from Failure returns null`() {
        val value = 0
        RemoteData.fail(value).getOrNull() shouldBe null
    }

    @Test
    fun `get value or null from Success returns value`() {
        val value = 0
        RemoteData.succeed(value).getOrNull() shouldBe value
    }

    @Test
    fun `get value or default from NotAsked returns default`() {
        val default = 0
        with(RemoteData.NotAsked) {
            getOrElse(default) shouldBe default
            getOrElse { default } shouldBe default
        }
    }

    @Test
    fun `get value or default from Loading returns default`() {
        val default = 0
        with (RemoteData.Loading) {
            getOrElse(default) shouldBe default
            getOrElse { default } shouldBe default
        }
    }

    @Test
    fun `get value or default from Failure returns default`() {
        val value = 0
        val default = 1
        with(RemoteData.fail(value)) {
            getOrElse(default) shouldBe default
            getOrElse { default } shouldBe default
        }
    }

    @Test
    fun `get value or default from Success returns value`() {
        val value = 0
        val default = 1
        with(RemoteData.succeed(value)) {
            getOrElse(default) shouldBe value
            getOrElse { default } shouldBe value
        }
    }
}
