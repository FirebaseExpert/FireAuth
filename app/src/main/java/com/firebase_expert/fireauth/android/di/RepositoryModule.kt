package com.firebase_expert.fireauth.android.di

import android.content.SharedPreferences
import com.firebase_expert.fireauth.android.data.repository.AuthPrefsRepositoryImpl
import com.firebase_expert.fireauth.android.data.repository.AuthRepositoryImpl
import com.firebase_expert.fireauth.android.data.repository.MainRepositoryImpl
import com.firebase_expert.fireauth.android.domain.repository.AuthPrefsRepository
import com.firebase_expert.fireauth.android.domain.repository.AuthRepository
import com.firebase_expert.fireauth.android.domain.repository.MainRepository
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> {
        AuthRepositoryImpl(
            auth = get<FirebaseAuth>(),
            actionCodeSettings = get<ActionCodeSettings>()
        )
    }

    single<AuthPrefsRepository> {
        AuthPrefsRepositoryImpl(
            sharedPref = get<SharedPreferences>()
        )
    }

    single<MainRepository> {
        MainRepositoryImpl(
            db = get<FirebaseDatabase>()
        )
    }
}