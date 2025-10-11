package com.firebase_expert.fireauth.android.util.extension

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun Context.openPlayStore(url: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = url.toUri()
        setPackage("com.android.vending")
    }
    startActivity(intent)
}

fun Context.openWebsite(url: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = url.toUri()
    }
    startActivity(intent)
}