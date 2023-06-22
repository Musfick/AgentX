package com.musfick.agentx.domain.repository

import com.musfick.agentx.domain.model.Agent
import com.musfick.agentx.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AgentRepository {
    fun getAllAgents():Flow<Resource<List<Agent>>>
}