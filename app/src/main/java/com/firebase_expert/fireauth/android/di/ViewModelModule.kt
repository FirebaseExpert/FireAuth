package com.firebase_expert.fireauth.android.di

import com.firebase_expert.fireauth.android.domain.repository.AuthPrefsRepository
import com.firebase_expert.fireauth.android.domain.repository.AuthRepository
import com.firebase_expert.fireauth.android.domain.repository.MainRepository
import com.firebase_expert.fireauth.android.ui.screen.auth.AuthViewModel
import com.firebase_expert.fireauth.android.ui.screen.main.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        AuthViewModel(
            authRepo = get<AuthRepository>(),
            authPrefsRepo = get<AuthPrefsRepository>()
        )
    }

    viewModel {
        MainViewModel(
            mainRepo = get<MainRepository>()
        )
    }
}