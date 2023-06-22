package com.musfick.agentx

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request

class Greeting {
    private val platform: Platform = getPlatform()
    private val client = HttpClient()


    suspend fun greeting(): String {

        try {
            val response = client.get("https://ktor.io/docs/")
            return response.bodyAsText()
        }catch (e:Exception){
            return "Error"
        }

    }
}