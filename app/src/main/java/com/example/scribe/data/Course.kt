package com.example.scribe.data

import androidx.compose.ui.graphics.Brush


data class Course(
    val id: Int,
    val courseName: String,
    val code: String,
    val semester: String,
    val color: Brush,
) {
    fun doesSearchExist(query: String): Boolean {
        val combinations = listOf(
            courseName,
        )

        return combinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}