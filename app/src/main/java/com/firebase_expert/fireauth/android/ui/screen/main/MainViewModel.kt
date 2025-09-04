package com.firebase_expert.fireauth.android.ui.screen.main

import androidx.lifecycle.ViewModel
import com.firebase_expert.fireauth.android.domain.repository.MainRepository

class MainViewModel(
    mainRepo: MainRepository
): ViewModel() {
    val message = mainRepo.message
}