package com.example.staysunny.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PermissionViewModel : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loaderState: LiveData<Boolean> get() = _loading

    private val _successFlag = MutableLiveData<Boolean>()
    val mensaje: LiveData<Boolean> get() = _successFlag

    fun triggerSuccessMessage() {
        _successFlag.value = true
    }

    fun resetSuccessMessage() {
        _successFlag.value = false
    }

    fun showLoader() {
        _loading.value = true
    }

    fun hideLoader() {
        _loading.value = false
    }
}
