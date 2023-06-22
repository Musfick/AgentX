package com.musfick.agentx.domain.repository

import com.musfick.agentx.data.local.LocalDataSource
import com.musfick.agentx.data.remote.RemoteDataSource
import com.musfick.agentx.data.toAgent
import com.musfick.agentx.data.toAgentEntity
import com.musfick.agentx.domain.model.Agent
import com.musfick.agentx.utils.Resource
import com.musfick.agentx.utils.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AgentRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
):AgentRepository {


    override fun getAllAgents(): Flow<Resource<List<Agent>>>{
        return networkBoundResource(
            query = {
                localDataSource.getAllAgents().map {
                    it.map { it.toAgent() }
                }
            },
            fetch = {
                remoteDataSource.getAllAgents()
            },
            saveFetchResult = {
                val items = it.data.map { it.toAgentEntity() }
                localDataSource.insertAgent(items)
            }
        )
    }
}