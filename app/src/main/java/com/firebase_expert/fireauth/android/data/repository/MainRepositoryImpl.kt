package com.firebase_expert.fireauth.android.data.repository

import com.firebase_expert.fireauth.android.domain.model.SaleInfo
import com.firebase_expert.fireauth.android.domain.repository.MainRepository
import com.firebase_expert.fireauth.android.util.SALE_INFO
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class MainRepositoryImpl(
    private val db: FirebaseDatabase
) : MainRepository {
    override suspend fun getSaleInfo() = db.reference.child(SALE_INFO)
        .get()
        .await()
        .getValue(SaleInfo::class.java)
}