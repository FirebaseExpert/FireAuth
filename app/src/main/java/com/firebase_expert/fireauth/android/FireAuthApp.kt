package com.firebase_expert.fireauth.android

import android.app.Application
import com.firebase_expert.fireauth.android.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import com.firebase_expert.fireauth.android.di.firebaseModule
import com.firebase_expert.fireauth.android.di.repositoryModule
import com.firebase_expert.fireauth.android.di.viewModelModule

class FireAuthApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FireAuthApp)
            modules(
                appModule,
                viewModelModule,
                firebaseModule,
                repositoryModule
            )
        }
    }
}