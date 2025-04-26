package com.example.pdfassignment.repository

import com.example.pdfassignment.model.localDB.entity.User
import com.example.pdfassignment.model.localDB.dao.UserDao
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao) {

    suspend fun addUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun getUser(userId:String): User?{
        return userDao.getUser(userId)
    }
    // Add other methods as needed (e.g., get user, delete user)
}