package com.firebase_expert.fireauth.android.util.extensions

import android.content.Intent

fun Intent.clearEmailLink() {
    data = null
}

val Intent.emailLink: String? get() = data?.toString()