package com.example.staysunny.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.example.staysunny.core.ResultWrapper
import com.example.staysunny.model.User
import com.example.staysunny.network.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PersonalInformationVariantViewModel @Inject constructor(
    private val userRepo: UserRepository
) : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loaderState: LiveData<Boolean> get() = _loading

    private val _userCreated = MutableLiveData<Boolean>()
    val operationSuccess: LiveData<Boolean> get() = _userCreated

    fun createUserInfo(
        userId: String,
        name: String,
        lastName: String,
        userName: String,
        bornDate: Date
    ) {
        _loading.value = true

        val user = User(
            id = userId,
            name = name,
            lastName = lastName,
            userName = userName,
            bornDate = bornDate
        )

        viewModelScope.launch {
            val result = userRepo.createUser(user)

            when (result) {
                is ResultWrapper.Success -> {
                    Log.d("CreateUser", "Usuario registrado correctamente.")
                    _userCreated.postValue(true)
                }
                is ResultWrapper.Error -> {
                    Log.e("CreateUser", "Error: ${result.exception.message}")
                    _userCreated.postValue(false)
                }
            }

            _loading.postValue(false)
        }
    }
}
