package com.firebase_expert.fireauth.android.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import org.koin.dsl.module

val firebaseModule = module {
    single<FirebaseAuth> {
        Firebase.auth
    }
}