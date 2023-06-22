package com.musfick.agentx.domain.user_case

import com.musfick.agentx.CommonFlow
import com.musfick.agentx.asCommonFlow
import com.musfick.agentx.asListWrapper
import com.musfick.agentx.data.local.DatabaseDriverFactory
import com.musfick.agentx.data.local.LocalDataSource
import com.musfick.agentx.data.local.LocalDataSourceImpl
import com.musfick.agentx.data.remote.RemoteDataSource
import com.musfick.agentx.data.remote.RemoteDataSourceImpl
import com.musfick.agentx.domain.model.Agent
import com.musfick.agentx.domain.repository.AgentRepository
import com.musfick.agentx.domain.repository.AgentRepositoryImpl
import com.musfick.agentx.network.ValorantApi
import com.musfick.agentx.utils.ListWrapper
import com.musfick.agentx.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllAgentsUseCase(databaseDriverFactory: DatabaseDriverFactory) {

    private val valorantApi = ValorantApi()
    private val localDataSource:LocalDataSource = LocalDataSourceImpl(databaseDriverFactory)
    private val remoteDataSource:RemoteDataSource = RemoteDataSourceImpl(valorantApi)
    private val repository:AgentRepository = AgentRepositoryImpl(localDataSource, remoteDataSource)

    operator fun invoke():Flow<Resource<ListWrapper<Agent>>> = repository.getAllAgents().asListWrapper()

    val allAgents:CommonFlow<Resource<ListWrapper<Agent>>> = invoke().asCommonFlow()

}