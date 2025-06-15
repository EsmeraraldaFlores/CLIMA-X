package com.example.staysunny.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _loading = MutableLiveData(false)
    val loaderState: LiveData<Boolean> get() = _loading

    private val _resetSuccess = MutableLiveData<Boolean>()
    val passwordResetState: LiveData<Boolean> get() = _resetSuccess

    fun sendPasswordResetEmail(email: String) {
        _loading.value = true

        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                _loading.value = false
                _resetSuccess.value = task.isSuccessful
            }
            .addOnFailureListener {
                _loading.value = false
                _resetSuccess.value = false
            }
    }
}

