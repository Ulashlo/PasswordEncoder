package com.vanikolskii.passwordencoder.barcode

import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter

class ZxingBarcodeGenerator : BarcodeGenerator {
    override fun generateQRCode(data: String, r: Int): BitMatrix {
        val writer = QRCodeWriter()
        return writer.encode(
            data,
            BarcodeFormat.QR_CODE,
            r,
            r
        )
    }
}