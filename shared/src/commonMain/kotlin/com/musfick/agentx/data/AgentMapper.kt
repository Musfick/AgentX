package com.musfick.agentx.data

import com.musfick.agentx.data.remote.AgentDTO
import com.musfick.agentx.domain.model.Agent
import database.AgentEntity

fun AgentEntity.toAgent(): Agent {
    return Agent(
        uuid = this.uuid,
        backgroundGradientColors = this.backgroundGradientColors?.split(",")
            ?.let { listOf(*it.toTypedArray()) },
        displayName = this.displayName,
        displayIcon = this.displayIcon,
        fullPortrait = this.fullPortrait,
        background = this.background,
        role = this.role,
        description = this.description
    )
}

fun AgentDTO.toAgentEntity():AgentEntity {
    return  AgentEntity(
        uuid = this.uuid,
        backgroundGradientColors = this.backgroundGradientColors?.joinToString(separator = ",", prefix = "", postfix = ""),
        displayName = this.displayName,
        displayIcon = this.displayIcon,
        fullPortrait = this.fullPortrait,
        background = this.background,
        role = this.role?.displayName,
        description = this.description
    )
}