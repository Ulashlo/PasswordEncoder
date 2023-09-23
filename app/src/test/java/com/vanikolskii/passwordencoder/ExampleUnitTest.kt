package com.vanikolskii.passwordencoder

import com.vanikolskii.passwordencoder.crypto.AESCipher
import com.vanikolskii.passwordencoder.crypto.CryptoProxy
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun checkAECCipher() {
        val crypto = CryptoProxy(AESCipher())
        val password = "Some password"
        val testText = "Text for testing. Very long text! Text for testing. Very long text! Text for testing. " +
                "Very long text! Text for testing. Very long text! Text for testing. Very long text! " +
                "Text for testing. Very long text! Text for testing. Very long text! Text for testing. Very long text! " +
                "Text for testing. Very long text! Text for testing. Very long text! Text for testing. Very long text! "
        val encoded = crypto.encode(testText, password)
        val decoded = crypto.decode(encoded, password)
        assertEquals(testText, decoded)
    }
}