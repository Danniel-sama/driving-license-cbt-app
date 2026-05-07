package com.premierfleet.drivinglicense.data.repository

import com.premierfleet.drivinglicense.data.database.QuestionDao
import com.premierfleet.drivinglicense.data.model.Question

class QuestionRepository(private val questionDao: QuestionDao) {
    suspend fun getAllQuestions(): List<Question> = questionDao.getAllQuestions()

    suspend fun getQuestionsByCategory(category: String): List<Question> =
        questionDao.getQuestionsByCategory(category)

    suspend fun getQuestionById(questionId: Int): Question? =
        questionDao.getQuestionById(questionId)

    suspend fun getTotalCount(): Int = questionDao.getTotalQuestionsCount()
}
