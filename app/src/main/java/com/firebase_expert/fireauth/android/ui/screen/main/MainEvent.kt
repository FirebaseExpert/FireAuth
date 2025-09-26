package com.firebase_expert.fireauth.android.ui.screen.main

sealed class MainEvent {
    data class ShowToast(val message: String) : MainEvent()
}