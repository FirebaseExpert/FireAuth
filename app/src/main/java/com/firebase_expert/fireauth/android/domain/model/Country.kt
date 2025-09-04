package com.firebase_expert.fireauth.android.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val code: String,
    val phoneCode: String
)