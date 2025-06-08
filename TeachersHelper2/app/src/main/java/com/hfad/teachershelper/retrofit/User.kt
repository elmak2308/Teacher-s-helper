package com.hfad.teachershelper.retrofit

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("phone") val phone: String,
    @SerializedName("email") val email: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("hashed_password") val hashedPassword: String,
    @SerializedName("disabled") val disabled: Boolean
)
