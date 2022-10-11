package com.example.alt1copy.data

import androidx.annotation.DrawableRes

data class SimpleMessage(
    val id: Int,
    val sender: String,
    val text: String,
    @DrawableRes val image: Int
)
