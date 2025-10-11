package com.firebase_expert.fireauth.android.util.extension

import android.util.Patterns

fun String.isEmailValid() = isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isPhoneNumberValid() = isNotBlank() && Patterns.PHONE.matcher(this).matches()

fun String.prefixWithPlus() = "+$this"