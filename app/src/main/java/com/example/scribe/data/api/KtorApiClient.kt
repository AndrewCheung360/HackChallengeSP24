package com.example.scribe.data.api

import com.example.scribe.data.model.CourseData
import com.example.scribe.data.model.NoteData
import com.example.scribe.data.model.UserData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


class KtorApiClient {
    private val BASE_URL = "http://34.48.67.43"

    val client = HttpClient(CIO) {
        install(ContentNegotiation){
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        install(Logging) {
            level = LogLevel.ALL
        }
    }

    suspend fun getCourses() : List<CourseData> {
        return client.get("$BASE_URL/courses").body()
    }

    suspend fun getCourseById(id: Int) : CourseData {
        return client.get("$BASE_URL/courses/$id").body()
    }

    suspend fun  getNotes() : List<NoteData> {
        return client.get("$BASE_URL/notes").body()
    }
    suspend fun getNotesByCourseId(id: Int) : List<String> {
        return client.get("$BASE_URL/notes/json/$id").body()
    }

    suspend fun getPdfByNoteId(id: Int) : HttpResponse {
        return client.get("$BASE_URL/notes/$id")
    }

    suspend fun getUsers() : List<UserData>{
        return client.get("$BASE_URL/users").body()
    }

    suspend fun getUserById(id: Int) {
        return client.get("$BASE_URL/users/$id").body()
    }
    
}