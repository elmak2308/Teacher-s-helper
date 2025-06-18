package com.hfad.teachershelper.retrofit

data class AuthRequestHashedPassword(
    val password: String,
    val temp_token: String
)
