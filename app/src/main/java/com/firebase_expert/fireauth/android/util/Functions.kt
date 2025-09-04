package com.firebase_expert.fireauth.android.util

import android.content.Context
import android.widget.Toast

fun showToast(
    context: Context,
    message: String
) = Toast.makeText(context, message, Toast.LENGTH_LONG).show()