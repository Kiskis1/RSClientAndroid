package com.example.alt1copy.bubble

import android.app.Application
import android.app.NotificationManager
import android.content.pm.ShortcutManager

class BubbleApplication : Application() {
    private val bubbleNotification: BubbleNotification by lazy {
        AndroidBubbleNotificationView(
            this,
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager,
            getSystemService(SHORTCUT_SERVICE) as ShortcutManager
        )
    }

    val bubbleViewModelFactory: BubbleViewModelFactory by lazy {
        BubbleViewModelFactory(this, bubbleNotification)
    }
}
