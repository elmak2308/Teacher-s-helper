package com.hfad.teachershelper.retrofit

data class AuthResponse(
    val success: Boolean,
    val accessToken: String? = null,
    val access_token: String? = null,
    val token_type: String? = null
)
