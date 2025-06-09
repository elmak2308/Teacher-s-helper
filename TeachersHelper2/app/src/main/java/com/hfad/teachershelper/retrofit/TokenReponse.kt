package com.hfad.teachershelper.retrofit

import com.google.gson.annotations.SerializedName

data class TokenReponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String
)
