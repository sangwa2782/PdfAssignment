package com.example.pdfassignment.model.localDB.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pdfassignment.model.localDB.dao.ProductDao
import com.example.pdfassignment.model.localDB.entity.ProductEntity
import com.example.pdfassignment.utils.Converters

@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}