package com.example.recyclerview

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder()
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl("https://google.com")
    .build()

interface StudentApiService {
    @GET("123")
    fun getAllStudent(@Query("limit") type: Int): Deferred<List<Student>>
}

object StudentApi {
    val retrofitService: StudentApiService by lazy {
        retrofit.create(StudentApiService::class.java)
    }
}