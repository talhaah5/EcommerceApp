package com.example.ecommerceapplication

import com.example.ecommerceapplication.global.Global

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTest {
    @Test
    fun emailValidator_CorrectEmail() {
        val global = Global()
        assertEquals(global.isEmailValid("name@email.com"),true)
    }
    @Test
    fun emailValidator_InCorrectEmail() {
        val global = Global()
        assertEquals(global.isEmailValid("name.c"),false)
    }
}