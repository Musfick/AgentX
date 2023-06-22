package com.musfick.agentx.android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musfick.agentx.domain.user_case.GetAllAgentsUseCase
import com.musfick.agentx.state.MainUiState
import com.musfick.agentx.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AgentListScreenViewModel constructor(
    private val getAllAgentsUseCase: GetAllAgentsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        loadAgentList()
    }

    fun loadAgentList() {
        viewModelScope.launch {
            getAllAgentsUseCase().flowOn(Dispatchers.IO).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoading = true,
                                isError = false,
                                agents = it.data?.items ?: emptyList()
                            )
                        }
                    }

                    is Resource.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                agents = it.data?.items ?: emptyList()
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                isError = true,
                                agents = it.data?.items ?: emptyList(),
                                message = it.message
                            )
                        }
                    }
                }
            }
        }
    }
}
