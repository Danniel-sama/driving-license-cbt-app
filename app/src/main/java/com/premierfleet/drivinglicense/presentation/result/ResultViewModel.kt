package com.premierfleet.drivinglicense.presentation.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.premierfleet.drivinglicense.data.model.TestResult

class ResultViewModel : ViewModel() {
    private val _testResult = MutableLiveData<TestResult>()
    val testResult: LiveData<TestResult> = _testResult

    private val _resultMessage = MutableLiveData<String>()
    val resultMessage: LiveData<String> = _resultMessage

    fun setTestResult(result: TestResult) {
        _testResult.value = result
        _resultMessage.value = if (result.isPassed) {
            "Congratulations! You passed with ${result.percentage}%"
        } else {
            "You scored ${result.percentage}%. Try again!"
        }
    }
}
