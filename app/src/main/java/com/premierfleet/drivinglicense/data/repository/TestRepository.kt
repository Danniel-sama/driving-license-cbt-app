package com.premierfleet.drivinglicense.data.repository

import com.premierfleet.drivinglicense.data.database.TestResultDao
import com.premierfleet.drivinglicense.data.model.TestResult

class TestRepository(private val testResultDao: TestResultDao) {
    suspend fun saveTestResult(result: TestResult): Long =
        testResultDao.insertResult(result)

    suspend fun getAllResults(): List<TestResult> = testResultDao.getAllResults()

    suspend fun getResultById(resultId: Int): TestResult? =
        testResultDao.getResultById(resultId)

    suspend fun getAverageScore(): Double = testResultDao.getAverageScore()
}
