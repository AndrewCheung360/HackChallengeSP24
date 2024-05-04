package com.example.scribe.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CourseData(
    val id: Int,
    val code: String,
    val name: String,
    val description: String,
    val notes: List<NoteData>,
    val students: List<UserData>

) {
}
@Serializable
data class NoteData(
    val id: Int,
    val title: String,
    val courseId: Int,
    val poster: UserData,
) {

}

@Serializable
data class UserData(
    val id: Int,
    val name: String,
    val profileImage: String,
    val supabaseId: String,
    val email: String,

) {
}