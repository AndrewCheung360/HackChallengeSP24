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
import com.example.scribe.models.Note
import com.example.scribe.ui.theme.BabyBlue
import com.example.scribe.ui.theme.BlueEnd
import com.example.scribe.ui.theme.BlueStart
import com.example.scribe.ui.theme.GreenEnd
import com.example.scribe.ui.theme.GreenStart
import com.example.scribe.ui.theme.LightGreen
import com.example.scribe.ui.theme.OrangeEnd
import com.example.scribe.ui.theme.OrangeStart
import com.example.scribe.ui.theme.PurpleEnd
import com.example.scribe.ui.theme.PurpleStart
import com.example.scribe.ui.theme.RedOrange
import com.example.scribe.ui.theme.RedPink
import com.example.scribe.ui.theme.Violet
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
            _userCourses.value += course
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

    fun getCourse(courseId: Int): Course {
        return allCourses.find { it.id == courseId } ?: Course(
            id = 0,
            courseName = "Course Not Found",
            code = "CS 0000",
            semester = "Fall 2021",
            color = getGradient(
                BabyBlue, RedOrange
            ),
            notes = mutableListOf(
                Note(
                    id = 0,
                    title = "Note Not Found",
                    content = "This note does not exist.",
                    color = Violet
                )
            )
        )

    }

}


