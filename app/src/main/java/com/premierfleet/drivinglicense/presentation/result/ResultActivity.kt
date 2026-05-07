package com.premierfleet.drivinglicense.presentation.result

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.premierfleet.drivinglicense.databinding.ActivityResultBinding
import com.premierfleet.drivinglicense.presentation.home.HomeActivity
import com.premierfleet.drivinglicense.presentation.quiz.QuizActivity

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var viewModel: ResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        extractAndShowResults()
        setupUI()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(ResultViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun extractAndShowResults() {
        val score = intent.getIntExtra("score", 0)
        val total = intent.getIntExtra("total", 0)
        val percentage = intent.getIntExtra("percentage", 0)
        val passed = intent.getBooleanExtra("passed", false)

        binding.scoreText.text = "$score/$total"
        binding.percentageText.text = "$percentage%"
        binding.resultStatus.text = if (passed) "PASSED" else "FAILED"
        binding.resultStatus.setTextColor(
            if (passed) android.graphics.Color.GREEN else android.graphics.Color.RED
        )
    }

    private fun setupUI() {
        binding.retakeButton.setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
            finish()
        }

        binding.homeButton.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}
