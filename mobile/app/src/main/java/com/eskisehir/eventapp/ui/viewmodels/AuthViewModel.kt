package com.eskisehir.eventapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eskisehir.events.data.local.entity.UserProfileEntity
import com.eskisehir.events.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _events = MutableSharedFlow<AuthEvent>()
    val events = _events.asSharedFlow()

    fun register(fullName: String, email: String) {
        viewModelScope.launch {
            userRepository.updateProfile(
                UserProfileEntity(
                    email = email,
                    fullName = fullName,
                    profileImageUri = null,
                    interests = emptyList()
                )
            )
            _events.emit(AuthEvent.Success)
        }
    }

    fun login(email: String) {
        viewModelScope.launch {
            val name = email.split("@")[0].replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            userRepository.updateProfile(
                UserProfileEntity(
                    email = email,
                    fullName = name,
                    profileImageUri = null,
                    interests = emptyList()
                )
            )
            _events.emit(AuthEvent.Success)
        }
    }
}

sealed class AuthEvent {
    object Success : AuthEvent()
}
