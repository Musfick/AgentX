package com.musfick.agentx.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.musfick.agentx.android.theme.AgentXTheme
import com.musfick.agentx.android.ui.AgentListScreen
import com.musfick.agentx.android.ui.AgentListScreenViewModel
import com.musfick.agentx.data.local.DatabaseDriverFactory
import com.musfick.agentx.domain.user_case.GetAllAgentsUseCase

class MainActivity : ComponentActivity() {


    //There are lots of way to improve the Compose implementation
    //I am a native android developer with almost 3 years experience
    //learning...learning...learning...


    //creating databaseDriverFactory
    //creating useCase
    private val getAllAgentsUseCase = GetAllAgentsUseCase(DatabaseDriverFactory(this))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AgentXTheme(
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {

                    //we are menually injecting usecase
                    //this can be done with koin(Dependency Injection framwork)
                    AgentListScreen(viewModel = AgentListScreenViewModel(getAllAgentsUseCase))
                }
            }
        }
    }
}