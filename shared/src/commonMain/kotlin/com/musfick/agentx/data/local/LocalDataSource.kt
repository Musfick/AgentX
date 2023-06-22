package com.musfick.agentx.data.local

import com.musfick.agentx.data.remote.AgentDTO
import database.AgentEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getAllAgents():Flow<List<AgentEntity>>
    suspend fun insertAgent(agents: List<AgentEntity>)
}