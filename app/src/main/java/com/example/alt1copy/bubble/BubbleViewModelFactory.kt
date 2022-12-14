package com.example.alt1copy.bubble

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alt1copy.MainViewModel

class BubbleViewModelFactory(
    private val application: Application,
    private val bubbleNotification: BubbleNotification
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application, bubbleNotification) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
