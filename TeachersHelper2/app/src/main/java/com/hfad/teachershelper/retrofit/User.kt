package com.hfad.teachershelper.retrofit

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("phone") val phone: String,
    @SerializedName("email") val email: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("password") val hashedPassword: String
)
