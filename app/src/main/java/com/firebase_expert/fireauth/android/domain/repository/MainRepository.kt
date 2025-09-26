package com.firebase_expert.fireauth.android.domain.repository

import com.firebase_expert.fireauth.android.domain.model.SaleInfo

interface MainRepository {
    suspend fun getSaleInfo(): SaleInfo?
}