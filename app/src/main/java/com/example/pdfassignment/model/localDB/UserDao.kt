package com.example.pdfassignment.model.localDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user_table WHERE user_id = :userId")
    suspend fun getUser(userId:String): User?

    // Add other database operations as needed (e.g., query, delete)
}