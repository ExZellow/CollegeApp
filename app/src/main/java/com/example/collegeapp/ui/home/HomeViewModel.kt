package com.example.collegeapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    init {
        Log.i("HomeViewModel", "HomeVM created")
    }

    private val _text = MutableLiveData<String>().apply {
        value = "Фрагмент с главной страницей"
    }
    val text: LiveData<String> = _text

    override fun onCleared() {
        super.onCleared()
        Log.i("HomeViewModel", "HomeVM destroyed")
    }
}