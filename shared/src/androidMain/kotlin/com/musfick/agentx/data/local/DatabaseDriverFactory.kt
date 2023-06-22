package com.musfick.agentx.data.local

import android.content.Context
import com.musfick.agentx.database.AgentXDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory(private val context: Context) {

    actual fun createDriver():SqlDriver {
        return AndroidSqliteDriver(AgentXDatabase.Schema, context, "agent-x.db")
    }
}