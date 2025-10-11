package com.firebase_expert.fireauth.android.domain.repository

import com.firebase_expert.fireauth.android.domain.model.App

interface MainRepository {
    suspend fun getApps(): List<App>?
}