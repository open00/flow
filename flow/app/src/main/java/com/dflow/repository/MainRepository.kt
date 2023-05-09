package com.dflow.repository

import com.dflow.api.ApiService
import com.dflow.utils.manageApi
import com.dflow.utils.manageApiFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class MainRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getRandomItem () = manageApi {
        apiService.getRandomItem()
    }

    suspend fun getRandomItemWithFlow () = manageApiFlow {
        apiService.getRandomItem()
    }
}