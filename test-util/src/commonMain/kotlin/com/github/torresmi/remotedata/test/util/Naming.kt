package com.github.torresmi.remotedata.test.util

/**
 * Javascript tests do not allow for backtick test names. This allows specifying
 * other names for javascript tests.
 *
 * Other platforms default to the more flexible
 */
expect annotation class JsName constructor(val name: String)
