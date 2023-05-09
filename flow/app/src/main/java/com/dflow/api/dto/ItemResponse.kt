package com.dflow.api.dto
import com.google.gson.annotations.SerializedName


data class ItemResponse(

    @SerializedName("message")
    val message: String?,

    @SerializedName("status")
    val status: String?
)