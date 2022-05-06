package com.example.mvvmstickermodule

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.stickers.activity.StickerActivity
import com.permissionx.guolindev.PermissionX

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sticker = findViewById<TextView>(R.id.text_stickers)

        sticker.setOnClickListener {
                        startActivity(Intent(this, StickerActivity::class.java))
        }

    }
}