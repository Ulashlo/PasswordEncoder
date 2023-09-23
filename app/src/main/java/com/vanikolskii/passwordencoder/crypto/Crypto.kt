package com.vanikolskii.passwordencoder.crypto

interface Crypto {
    fun decode(data: ByteArray, key: ByteArray): ByteArray
    fun encode(data: ByteArray, key: ByteArray): ByteArray
}