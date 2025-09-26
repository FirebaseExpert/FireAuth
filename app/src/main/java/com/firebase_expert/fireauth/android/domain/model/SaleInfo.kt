package com.firebase_expert.fireauth.android.domain.model

data class SaleInfo(
    val apps: List<App> = emptyList(),
    val message: String = "",
    val question: String = "",
    val url: String = ""
)

data class App(
    val name: String = "",
    val url: String = ""
)