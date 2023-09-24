package com.vanikolskii.secretencoder

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.vanikolskii.secretencoder.contracts.ScanResultContract
import com.vanikolskii.secretencoder.crypto.AESCipher
import com.vanikolskii.secretencoder.crypto.CryptoProxy


class ScanSecretActivity : AppCompatActivity() {
    private lateinit var startScannerBt: Button
    private lateinit var copyBt: Button
    private lateinit var keyET: EditText
    private lateinit var resultTV: TextView
    private lateinit var returnBt: ImageButton
    private lateinit var layout: ConstraintLayout

    private var activityLauncher: ActivityResultLauncher<Unit> = registerForActivityResult(
        ScanResultContract()
    ) { result ->
        if (!result.isSuccess) {
            Toast.makeText(this, R.string.scan_error_toast, Toast.LENGTH_SHORT).show()
            Log.i(TAG, "Scan error")
            return@registerForActivityResult
        }
        val key = keyET.text.toString()
        if (key.isEmpty()) {
            Toast.makeText(this, R.string.enter_key_toast, Toast.LENGTH_SHORT).show()
            Log.i(TAG, "Key is empty")
            return@registerForActivityResult
        }
        if (key.length > 40) {
            Toast.makeText(this, R.string.key_too_long_toast, Toast.LENGTH_SHORT).show()
            Log.i(TAG, "Key is too long")
            return@registerForActivityResult
        }
        val proxy = CryptoProxy(AESCipher())
        try {
            resultTV.text = proxy.decode(result.data, key)
        } catch (exp: Exception) {
            Toast.makeText(this, R.string.unable_decode_toast, Toast.LENGTH_LONG).show()
            Log.e(TAG, String.format("Decode error. Key: %s", key), exp)
            return@registerForActivityResult
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_secret)

        startScannerBt = findViewById(R.id.startScannerBt)
        copyBt = findViewById(R.id.copyBt)
        keyET = findViewById(R.id.keyForScanET)
        resultTV = findViewById(R.id.resultTV)
        returnBt = findViewById(R.id.returnFromScanPasswordBt)
        layout = findViewById(R.id.scanSecretLayout)

        startScannerBt.setOnClickListener {
            val key = keyET.text.toString()
            if (key.isEmpty()) {
                Toast.makeText(this, R.string.enter_key_toast, Toast.LENGTH_SHORT).show()
                Log.i(TAG, "Key is empty")
                return@setOnClickListener
            }
            resultTV.text = ""
            activityLauncher.launch(Unit)
            closeKeyboard()
        }

        copyBt.setOnClickListener {
            val text = resultTV.text
            if (text.isEmpty()) {
                Toast.makeText(this, R.string.nothing_to_copy_toast, Toast.LENGTH_SHORT).show()
                Log.i(TAG, "Nothing to copy")
                return@setOnClickListener
            }
            val clipboard: ClipboardManager =
                getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(text, text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, R.string.secret_copied_toast, Toast.LENGTH_SHORT).show()
            Log.i(TAG, "Secret copied to clipboard")
        }

        layout.setOnClickListener {
            closeKeyboard()
        }

        returnBt.setOnClickListener {
            finish()
        }
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val manager = getSystemService(
                INPUT_METHOD_SERVICE
            ) as InputMethodManager
            manager
                .hideSoftInputFromWindow(
                    view.windowToken, 0
                )
        }
    }

    companion object {
        private const val TAG = "ScanSecret"
    }
}