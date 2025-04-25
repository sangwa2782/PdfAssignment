package com.example.pdfassignment.di

import android.content.Context
import androidx.room.Room
import com.example.pdfassignment.model.localDB.UserDao
import com.example.pdfassignment.model.localDB.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserDatabase(@ApplicationContext context: Context): UserDatabase {
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "user_database"
        ).fallbackToDestructiveMigration().build()// Add a fallback
    }


    @Provides
    fun provideUserDao(userDatabase: UserDatabase): UserDao {
        return userDatabase.userDao()
    }



}