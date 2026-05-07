package com.premierfleet.drivinglicense.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.premierfleet.drivinglicense.data.model.TestResult

@Dao
interface TestResultDao {
    @Query("SELECT * FROM test_results ORDER BY timestamp DESC")
    suspend fun getAllResults(): List<TestResult>

    @Query("SELECT * FROM test_results WHERE id = :resultId")
    suspend fun getResultById(resultId: Int): TestResult?

    @Query("SELECT AVG(percentage) FROM test_results")
    suspend fun getAverageScore(): Double

    @Insert
    suspend fun insertResult(result: TestResult): Long
}
