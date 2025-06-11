package com.hfad.teachershelper.retrofit

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MainAPI {
    @GET("subjects/{id}")
    suspend fun getSubjectById(@Path("id") id: Int): Subject

    @GET("subjects/")
    suspend fun getAllItems(): List<Subject>
    @POST("/signup")
    suspend fun sendLoginPaorol(@Body loginParol: Login_parol): ResponseData

    @FormUrlEncoded
    @POST("token/init")
    suspend fun getToken(
        @Field("phone") phone: String
    ): Response<TokenReponse>

    @FormUrlEncoded
    @POST("token/complete")
    suspend fun get(
        @Field("password") password: String,
        @Field("temp_token") tempToken: String
    ): Response<TokenReponse>
}
//    @GET("subjects/{id}")
//    suspend fun getSubjectById(@Path("id") id: Int): Subject
//
//    @GET("subjects/")
//    suspend fun getAllItems(): List<Subject>
//
//    @POST("/signup")
//    suspend fun sendLoginPaorol(@Body loginParol: Login_parol): ResponseData
//
//    @POST("/token/init")
//    suspend fun getToken(@Body authRequestFullName: AuthRequestFullName): Response<TokenReponse>
//
//    @POST("/token/complete")
//    suspend fun get(@Body authRequestHashedPassword: AuthRequestHashedPassword): Response<TokenReponse>

//    @GET("subjects/")
////короче тут должна быть ссылка на предмет
//    //типо как понять какой предмет каким по счету
//    suspend fun getSubjectById(@Path("id") id: Int): List<Subject>
//    //@Path("id") id: Int
//    @GET("subjects/")
//    suspend fun getAllItems(): List<Subject>
//
////    @POST("/token/init") // сменить url
////    suspend fun auth(@Body authRequestFullName: AuthRequestFullName): User
////    @POST("/token/complete")// сменить url
////    suspend fun autH(@Body authRequestHashedPassword: AuthRequestHashedPassword): User
//
//
//    @POST("/signup")
//    suspend fun sendLoginPaorol(@Body loginParol: Login_parol): ResponseData
//
//    @POST("/token/init")
//    suspend fun getToken(authRequestFullName: AuthRequestFullName
//    ): Response<TokenReponse>
////    @Field("username") username: String,
//    @POST("/token/complete")
//    suspend fun get(@Body authRequestHashedPassword: AuthRequestHashedPassword
//    ): Response<TokenReponse>

