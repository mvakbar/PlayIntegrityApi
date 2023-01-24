package com.example.playintegritylearning

import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface ServiceApi {

    @FormUrlEncoded
    @GET("")
    fun verifyIntegrityToken(
        @Field("integrityToken") integrityToken: String,
    ): Call<String>

    @GET("")
    fun getUniqueValue(): Call<String>

}
