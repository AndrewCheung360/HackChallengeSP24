package com.example.scribe.models

import androidx.compose.ui.graphics.Color
import com.example.scribe.ui.theme.BabyBlue
import com.example.scribe.ui.theme.LightGreen
import com.example.scribe.ui.theme.RedOrange
import com.example.scribe.ui.theme.RedPink
import com.example.scribe.ui.theme.Violet

data class Note(
    val id: Int,
    val title: String,
    val content: String,
    val color: Color
)