val allCourses = mutableStateListOf(
    Course(
        id = 1, courseName = "Intro to Python", code = "CS 1110", semester = "Fall 2021", color = getGradient(
            PurpleStart, PurpleEnd
        ), notes = mutableListOf(
            Note(
                id = 1,
                title = "Python Basics",
                content = "Python is a high-level, interpreted programming language. It is known for its simplicity and readability. Python is a versatile language that can be used for web development, data analysis, artificial intelligence, and more.",
                color = BabyBlue
            ),
            Note(
                id = 2,
                title = "Data Types",
                content = "Python has several built-in data types, including integers, floats, strings, lists, tuples, and dictionaries. Each data type has its own set of operations and methods.",
                color = RedOrange
            ),
            Note(
                id = 3,
                title = "Control Structures",
                content = "Python supports several control structures, including if statements, for loops, while loops, and functions. These structures allow you to control the flow of your program and make decisions based on conditions.",
                color = Violet
            ),
            Note(
                id = 4,
                title = "Functions",
                content = "Functions are reusable blocks of code that perform a specific task. In Python, you can define your own functions using the def keyword. Functions can take parameters and return values.",
                color = LightGreen
            ),
            Note(
                id = 5,
                title = "Modules",
                content = "Modules are reusable pieces of code that can be imported into your program. Python has a large standard library that includes modules for common tasks, such as file I/O, networking, and math operations.",
                color = RedPink
            )
        )
    ),
    Course(
        id = 2, courseName = "Object-Oriented Programming", code = "CS 2110", semester = "Spring 2022", color = getGradient(
            BlueStart, BlueEnd
        ), notes = mutableListOf(
            Note(
                id = 1,
                title = "Classes and Objects",
                content = "Object-oriented programming is a programming paradigm that uses objects to model real-world entities. In Python, classes are used to define objects, which can have attributes and methods.",
                color = BabyBlue
            ),
            Note(
                id = 2,
                title = "Inheritance",
                content = "Inheritance is a mechanism that allows you to create a new class based on an existing class. The new class inherits the attributes and methods of the existing class and can add its own attributes and methods.",
                color = RedOrange
            ),
            Note(
                id = 3,
                title = "Polymorphism",
                content = "Polymorphism is the ability of an object to take on multiple forms. In Python, polymorphism is achieved through method overriding and method overloading.",
                color = Violet
            ),
            Note(
                id = 4,
                title = "Encapsulation",
                content = "Encapsulation is the bundling of data and methods that operate on that data into a single unit. In Python, encapsulation is achieved through classes and objects.",
                color = LightGreen
            ),
            Note(
                id = 5,
                title = "Abstraction",
                content = "Abstraction is the process of hiding the implementation details of an object and showing only the essential features. In Python, abstraction is achieved through abstract classes and interfaces.",
                color = RedPink
            )
        )
    ),
    Course(
        id = 3, courseName = "Data Structures and Functional Programming", code = "CS 3110", semester = "Fall 2022", color = getGradient(
            OrangeStart, OrangeEnd
        ), notes = mutableListOf(
            Note(
                id = 1,
                title = "Linked Lists",
                content = "A linked list is a data structure that consists of a sequence of elements, where each element points to the next element in the sequence. Linked lists can be singly linked, doubly linked, or circular.",
                color = BabyBlue
            ),
            Note(
                id = 2,
                title = "Stacks and Queues",
                content = "Stacks and queues are abstract data types that represent collections of elements. Stacks use a last-in, first-out (LIFO) ordering, while queues use a first-in, first-out (FIFO) ordering.",
                color = RedOrange
            ),
            Note(
                id = 3,
                title = "Trees",
                content = "A tree is a hierarchical data structure that consists of nodes connected by edges. Trees are used to represent hierarchical relationships, such as family trees, file systems, and organization charts.",
                color = Violet
            ),
            Note(
                id = 4,
                title = "Functional Programming",
                content = "Functional programming is a programming paradigm that treats computation as the evaluation of mathematical functions. In functional programming, functions are first-class citizens and can be passed as arguments to other functions.",
                color = LightGreen
            ),
            Note(
                id = 5,
                title = "Recursion",
                content = "Recursion is a programming technique where a function calls itself to solve a smaller instance of the same problem. Recursion is often used to solve problems that can be broken down into smaller subproblems.",
                color = RedPink
            )
        )
    ),
    Course(
        id = 4, courseName = "Computer Organization", code = "CS 3410", semester = "Spring 2023", color = getGradient(
            GreenStart, GreenEnd
        ), notes = mutableListOf(
            Note(
                id = 1,
                title = "Digital Logic",
                content = "Digital logic is the foundation of computer systems and electronic devices. It involves the design and analysis of circuits that use binary signals to represent information.",
                color = BabyBlue
            ),
            Note(
                id = 2,
                title = "Machine Language",
                content = "Machine language is the lowest-level programming language that a computer can understand. It consists of binary code that is directly executed by the computer's hardware.",
                color = RedOrange
            ),
            Note(
                id = 3,
                title = "Assembly Language",
                content = "Assembly language is a low-level programming language that is closely related to machine language. It uses mnemonic codes to represent machine instructions and is easier for humans to read and write than machine code.",
                color = Violet
            ),
            Note(
                id = 4,
                title = "Memory Hierarchy",
                content = "The memory hierarchy is a system of storage devices that store data at different speeds and capacities. It includes registers, cache memory, main memory, and secondary storage.",
                color = LightGreen
            ),
            Note(
                id = 5,
                title = "Input/Output",
                content = "Input/output (I/O) is the process of transferring data between a computer and external devices, such as keyboards, monitors, and printers. I/O devices are connected to the computer through interfaces, such as USB and HDMI.",
                color = RedPink
            )
        )
    ),
    Course(
        id = 5,
        courseName = "Introduction to Analysis of Algorithms",
        code = "CS 4820",
        semester = "Spring 2023",
        color = getGradient(BlueStart, BlueEnd),
        notes = mutableListOf(
            Note(
                id = 1,
                title = "Algorithm Analysis",
                content = "Algorithm analysis is the process of evaluating the efficiency of an algorithm in terms of time and space complexity. It involves analyzing the running time and memory usage of an algorithm as a function of the input size.",
                color = BabyBlue
            ),
            Note(
                id = 2,
                title = "Divide and Conquer",
                content = "Divide and conquer is a problem-solving technique that involves breaking a problem into smaller subproblems, solving the subproblems recursively, and combining the solutions to solve the original problem. It is used to solve problems that can be divided into independent subproblems.",
                color = RedOrange
            ),
            Note(
                id = 3,
                title = "Dynamic Programming",
                content = "Dynamic programming is a problem-solving technique that involves breaking a problem into smaller subproblems, solving the subproblems recursively, and storing the solutions in a table to avoid redundant computations. It is used to solve problems that have overlapping subproblems.",
                color = Violet
            ),
            Note(
                id = 4,
                title = "Greedy Algorithms",
                content = "Greedy algorithms are problem-solving techniques that make a series of choices that are locally optimal at each step, with the hope of finding a globally optimal solution. Greedy algorithms are used to solve optimization problems that can be solved by making a sequence of choices.",
                color = LightGreen
            ),
            Note(
                id = 5,
                title = "Graph Algorithms",
                content = "Graph algorithms are algorithms that operate on graphs, which are mathematical structures that represent relationships between pairs of objects. Graph algorithms are used to solve problems that involve networks, such as finding the shortest path between two nodes or detecting cycles in a graph.",
                color = RedPink
            )
        )
    ),
    Course(
        id = 6,
        courseName = "Introduction to Machine Learning",
        code = "CS 4780",
        semester = "Spring 2023",
        color = getGradient(OrangeStart, OrangeEnd),
        notes = mutableListOf(
            Note(
                id = 1,
                title = "Supervised Learning",
                content = "Supervised learning is a machine learning technique that involves training a model on labeled data to make predictions on new, unseen data. It is used to solve problems that involve predicting a target variable based on input features.",
                color = BabyBlue
            ),
            Note(
                id = 2,
                title = "Unsupervised Learning",
                content = "Unsupervised learning is a machine learning technique that involves training a model on unlabeled data to discover patterns and relationships in the data. It is used to solve problems that involve clustering, dimensionality reduction, and anomaly detection.",
                color = RedOrange
            ),
            Note(
                id = 3,
                title = "Reinforcement Learning",
                content = "Reinforcement learning is a machine learning technique that involves training a model to make a sequence of decisions in an environment to maximize a reward. It is used to solve problems that involve decision-making and control.",
                color = Violet
            ),
            Note(
                id = 4,
                title = "Neural Networks",
                content = "Neural networks are a class of machine learning models that are inspired by the structure and function of the human brain. They consist of layers of interconnected nodes that process input data and produce output predictions.",
                color = LightGreen
            ),
            Note(
                id = 5,
                title = "Deep Learning",
                content = "Deep learning is a subfield of machine learning that uses neural networks with multiple layers to learn complex patterns in data. It is used to solve problems that involve image recognition, speech recognition, and natural language processing.",
                color = RedPink
            )
        )
    ),
    Course(
        id = 7,
        courseName = "Introduction to Computer Vision",
        code = "CS 4670",
        semester = "Spring 2023",
        color = getGradient(GreenStart, GreenEnd),
        notes = mutableListOf(
            Note(
                id = 1,
                title = "Image Processing",
                content = "Image processing is the analysis and manipulation of digital images to extract information or enhance their visual appearance. It involves techniques such as filtering, edge detection, and image segmentation.",
                color = BabyBlue
            ),
            Note(
                id = 2,
                title = "Feature Extraction",
                content = "Feature extraction is the process of identifying and extracting meaningful information from raw data. In computer vision, feature extraction is used to identify key points, edges, and textures in an image.",
                color = RedOrange
            ),
            Note(
                id = 3,
                title = "Object Detection",
                content = "Object detection is the process of identifying and locating objects in an image or video. It involves techniques such as sliding window detection, region-based detection, and deep learning-based detection.",
                color = Violet
            ),
            Note(
                id = 4,
                title = "Image Classification",
                content = "Image classification is the process of categorizing an image into one of several predefined classes. It is used to solve problems such as identifying objects in an image, detecting anomalies, and recognizing patterns.",
                color = LightGreen
            ),
            Note(
                id = 5,
                title = "Image Segmentation",
                content = "Image segmentation is the process of partitioning an image into multiple segments to simplify its representation. It is used to separate objects from the background, identify regions of interest, and extract features for analysis.",
                color = RedPink
            )
        )
    ),
    Course(
        id = 8,
        courseName = "Introduction to Natural Language Processing",
        code = "CS 4740",
        semester = "Spring 2023",
        color = getGradient(PurpleStart, PurpleEnd),
        notes = mutableListOf(
            Note(
                id = 1,
                title = "Text Preprocessing",
                content = "Text preprocessing is the process of cleaning and preparing text data for analysis. It involves tasks such as tokenization, stopword removal, stemming, and lemmatization.",
                color = BabyBlue
            ),
            Note(
                id = 2,
                title = "Text Classification",
                content = "Text classification is the process of categorizing text documents into predefined classes. It is used to solve problems such as sentiment analysis, spam detection, and topic modeling.",
                color = RedOrange
            ),
            Note(
                id = 3,
                title = "Named Entity Recognition",
                content = "Named entity recognition is the process of identifying and classifying named entities in text data. It is used to extract information such as names, dates, locations, and organizations from unstructured text.",
                color = Violet
            ),
            Note(
                id = 4,
                title = "Text Generation",
                content = "Text generation is the process of creating new text based on a given input. It is used to generate human-like text, such as chatbot responses, product descriptions, and news articles.",
                color = LightGreen
            ),
            Note(
                id = 5,
                title = "Machine Translation",
                content = "Machine translation is the process of translating text from one language to another using a machine translation system. It is used to bridge the language barrier and enable communication between speakers of different languages.",
                color = RedPink
            )
        )
    ),
)