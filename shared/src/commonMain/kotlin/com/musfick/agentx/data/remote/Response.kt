package com.musfick.agentx.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class Response<T> (
    val status:Int,
    val data:T
)