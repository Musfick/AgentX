package com.musfick.agentx

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform