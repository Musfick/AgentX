package com.musfick.agentx

import com.musfick.agentx.utils.ListWrapper
import com.musfick.agentx.utils.Resource
import io.ktor.utils.io.core.Closeable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun <T> Flow<T>.asCommonFlow(): CommonFlow<T> = CommonFlow(this)
class CommonFlow<T>(private val origin: Flow<T>) : Flow<T> by origin {
    fun watch(block: (T) -> Unit): Closeable {
        val job = Job()

        onEach {
            block(it)
        }.launchIn(CoroutineScope(Dispatchers.Main + job))

        return object : Closeable {
            override fun close() {
                job.cancel()
            }
        }
    }
}

fun <T>Flow<Resource<List<T>>>.asListWrapper():Flow<Resource<ListWrapper<T>>> = this
    .map {
        when(it){
            is Resource.Loading -> Resource.Loading(data = ListWrapper(it.data))
            is Resource.Success -> Resource.Success(data = ListWrapper(it.data))
            is Resource.Error -> Resource.Error(data = ListWrapper(it.data), message = it.message)
        }
    }