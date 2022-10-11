package com.example.alt1copy.bubble

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow
import com.example.alt1copy.DetailFragment
import com.example.alt1copy.R

class BubbleActivity : AppCompatActivity(R.layout.activity_bubble) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val message = intent.data?.lastPathSegment ?: return

        if (savedInstanceState == null) {
            supportFragmentManager.commitNow {
                replace(R.id.container, DetailFragment.newInstance(message))
            }
        }
    }
}
