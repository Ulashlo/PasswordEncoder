package com.vanikolskii.secretencoder.crypto

class CryptoProxy(private val crypto: Crypto) {

    fun encode(data: String, key: String): String {
        return String(
            crypto.encode(
                data.encodeToByteArray(),
                key.encodeToByteArray()
            ), Charsets.UTF_8
        )
    }

    fun decode(data: String, key: String): String {
        return String(
            crypto.decode(
                data.encodeToByteArray(),
                key.encodeToByteArray()
            ), Charsets.UTF_8
        )
    }
}