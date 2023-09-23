package com.vanikolskii.passwordencoder.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.vanikolskii.passwordencoder.ScannerActivity

class ScanResultContract : ActivityResultContract<Unit, ScanResult>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(context, ScannerActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): ScanResult = when {
        resultCode != Activity.RESULT_OK -> ScanResult("", false)
        else -> ScanResult(intent?.getStringExtra(CONTENT) ?: "", true)
    }

    companion object {
        const val CONTENT = "content"
    }
}