package com.example.alt1copy.bubble

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.example.alt1copy.MainActivity
import com.example.alt1copy.R
import com.example.alt1copy.data.SimpleMessage

class AndroidBubbleNotificationView(
    private val context: Context,
    private val notificationManager: NotificationManager,
    private val shortcutManager: ShortcutManager

) : BubbleNotification {

    companion object {
        private const val CHANNEL_NEW_BUBBLE = "new_bubble"

        private const val REQUEST_CONTENT = 1
        private const val REQUEST_BUBBLE = 2
    }

    init {
        setUpNotificationChannels()
        clearDynamicShortCuts()
    }

    override fun showNotification(simpleMessage: SimpleMessage) {
        //create person
        val icon = Icon.createWithResource(context, simpleMessage.image)
        val contentUri = createContentUri(simpleMessage.sender)


        val builder = getNotificationBuilder()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val person = Person.Builder()
                .setName(simpleMessage.sender)
                .setIcon(icon)
                .setImportant(true)
                .build()
            val bubbleData = createBubbleMetadata(contentUri, icon)

            val shortcut = createDynamicShortcut(
                simpleMessage,
                icon,
                person
            )
            addDynamicShortcut(shortcut)

            with(builder) {
                setBubbleMetadata(bubbleData)
                style = Notification.MessagingStyle(person).addMessage(
                    Notification.MessagingStyle.Message(
                        simpleMessage.text,
                        System.currentTimeMillis(),
                        person
                    )
                )
                setShortcutId(shortcut.id)
                addPerson(person)
            }


        }

        // The user can turn off the bubble in system settings. In that case, this notification
        // is shown as a normal notification instead of a bubble. Make sure that this
        // notification works as a normal notification as well.
        with(builder) {
            setContentTitle(
                context.resources.getString(
                    R.string.message_from,
                    simpleMessage.sender
                )
            )
            setSmallIcon(R.drawable.ic_stat_notification)
            setCategory(Notification.CATEGORY_MESSAGE)
            setContentIntent(
                PendingIntent.getActivity(
                    context,
                    REQUEST_CONTENT,
                    Intent(context, MainActivity::class.java)
                        .setAction(Intent.ACTION_VIEW)
                        .setData(contentUri),
                    PendingIntent.FLAG_MUTABLE
                )
            )
            setShowWhen(true)
        }


        notificationManager.notify(simpleMessage.id, builder.build())
    }

    private fun getNotificationBuilder(): Notification.Builder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, CHANNEL_NEW_BUBBLE)
        } else {
            Notification.Builder(context)
        }
    }

    private fun addDynamicShortcut(shortcut: ShortcutInfo) {
        if (atLeastAndroid11()) {
            shortcutManager.pushDynamicShortcut(shortcut)
        } else {
            shortcutManager.addDynamicShortcuts(listOf(shortcut))
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun createBubbleMetadata(
        contentUri: Uri,
        icon: Icon
    ): Notification.BubbleMetadata {
        // Create bubble intent
        val bubbleIntent =
            PendingIntent.getActivity(
                context,
                REQUEST_BUBBLE,
                // Launch BubbleActivity as the expanded bubble.
                Intent(context, BubbleActivity::class.java)
                    .setAction(Intent.ACTION_VIEW)
                    .setData(contentUri),
                PendingIntent.FLAG_MUTABLE
            )

        // Create bubble metadata
        val builder = if (atLeastAndroid11()) {
            Notification.BubbleMetadata.Builder(bubbleIntent, icon)
        } else {
            Notification.BubbleMetadata.Builder()
                .setIntent(bubbleIntent)
                .setIcon(icon)
        }
        return builder
            .setDesiredHeightResId(R.dimen.bubble_height)
            .setAutoExpandBubble(true)
            .setSuppressNotification(true)
            .build()
    }

    private fun setUpNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(CHANNEL_NEW_BUBBLE) == null) {
                val notificationChannel = NotificationChannel(
                    CHANNEL_NEW_BUBBLE,
                    context.getString(R.string.notification_channel_name_bubble),
                    NotificationManager.IMPORTANCE_HIGH
                )

                if (atLeastAndroid11()) {
                    notificationChannel.setAllowBubbles(true)
                }

                notificationManager.createNotificationChannel(notificationChannel)
            }
        }
    }

    private fun atLeastAndroid11() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

    private fun clearDynamicShortCuts() {
        shortcutManager.removeAllDynamicShortcuts()
    }

    private fun createContentUri(text: String): Uri {
        return "app://mybubble.filipebaptista.com/message/$text".toUri()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun createDynamicShortcut(
        message: SimpleMessage,
        icon: Icon,
        person: Person
    ): ShortcutInfo {
        return ShortcutInfo.Builder(context, message.id.toString())
            .setLongLived(true)
            .setIntent(
                Intent(context, MainActivity::class.java)
                    .setAction(Intent.ACTION_VIEW)
                    .setData(createContentUri(message.text))
            )
            .setShortLabel(message.sender)
            .setIcon(icon)
            .setPerson(person)
            .build()
    }
}
