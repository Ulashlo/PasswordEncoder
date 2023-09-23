package com.vanikolskii.passwordencoder

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vanikolskii.passwordencoder.barcode.ZxingBarcodeGenerator
import com.vanikolskii.passwordencoder.crypto.AESCipher
import com.vanikolskii.passwordencoder.crypto.CryptoProxy
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale


class CreateQRCodeActivity : AppCompatActivity() {
    private lateinit var returnBt: ImageButton
    private lateinit var secretET: EditText
    private lateinit var keyET: EditText
    private lateinit var generateQRCodeBt: Button
    private lateinit var downloadQRCodeBt: Button
    private lateinit var qrCodeIV: ImageView

    private var currentQRCode: Bitmap? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_qrcode)

        returnBt = findViewById(R.id.returnFromCreateQRCodeBt)
        secretET = findViewById(R.id.secretET)
        keyET = findViewById(R.id.keyForGenerateET)
        generateQRCodeBt = findViewById(R.id.generateQRCodeBt)
        downloadQRCodeBt = findViewById(R.id.downloadQRCodeBT)
        qrCodeIV = findViewById(R.id.qrCodeIV)

        generateQRCodeBt.setOnClickListener {
            val secret = secretET.text.toString()
            if (secret.isEmpty()) {
                Toast.makeText(this, R.string.enter_encode_secret_toast, Toast.LENGTH_SHORT).show()
                Log.i(TAG, "Secret is empty")
                return@setOnClickListener
            }
            if (secret.length > 256) {
                Toast.makeText(this, R.string.encode_secret_too_long_toast, Toast.LENGTH_SHORT)
                    .show()
                Log.i(TAG, "Secret is too long")
                return@setOnClickListener
            }
            val key = keyET.text.toString()
            if (key.isEmpty()) {
                Toast.makeText(this, R.string.enter_key_toast, Toast.LENGTH_SHORT).show()
                Log.i(TAG, "Key is empty")
                return@setOnClickListener
            }
            if (key.length > 40) {
                Toast.makeText(this, R.string.key_too_long_toast, Toast.LENGTH_SHORT).show()
                Log.i(TAG, "Key is too long")
                return@setOnClickListener
            }
            val proxy = CryptoProxy(AESCipher())
            val data: String
            try {
                data = proxy.encode(secret, key)
            } catch (exp: Exception) {
                Toast.makeText(this, R.string.unable_encode_toast, Toast.LENGTH_SHORT).show()
                Log.e(TAG, String.format("Encode error. Secret: %s, key: %s", secret, key), exp)
                return@setOnClickListener
            }
            val r = getSize()
            val generator = ZxingBarcodeGenerator()
            val image = generator.generateQRCode(data, r)
            qrCodeIV.setImageBitmap(generator.convertBitMatrixToBitmap(image, false))
            currentQRCode = generator.convertBitMatrixToBitmap(image, true);
            Log.i(TAG, String.format("Encoded successfully. Secret: %s, key: %s", secret, key))
        }

        downloadQRCodeBt.setOnClickListener {
            if (currentQRCode == null) {
                Toast.makeText(this, R.string.generate_qrcode_reminder_toast, Toast.LENGTH_SHORT)
                    .show()
                Log.i(TAG, "QRCode is not generated")
                return@setOnClickListener
            }
            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .absolutePath
            val imageName = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis())
            val fileName = String.format(
                "qrcode-%s.jpg",
                imageName
            )
            val outFile = File(dir, fileName)
            FileOutputStream(outFile).use { outStream ->
                currentQRCode!!.compress(Bitmap.CompressFormat.PNG, 100, outStream)
                outStream.flush()
                Toast.makeText(this, R.string.save_qrcode_toast, Toast.LENGTH_SHORT).show()
                Log.i(TAG, String.format("Saved QRCode: %s", R.string.save_qrcode_toast, imageName))
            }
        }

        returnBt.setOnClickListener {
            finish()
        }
    }

    private fun getSize(): Int {
        val height = baseContext.resources.displayMetrics.heightPixels
        val width = baseContext.resources.displayMetrics.widthPixels
        return if (height > width) {
            width * 4 / 5
        } else {
            height * 2 / 5
        }
    }

    companion object {
        private const val TAG = "CreateQRCode"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}