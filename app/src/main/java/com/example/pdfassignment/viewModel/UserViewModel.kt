package com.example.pdfassignment.viewModel

import androidx.activity.result.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pdfassignment.model.localDB.User
import com.example.pdfassignment.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _userFromDb = MutableLiveData<User?>()
    val userFromDb: LiveData<User?> = _userFromDb

    fun addUser(user: User) {
        viewModelScope.launch {
            userRepository.addUser(user)
        }
    }
//    fun getUser(userId:String) {
//        viewModelScope.launch {
//            userRepository.getUser(userId)
//            _userFromDb.postValue(user)
//        }
//    }

    fun getUser(userId: String) {
        viewModelScope.launch {
            val user = userRepository.getUser(userId)
            _userFromDb.postValue(user)
        }
    }
}