package com.example.staysunny.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.staysunny.core.ResultWrapper
import com.example.staysunny.network.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
private val repository: UserRepository
): ViewModel() {
    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean>
        get() = _loaderState

    private val _createdUser = MutableLiveData<String>()
    val createdUser: LiveData<String>
        get() = _createdUser


    fun requestRegister(email: String, password: String) {
            _loaderState.value = true
            viewModelScope.launch {
                when (val result = repository.requestRegister(email, password)) {
                    is ResultWrapper.Success -> {
                        _loaderState.value = false
                        _createdUser.value = result.data.uid
                    }
                    is ResultWrapper.Error -> {
                        _loaderState.value = false
                        Log.e("Firebase", "Ocurrio un problema")
                    }
                }
            }
        }
}