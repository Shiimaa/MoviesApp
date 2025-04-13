package com.moviesapp.domain

import okhttp3.Interceptor
import okhttp3.Response

private const val authToken =
    "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhOTlhYWNkMjk4ZGZjNTllZGFiZDI5ODI0MmJjZThmMiIsIm5iZiI6MTYxNzk2MzYwMS4xNTIsInN1YiI6IjYwNzAyYTUxOTI0Y2U1MDAyOWIzNjI0OCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.wqG-AtOKsnOj6l3z5H2_Nsguf12af5OxI8bjRzS4LT4"

class AuthInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestBuilder = originalRequest.newBuilder()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer $authToken")

        val request = requestBuilder.build()

        return chain.proceed(request)
    }
}
