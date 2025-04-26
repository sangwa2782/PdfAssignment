package com.example.pdfassignment.model.localDB.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pdfassignment.model.localDB.entity.User
import com.example.pdfassignment.model.localDB.dao.UserDao


@Database(
    entities = [User::class],
    version = 2, // Increment version when schema changes
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
//    abstract fun apiDataDao(): ApiDataDao
//
//    abstract fun imageDao(): ImageDao
}