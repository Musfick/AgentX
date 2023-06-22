package com.musfick.agentx.data.local

import com.musfick.agentx.database.AgentXDatabase
import com.musfick.agentx.data.remote.AgentDTO
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import database.AgentEntity
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(databaseDriverFactory: DatabaseDriverFactory): LocalDataSource {

    private val db = AgentXDatabase(databaseDriverFactory.createDriver())
    private val queries = db.agentQueries

    override fun getAllAgents(): Flow<List<AgentEntity>> {
        return queries.getAllAgents().asFlow().mapToList()
    }

    override suspend fun insertAgent(agents: List<AgentEntity>) {
        queries.transaction {
            agents.forEach {
                queries.insertAgent(
                    uuid = it.uuid,
                    backgroundGradientColors = it.backgroundGradientColors,
                    displayName = it.displayName,
                    displayIcon = it.displayIcon,
                    fullPortrait = it.fullPortrait,
                    background = it.background,
                    role = it.role,
                    description = it.description
                )
            }
        }
    }

}