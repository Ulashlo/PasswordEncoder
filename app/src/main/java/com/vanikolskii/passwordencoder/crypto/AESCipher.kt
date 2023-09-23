package com.vanikolskii.passwordencoder.crypto

import java.security.spec.KeySpec
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


class AESCipher : Crypto {
    private val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")

    override fun decode(data: ByteArray, key: ByteArray): ByteArray {
        val k = getKeyFromPassword(key)
        cipher.init(Cipher.DECRYPT_MODE, k, ivSpec)
        return cipher.doFinal(Base64.getDecoder().decode(data))
    }

    override fun encode(data: ByteArray, key: ByteArray): ByteArray {
        val k = getKeyFromPassword(key)
        cipher.init(Cipher.ENCRYPT_MODE, k, ivSpec)
        return Base64.getEncoder().encode(cipher.doFinal(data))
    }

    private fun getKeyFromPassword(password: ByteArray): SecretKey {
        val factory =
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec: KeySpec =
            PBEKeySpec(String(password).toCharArray(), salt, 65536, 256)
        return SecretKeySpec(
            factory.generateSecret(spec)
                .encoded, "AES"
        )
    }

    companion object {
        val salt =
            "wdvw54vf$%ve54h5_r!be1v5rtb5evvrwcsvsedxfgrsgdfhgvdset!vsdvbfdhrg".encodeToByteArray()
        val ivSpec = IvParameterSpec(
            byteArrayOf(-71, -62, 113, -108, 96, -79, 71, 39, 44, 118, 6, 100, 15, 102, 108, 21)
        )
    }
}