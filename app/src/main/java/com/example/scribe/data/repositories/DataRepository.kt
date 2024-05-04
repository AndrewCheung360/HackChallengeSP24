package com.example.scribe.data.repositories

import com.example.scribe.data.api.KtorApiClient

class DataRepository(private val apiClient: KtorApiClient) {
    suspend fun getCourses() = apiClient.getCourses()
    suspend fun getCourseById(id: Int) = apiClient.getCourseById(id)
    suspend fun getNotes() = apiClient.getNotes()
    suspend fun getNotesByCourseId(id: Int) = apiClient.getNotesByCourseId(id)
    suspend fun getPdfByNoteId(id: Int) = apiClient.getPdfByNoteId(id)

    suspend fun getUsers() = apiClient.getUsers()

    suspend fun getUserById(id: Int) = apiClient.getUserById(id)

}