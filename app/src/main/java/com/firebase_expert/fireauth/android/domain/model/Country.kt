package com.firebase_expert.fireauth.android.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val name: String,
    val phoneCode: String
)