package com.dflow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dflow.api.dto.ItemResponse
import com.dflow.repository.MainRepository
import com.dflow.utils.ApiResult
import com.dflow.utils.UiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    private val _res = MutableLiveData<ItemResponse?>()
    val res: LiveData<ItemResponse?>
        get() = _res

    private val _resError = MutableLiveData<Boolean>()
    val resError: LiveData<Boolean>
        get() = _resError

    private val _uiState = MutableStateFlow<UiResult<ItemResponse>>(UiResult.Loading())
    val uiState: StateFlow<UiResult<ItemResponse>>
        get() = _uiState

    private val _uiStateShared = MutableSharedFlow<UiResult<ItemResponse>>()
    val uiStateShared: MutableSharedFlow<UiResult<ItemResponse>>
        get() = _uiStateShared


    fun findRandomItem() = viewModelScope.launch {
        when (val d = mainRepository.getRandomItem()) {
            is ApiResult.Loading -> _resError.value = false
            is ApiResult.Error -> _resError.value = true
            is ApiResult.Success -> { _res.value = d.data }
        }
    }

    fun findMultipleRandomItem() = viewModelScope.launch {

        val deferreds = listOf(
            async { mainRepository.getRandomItem() },
            async { mainRepository.getRandomItem() },
            async { mainRepository.getRandomItem() }
        )

        val d = deferreds.awaitAll()
        d.forEach {
            delay(1000)
            when (it) {
                is ApiResult.Loading -> _resError.value = false
                is ApiResult.Error -> _resError.value = true
                is ApiResult.Success -> {
                    _res.value = it.data
                }
            }
        }
    }

    fun findRandomItemWithFlow() = viewModelScope.launch {

        mainRepository.getRandomItemWithFlow().collect {
                when (it) {
                    is ApiResult.Error -> _resError.value = true
                    is ApiResult.Success -> { _res.value = it.data }
                    is ApiResult.Loading -> _resError.value = false
                }
            }
    }

    fun findMultipleRandomItemWithStateFlow() = viewModelScope.launch {

        flowOf(
            mainRepository.getRandomItem(),
            mainRepository.getRandomItem(),
            mainRepository.getRandomItem()
        ).flowOn(Dispatchers.IO).collect {
            withContext(Dispatchers.IO) { delay(1000) }
            withContext(Dispatchers.Main) {
                when (it) {
                    is ApiResult.Loading -> _uiState.value = UiResult.Loading()
                    is ApiResult.Error -> _uiState.value = UiResult.Error()
                    is ApiResult.Success -> { _uiState.value = UiResult.Success(it.data) }
                }
            }
        }
    }


    fun findMultipleRandomItemWithSharedFlow() = viewModelScope.launch {

        val f = flowOf(
            mainRepository.getRandomItem(),
            mainRepository.getRandomItem(),
            mainRepository.getRandomItem()
        )
        f.flowOn(Dispatchers.IO).collect {
            withContext(Dispatchers.IO) { delay(1000) }
            withContext(Dispatchers.Main) {
                when (it) {
                    is ApiResult.Loading -> uiStateShared.emit(UiResult.Loading())
                    is ApiResult.Error -> _uiStateShared.emit(UiResult.Error())
                    is ApiResult.Success -> { _uiStateShared.emit(UiResult.Success(it.data)) }
                }
            }
        }
    }
}

