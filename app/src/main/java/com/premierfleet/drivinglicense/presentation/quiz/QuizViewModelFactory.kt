package com.premierfleet.drivinglicense.presentation.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.premierfleet.drivinglicense.data.repository.QuestionRepository
import com.premierfleet.drivinglicense.data.repository.TestRepository

class QuizViewModelFactory(
    private val questionRepository: QuestionRepository,
    private val testRepository: TestRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return QuizViewModel(questionRepository, testRepository) as T
    }
}
