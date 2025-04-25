package com.example.pdfassignment.model.localDB

import androidx.room.Database
import androidx.room.RoomDatabase


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