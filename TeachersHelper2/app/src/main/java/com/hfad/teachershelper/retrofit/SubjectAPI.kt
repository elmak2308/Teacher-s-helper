package com.hfad.teachershelper.retrofit

import retrofit2.http.GET
import retrofit2.http.Path

interface SubjectAPI {
    @GET("subjects/{id}")
//короче тут должна быть ссылка на предмет
    //типо как понять какой предмет каким по счету
    suspend fun getSubjectById(@Path("id") id: Int): Subject
}