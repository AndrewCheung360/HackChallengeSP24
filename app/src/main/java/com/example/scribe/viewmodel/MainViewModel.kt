package com.example.scribe.viewmodel

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scribe.components.home.getGradient
import com.example.scribe.models.Course
import com.example.scribe.ui.theme.BlueEnd
import com.example.scribe.ui.theme.BlueStart
import com.example.scribe.ui.theme.GreenEnd
import com.example.scribe.ui.theme.GreenStart
import com.example.scribe.ui.theme.OrangeEnd
import com.example.scribe.ui.theme.OrangeStart
import com.example.scribe.ui.theme.PurpleEnd
import com.example.scribe.ui.theme.PurpleStart
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.IDToken
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID


class MainViewModel: ViewModel() {

    val supabase = createSupabaseClient(
        supabaseUrl = "https://bhmauikukhwzlmgohfxy.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImJobWF1aWt1a2h3emxtZ29oZnh5Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTQ4MDQ3NzEsImV4cCI6MjAzMDM4MDc3MX0.pEql7E_RAHwyfyvertCkGxOPm4XPZViuk8dN2ivKG3s"
    ){
        install(Auth)
        install(Postgrest)
    }

    private val _user = MutableStateFlow<UserInfo?>(supabase.auth.currentUserOrNull())
    val user = _user.asStateFlow()

    private val _name = MutableStateFlow("John Doe")
    val name = _name.asStateFlow()

    private val _avatar = MutableStateFlow("https://pbs.twimg.com/profile_images/1699115602839764992/d3uthjYq_400x400.jpg")
    val avatar = _avatar.asStateFlow()

    //search bar instances
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _userCourses = MutableStateFlow<List<Course>>(emptyList())
    val userCourses = _userCourses.asStateFlow()

    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    private val courses = MutableStateFlow(allCourses)
    val searchCourses = searchText
            .combine(courses) { text, course ->
            if(text.isBlank()) {
                emptyList()
            } else {
                course.asSequence()
                    .map { it to it.doesSearchExist(text) }
                    .filter { it.second }
                    .sortedByDescending { it.second }
                    .take(5)
                    .map {it.first}
                    .toList()
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            courses.value
        )


    fun searchTextValue(text: String) {
        _searchText.value = text
    }

    fun getCourseList(): StateFlow<List<Course>> {
        return courses
    }

    fun addCourse(courseName: String) {
        val course = courses.value.find { it.courseName == courseName }
        if (course != null) {
            if(_userCourses.value.contains(course)) {
                _message.value = "You have already added this course"
            } else {
            _userCourses.value += course
            _message.value = "Course added successfully"
            }
        }
    }

    fun updateNameAvatar(name: String?, avatar: String?){
        _name.value = name ?: "John Doe"
        _avatar.value = avatar ?: "https://pbs.twimg.com/profile_images/1699115602839764992/d3uthjYq_400x400.jpg"
    }

    fun signIn(context: Context, coroutineScope: CoroutineScope, onSignedIn: () -> Unit){
        val credentialManager = CredentialManager.create(context)
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = java.security.MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

        val googleIdOption : GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(
                "462358544142-v1honrm2ggc03r5umi7aehkdh5snm8c4.apps.googleusercontent.com")
            .setNonce(hashedNonce)
            .build()

        val request : GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        coroutineScope.launch {
            try{
                val result = credentialManager.getCredential(
                    request = request,
                    context = context
                )
                val credential = result.credential

                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(credential.data)

                val googleIdToken = googleIdTokenCredential.idToken

                supabase.auth.signInWith(IDToken){
                    idToken = googleIdToken
                    provider = Google
                    nonce = rawNonce
                }

                Log.i(ContentValues.TAG, googleIdToken)
                _user.value = supabase.auth.currentUserOrNull()
                Log.i(ContentValues.TAG, _user.value?.userMetadata.toString())
                updateNameAvatar(_user.value?.userMetadata?.get("name").toString().replace("\"", ""), _user.value?.userMetadata?.get("picture").toString().replace("\"", ""))
                onSignedIn()
                Toast.makeText(context, "You are signed in!", Toast.LENGTH_SHORT).show()
            }catch(e : GetCredentialException){
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                Log.e(ContentValues.TAG, e.message, e)
            }catch(e : Exception){
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                Log.e(ContentValues.TAG, e.message, e)
            }

        }
    }
    fun signOut(coroutineScope: CoroutineScope, onSignOut: () -> Unit){
        coroutineScope.launch {
            supabase.auth.signOut()
            onSignOut()
        }
    }

}


val allCourses = mutableStateListOf(
    Course(
        id = 1, courseName = "Intro to Python", code = "CS 1110", semester = "Fall 2021", color = getGradient(
            PurpleStart, PurpleEnd
        )
    ),
    Course(
        id = 2, courseName = "Object-Oriented Programming", code = "CS 2110", semester = "Spring 2022", color = getGradient(
            BlueStart, BlueEnd
        )
    ),
    Course(
        id = 3, courseName = "Data Structures and Functional Programming", code = "CS 3110", semester = "Fall 2022", color = getGradient(
            OrangeStart, OrangeEnd
        )
    ),
    Course(
        id = 4, courseName = "Computer Organization", code = "CS 3410", semester = "Spring 2023", color = getGradient(
            GreenStart, GreenEnd
        )
    ),
    Course(
        id = 5,
        courseName = "Introduction to Analysis of Algorithms",
        code = "CS 4820",
        semester = "Spring 2023",
        color = getGradient(BlueStart, BlueEnd)
    ),
    Course(
        id = 6,
        courseName = "Introduction to Machine Learning",
        code = "CS 4780",
        semester = "Spring 2023",
        color = getGradient(OrangeStart, OrangeEnd)
    ),
    Course(
        id = 7,
        courseName = "Introduction to Computer Vision",
        code = "CS 4670",
        semester = "Spring 2023",
        color = getGradient(GreenStart, GreenEnd)
    ),
    Course(
        id = 8,
        courseName = "Introduction to Natural Language Processing",
        code = "CS 4740",
        semester = "Spring 2023",
        color = getGradient(PurpleStart, PurpleEnd)
    ),
)