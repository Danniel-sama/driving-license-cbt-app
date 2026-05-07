package com.premierfleet.drivinglicense.presentation.quiz

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.premierfleet.drivinglicense.R
import com.premierfleet.drivinglicense.databinding.ActivityQuizBinding
import com.premierfleet.drivinglicense.data.database.AppDatabase
import com.premierfleet.drivinglicense.data.repository.QuestionRepository
import com.premierfleet.drivinglicense.data.repository.TestRepository
import com.premierfleet.drivinglicense.presentation.home.HomeActivity
import com.premierfleet.drivinglicense.presentation.result.ResultActivity
import java.util.concurrent.TimeUnit

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding
    private lateinit var viewModel: QuizViewModel
    private lateinit var factory: QuizViewModelFactory
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeDatabase()
        setupViewModel()
        setupUI()
        startTimer()
    }

    private fun initializeDatabase() {
        val database = AppDatabase.getInstance(this)
        val questionRepository = QuestionRepository(database.questionDao())
        val testRepository = TestRepository(database.testResultDao())
        factory = QuizViewModelFactory(questionRepository, testRepository)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, factory).get(QuizViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.questions.observe(this) { questions ->
            if (questions.isNotEmpty()) {
                updateQuestionUI()
            }
        }

        viewModel.currentQuestionIndex.observe(this) {
            updateQuestionUI()
        }

        viewModel.timeRemaining.observe(this) { timeMs ->
            updateTimerUI(timeMs)
            if (timeMs <= 0) {
                submitTest()
            }
        }
    }

    private fun setupUI() {
        binding.nextButton.setOnClickListener {
            viewModel.nextQuestion()
        }

        binding.previousButton.setOnClickListener {
            viewModel.previousQuestion()
        }

        binding.submitButton.setOnClickListener {
            confirmSubmit()
        }

        binding.optionsRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton = findViewById<RadioButton>(checkedId)
            val answer = selectedRadioButton?.text.toString()
            val currentQuestion = viewModel.questions.value?.getOrNull(
                viewModel.currentQuestionIndex.value ?: 0
            )
            if (currentQuestion != null) {
                viewModel.selectAnswer(currentQuestion.id, answer)
            }
        }
    }

    private fun updateQuestionUI() {
        val questions = viewModel.questions.value ?: return
        val currentIndex = viewModel.currentQuestionIndex.value ?: 0

        if (currentIndex < questions.size) {
            val question = questions[currentIndex]
            binding.questionNumber.text = "Question ${currentIndex + 1} of ${questions.size}"
            binding.questionText.text = question.questionText
            binding.optionARadioButton.text = question.optionA
            binding.optionBRadioButton.text = question.optionB
            binding.optionCRadioButton.text = question.optionC
            binding.optionDRadioButton.text = question.optionD

            binding.optionsRadioGroup.clearCheck()

            binding.previousButton.visibility = if (currentIndex == 0) View.GONE else View.VISIBLE
            binding.nextButton.visibility = if (currentIndex == questions.size - 1) View.GONE else View.VISIBLE
            binding.submitButton.visibility = if (currentIndex == questions.size - 1) View.VISIBLE else View.GONE
        }
    }

    private fun updateTimerUI(timeMs: Long) {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeMs)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeMs) % 60
        binding.timerText.text = String.format("%02d:%02d", minutes, seconds)
    }

    private fun confirmSubmit() {
        AlertDialog.Builder(this)
            .setTitle("Submit Test")
            .setMessage("Are you sure you want to submit the test?")
            .setPositiveButton("Yes") { _, _ -> submitTest() }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun submitTest() {
        countDownTimer?.cancel()
        val result = viewModel.submitTest()
        viewModel.saveTestResult(result)

        startActivity(Intent(this, ResultActivity::class.java).apply {
            putExtra("score", result.correctAnswers)
            putExtra("total", result.totalQuestions)
            putExtra("percentage", result.percentage)
            putExtra("passed", result.isPassed)
        })
        finish()
    }

    private fun startTimer() {
        val initialTime = viewModel.timeRemaining.value ?: 0L
        countDownTimer = object : CountDownTimer(initialTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                viewModel.updateTimeRemaining(millisUntilFinished)
            }

            override fun onFinish() {
                viewModel.updateTimeRemaining(0)
                submitTest()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}
