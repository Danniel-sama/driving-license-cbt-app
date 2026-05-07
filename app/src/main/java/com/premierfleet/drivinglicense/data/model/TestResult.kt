package com.premierfleet.drivinglicense.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "test_results")
data class TestResult(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val percentage: Int,
    val isPassed: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val duration: Long = 0
) {
    fun getScore(): String = "$correctAnswers/$totalQuestions"
}
