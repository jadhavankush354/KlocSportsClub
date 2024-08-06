package com.firstapplication.file.userAuthentication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firstapplication.file.userAuthentication.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: AuthRepository

): ViewModel() {
//  var currentUser: FirebaseUser = repo.currentUser!!

    init {
        getAuthState()
    }

    fun getAuthState() = repo.getAuthState(viewModelScope)

    val isEmailVerified get() = repo.currentUser?.isEmailVerified ?: false
    val currentUserEmail get() = repo.currentUser?.email ?: null
}