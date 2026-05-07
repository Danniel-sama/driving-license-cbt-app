package com.premierfleet.drivinglicense.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.premierfleet.drivinglicense.databinding.ActivityHomeBinding
import com.premierfleet.drivinglicense.data.database.AppDatabase
import com.premierfleet.drivinglicense.data.repository.QuestionRepository
import com.premierfleet.drivinglicense.data.repository.TestRepository
import com.premierfleet.drivinglicense.presentation.quiz.QuizActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var factory: HomeViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeDatabase()
        setupViewModel()
        setupUI()
    }

    private fun initializeDatabase() {
        val database = AppDatabase.getInstance(this)
        val questionRepository = QuestionRepository(database.questionDao())
        val testRepository = TestRepository(database.testResultDao())
        factory = HomeViewModelFactory(testRepository)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setupUI() {
        binding.startTestButton.setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
        }
    }
}
