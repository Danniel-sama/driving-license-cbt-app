package com.premierfleet.drivinglicense.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.premierfleet.drivinglicense.data.model.Question

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions")
    suspend fun getAllQuestions(): List<Question>

    @Query("SELECT * FROM questions WHERE category = :category")
    suspend fun getQuestionsByCategory(category: String): List<Question>

    @Query("SELECT * FROM questions WHERE id = :questionId")
    suspend fun getQuestionById(questionId: Int): Question?

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getTotalQuestionsCount(): Int

    @Insert
    suspend fun insertQuestions(questions: List<Question>)
}
