package com.example.alt1copy

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.alt1copy.bubble.BubbleNotification
import com.example.alt1copy.data.SimpleMessage

class MainViewModel(
    application: Application,
    private val bubbleNotification: BubbleNotification
) : AndroidViewModel(application) {

    fun showBubbleNotification() {
        bubbleNotification.showNotification(getRandomMessage())
    }

    private fun getRandomMessage(): SimpleMessage {
        return SimpleMessage(100, "James", "Hello", R.drawable.ic_base_person)
    }
}
