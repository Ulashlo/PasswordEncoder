package com.vanikolskii.passwordencoder.barcode

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyzer(val callback: (String, Int) -> Unit) :
    ImageAnalysis.Analyzer {

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
//        val w = imageProxy.width
//        val h = imageProxy.height
//        val r = (min(w, h) + 0.8).toInt();
//
//        imageProxy.setCropRect(
//            Rect(
//                (w - r) / 2,
//                (h - r) / 2,
//                (w + r) / 2,
//                (h + r) / 2,
//            )
//        )

        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        image.planes

        val scanner = BarcodeScanning.getClient()

        val result = scanner.process(image)
            .addOnSuccessListener { barcodes ->
                Log.d(TAG, "Scan success")
                if (barcodes.size > 0) {
                    callback(barcodes[0].rawValue ?: "", Activity.RESULT_OK)
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Scan fail")
                callback("", Activity.RESULT_CANCELED)
            }
            .addOnCompleteListener {
                imageProxy.close()
                Log.d(TAG, "Image proxy closed")
            }
    }

    companion object {
        const val TAG = "BarcodeAnalyzer"
    }
}