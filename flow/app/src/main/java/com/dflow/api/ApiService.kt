package com.dflow.api

import com.dflow.api.dto.ItemResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET(ApiConstants.RANDOM_URL)
    suspend fun getRandomItem() : Response<ItemResponse>
}

