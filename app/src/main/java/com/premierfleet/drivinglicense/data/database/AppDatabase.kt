package com.premierfleet.drivinglicense.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.premierfleet.drivinglicense.data.model.Question
import com.premierfleet.drivinglicense.data.model.TestResult

@Database(entities = [Question::class, TestResult::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun testResultDao(): TestResultDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "driving_license_db"
                ).createFromAsset("database/questions.db").build()
                INSTANCE = instance
                instance
            }
        }
    }
}
