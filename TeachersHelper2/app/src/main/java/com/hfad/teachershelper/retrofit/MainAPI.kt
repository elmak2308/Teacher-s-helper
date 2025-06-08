package com.hfad.teachershelper.retrofit

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MainAPI {
    @GET("subjects/")
//короче тут должна быть ссылка на предмет
    //типо как понять какой предмет каким по счету
    suspend fun getSubjectById(): List<Subject>
    //@Path("id") id: Int
    @GET("subjects/")
    suspend fun getAllItems(): List<Subject>

    @POST("/signup")
    suspend fun auth(@Body authRequestFullName: AuthRequestFullName): User

    suspend fun autH(@Body authRequestHashedPassword: AuthRequestHashedPassword): User
}