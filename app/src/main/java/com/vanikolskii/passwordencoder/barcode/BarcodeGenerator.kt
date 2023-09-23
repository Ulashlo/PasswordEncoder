package com.vanikolskii.passwordencoder.barcode

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.common.BitMatrix

interface BarcodeGenerator {
    fun generateQRCode(data: String, r: Int): BitMatrix

    fun convertBitMatrixToBitmap(image: BitMatrix, isImage: Boolean): Bitmap {
        val backgroundColor = if (isImage) Color.WHITE else Color.TRANSPARENT
        val width = image.width
        val height = image.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (image.get(x, y)) Color.BLACK else backgroundColor
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }
}