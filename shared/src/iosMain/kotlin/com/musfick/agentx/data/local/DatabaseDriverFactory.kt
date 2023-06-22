package com.musfick.agentx.data.local

import com.musfick.agentx.database.AgentXDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DatabaseDriverFactory {

    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(AgentXDatabase.Schema, "agent-x.db")
    }
}