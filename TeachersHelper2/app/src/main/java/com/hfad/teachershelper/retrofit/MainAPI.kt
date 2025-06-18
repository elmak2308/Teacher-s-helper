package com.hfad.teachershelper.retrofit

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
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

    @POST("/signup") // сменить url
    suspend fun auth(@Body authRequestFullName: AuthRequestFullName): User
    @POST("/hhh")// сменить url
    suspend fun autH(@Body authRequestHashedPassword: AuthRequestHashedPassword): User


    @POST("/token")
    suspend fun sendLoginPaorol(@Body loginParol: Login_parol): ResponseData
    suspend fun getToken(@Field("grant_type") grantType: String = "password",
                         @Field("username") username: String,
                         @Field("password") password: String,
                         @Field("scope") scope: String = "",
                         @Field("client_id") clientId: String = "client",
                         @Field("client_secret") clientSecret: String = "secret"
    ): Response<TokenReponse>
}