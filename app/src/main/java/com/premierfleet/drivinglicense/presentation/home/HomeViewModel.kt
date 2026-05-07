package com.premierfleet.drivinglicense.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.premierfleet.drivinglicense.data.model.TestResult
import com.premierfleet.drivinglicense.data.repository.TestRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val testRepository: TestRepository) : ViewModel() {

    private val _pastResults = MutableLiveData<List<TestResult>>()
    val pastResults: LiveData<List<TestResult>> = _pastResults

    private val _averageScore = MutableLiveData(0.0)
    val averageScore: LiveData<Double> = _averageScore

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadPastResults()
    }

    fun loadPastResults() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val results = testRepository.getAllResults()
                _pastResults.value = results
                val average = testRepository.getAverageScore()
                _averageScore.value = if (average.isNaN()) 0.0 else average
            } finally {
                _isLoading.value = false
            }
        }
    }
}
