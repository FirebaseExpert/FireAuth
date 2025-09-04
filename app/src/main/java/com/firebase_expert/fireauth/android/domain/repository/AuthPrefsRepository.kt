package com.firebase_expert.fireauth.android.domain.repository

interface AuthPrefsRepository {
    fun getEmail(): String?

    fun setEmail(email: String)
}