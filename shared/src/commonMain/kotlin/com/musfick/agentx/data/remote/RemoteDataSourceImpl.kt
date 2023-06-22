package com.musfick.agentx.data.remote

import com.musfick.agentx.network.ValorantApi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class RemoteDataSourceImpl(
    private val valorantApi: ValorantApi
):RemoteDataSource {

    val httpClient = valorantApi.httpClient

    override suspend fun getAllAgents(): Response<List<AgentDTO>> {
        return httpClient.get("https://valorant-api.com/v1/agents?isPlayableCharacter=true").body()
    }
}
