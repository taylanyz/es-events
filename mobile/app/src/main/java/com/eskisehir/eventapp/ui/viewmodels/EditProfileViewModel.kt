package com.eskisehir.eventapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eskisehir.events.data.local.entity.UserProfileEntity
import com.eskisehir.events.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val userProfile = userRepository.getUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun saveProfile(fullName: String, email: String, profileImageUri: String?, interests: List<String>) {
        viewModelScope.launch {
            userRepository.updateProfile(
                UserProfileEntity(
                    email = email,
                    fullName = fullName,
                    profileImageUri = profileImageUri,
                    interests = interests
                )
            )
        }
    }

    fun addFavoritePlace(name: String, location: String, category: String?) {
        viewModelScope.launch {
            userRepository.addFavoritePlace(name, location, category)
        }
    }
}
