package com.example.pdfassignment.model.apiService

import com.example.pdfassignment.model.apiModel.ProductResponse
import retrofit2.http.GET

interface ProductApiService {
    @GET("objects")
    suspend fun getProducts(): List<ProductResponse>
}
