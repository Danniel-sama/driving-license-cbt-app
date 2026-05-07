package com.premierfleet.drivinglicense.presentation.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.premierfleet.drivinglicense.data.model.Question
import com.premierfleet.drivinglicense.data.model.TestResult
import com.premierfleet.drivinglicense.data.model.UserAnswer
import com.premierfleet.drivinglicense.data.repository.QuestionRepository
import com.premierfleet.drivinglicense.data.repository.TestRepository
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class QuizViewModel(
    private val questionRepository: QuestionRepository,
    private val testRepository: TestRepository
) : ViewModel() {

    private val _questions = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>> = _questions

    private val _currentQuestionIndex = MutableLiveData(0)
    val currentQuestionIndex: LiveData<Int> = _currentQuestionIndex

    private val _userAnswers = MutableLiveData<MutableList<UserAnswer>>(mutableListOf())
    val userAnswers: LiveData<MutableList<UserAnswer>> = _userAnswers

    private val _timeRemaining = MutableLiveData(0L)
    val timeRemaining: LiveData<Long> = _timeRemaining

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _testStartTime = System.currentTimeMillis()

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            try {
                val questionsList = questionRepository.getAllQuestions()
                _questions.value = questionsList
                _isLoading.value = false
                val totalQuestions = questionsList.size
                val totalSeconds = totalQuestions * 30L // 30 seconds per question
                _timeRemaining.value = totalSeconds * 1000
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }

    fun nextQuestion() {
        val currentIndex = _currentQuestionIndex.value ?: 0
        val totalQuestions = _questions.value?.size ?: 0
        if (currentIndex < totalQuestions - 1) {
            _currentQuestionIndex.value = currentIndex + 1
        }
    }

    fun previousQuestion() {
        val currentIndex = _currentQuestionIndex.value ?: 0
        if (currentIndex > 0) {
            _currentQuestionIndex.value = currentIndex - 1
        }
    }

    fun selectAnswer(questionId: Int, selectedAnswer: String) {
        val answers = _userAnswers.value ?: mutableListOf()
        val question = _questions.value?.find { it.id == questionId }
        val isCorrect = selectedAnswer == question?.correctAnswer
        
        answers.removeAll { it.questionId == questionId }
        answers.add(UserAnswer(questionId, selectedAnswer, isCorrect))
        _userAnswers.value = answers
    }

    fun submitTest(): TestResult {
        val questions = _questions.value ?: emptyList()
        val answers = _userAnswers.value ?: mutableListOf()
        val correctCount = answers.count { it.isCorrect }
        val percentage = if (questions.isNotEmpty()) {
            ((correctCount.toFloat() / questions.size) * 100).roundToInt()
        } else {
            0
        }
        val isPassed = percentage >= 75
        val duration = System.currentTimeMillis() - _testStartTime

        return TestResult(
            totalQuestions = questions.size,
            correctAnswers = correctCount,
            percentage = percentage,
            isPassed = isPassed,
            duration = duration
        )
    }

    fun saveTestResult(result: TestResult) {
        viewModelScope.launch {
            testRepository.saveTestResult(result)
        }
    }

    fun updateTimeRemaining(millisRemaining: Long) {
        _timeRemaining.value = millisRemaining
    }
}
