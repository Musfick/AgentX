package com.musfick.agentx.data.remote

interface RemoteDataSource {
    suspend fun getAllAgents():Response<List<AgentDTO>>
}