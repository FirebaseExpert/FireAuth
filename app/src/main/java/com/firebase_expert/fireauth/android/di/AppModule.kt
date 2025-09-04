package com.firebase_expert.fireauth.android.di

import android.content.Context
import android.content.SharedPreferences
import com.firebase_expert.fireauth.android.R
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.actionCodeSettings
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val EMAIL_PREFS = "email_prefs"

val appModule = module {
    single<SharedPreferences> {
        val appContext = androidContext().applicationContext
        appContext.getSharedPreferences(EMAIL_PREFS, Context.MODE_PRIVATE)
    }

    single<ActionCodeSettings> {
        val context = androidContext()
        actionCodeSettings {
            url = context.getString(R.string.app_url)
            handleCodeInApp = true
            setAndroidPackageName(
                context.packageName,
                true,
                "1",
            )
        }
    }
}