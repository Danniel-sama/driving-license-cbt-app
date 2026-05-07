package com.premierfleet.drivinglicense.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.premierfleet.drivinglicense.data.repository.TestRepository

class HomeViewModelFactory(private val testRepository: TestRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return HomeViewModel(testRepository) as T
    }
}
