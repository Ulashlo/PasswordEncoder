package com.vanikolskii.secretencoder

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var scanQRCodeBt: ImageButton
    private lateinit var createQRCodeBt: ImageButton
    private lateinit var quietBt: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scanQRCodeBt = findViewById(R.id.scanQRCodeBt)
        createQRCodeBt = findViewById(R.id.createQRCodeBt)
        quietBt = findViewById(R.id.quietBt)

        scanQRCodeBt.setOnClickListener {
            intent = Intent(this, ScanSecretActivity::class.java)
            startActivity(intent)
        }

        createQRCodeBt.setOnClickListener {
            intent = Intent(this, CreateQRCodeActivity::class.java)
            startActivity(intent)
        }

        quietBt.setOnClickListener {
            finish()
        }
    }
}