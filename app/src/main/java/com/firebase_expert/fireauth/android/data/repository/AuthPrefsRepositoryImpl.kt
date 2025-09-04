package com.firebase_expert.fireauth.android.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.firebase_expert.fireauth.android.domain.repository.AuthPrefsRepository

const val EMAIL_KEY = "email"

class AuthPrefsRepositoryImpl(
    private val sharedPref: SharedPreferences
) : AuthPrefsRepository {
    override fun getEmail() = sharedPref.getString(EMAIL_KEY, null)

    override fun setEmail(email: String) = sharedPref.edit {
        putString(EMAIL_KEY, email)
    }
}