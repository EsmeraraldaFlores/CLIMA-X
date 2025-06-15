package com.example.staysunny.viewModel

import androidx.lifecycle.*
import com.example.staysunny.core.ResultWrapper
import com.example.staysunny.model.User
import com.example.staysunny.network.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PersonalInfoViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loaderState: LiveData<Boolean> get() = _loading

    private val _updateSuccess = MutableLiveData<Boolean>()
    val operationSuccess: LiveData<Boolean> get() = _updateSuccess

    fun updateUserInfo(name: String, lastName: String, userName: String, bornDate: Date) {
        _loading.value = true

        val uid = auth.currentUser?.uid.orEmpty()
        val updatedUser = User(uid, name, lastName, userName, bornDate)

        viewModelScope.launch {
            val result = userRepo.updateUser(updatedUser)

            _loading.value = false
            _updateSuccess.value = result is ResultWrapper.Success
        }
    }
}

