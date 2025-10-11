package com.firebase_expert.fireauth.android.data.repository

import com.firebase_expert.fireauth.android.domain.model.App
import com.firebase_expert.fireauth.android.domain.repository.MainRepository
import com.firebase_expert.fireauth.android.util.APPS
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class MainRepositoryImpl(
    private val db: FirebaseDatabase
) : MainRepository {
    override suspend fun getApps() = db.reference.child(APPS)
        .get()
        .await()
        .children.mapNotNull { snapshot ->
            snapshot.getValue(App::class.java)
        }
}