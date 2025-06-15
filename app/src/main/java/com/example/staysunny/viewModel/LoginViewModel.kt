package com.example.staysunny.viewModel

import androidx.lifecycle.*
import com.example.staysunny.core.ResultWrapper
import com.example.staysunny.network.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loaderState = MutableLiveData(false)
    val loaderState: LiveData<Boolean> get() = _loaderState

    private val _sessionValid = MutableLiveData<Boolean>()
    val sessionValid: LiveData<Boolean> get() = _sessionValid

    fun requestLogin(email: String, password: String) {
        _loaderState.value = true

        viewModelScope.launch {
            val result = userRepository.login(email, password)

            _loaderState.postValue(false)
            _sessionValid.postValue(result is ResultWrapper.Success)
        }
    }
}
