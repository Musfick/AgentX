package com.musfick.agentx.state

import com.musfick.agentx.domain.model.Agent

data class MainUiState(
    val isLoading:Boolean,
    val isError:Boolean,
    val agents:List<Agent>,
    val message:String?
){

    constructor(): this (
        isLoading = true,
        isError = false,
        agents = mutableListOf(),
        message = null
    )
}