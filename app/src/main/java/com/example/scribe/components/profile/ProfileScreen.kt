package com.example.scribe.components.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.scribe.components.auth.GoogleSignInButton
import com.example.scribe.components.auth.SignOutButton
import com.example.scribe.components.course.NoteItem
import com.example.scribe.viewmodel.MainViewModel

@Composable
fun ProfileScreen(viewModel: MainViewModel, onSignOut: () -> Unit) {


    val userNotes = remember {
        viewModel.userNotes.value
    }
    val starredNotes = remember {
        viewModel.starredNotes.value
    }
    val selectedTab = remember { mutableIntStateOf(0) }

    val name = remember {
        viewModel.name.value
    }
    val avatar = remember {
        viewModel.avatar.value
    }
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    AsyncImage(
                        model = avatar,
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = name, style = MaterialTheme.typography.h5)


            }
            Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.fillMaxWidth()) {
                SignOutButton(viewModel = viewModel, onSignOut)
            }

            Spacer(modifier = Modifier.height(16.dp))
            TabRow(selectedTabIndex = selectedTab.intValue, backgroundColor = MaterialTheme.colors.surface) {
                Tab(
                    selected = selectedTab.intValue == 0,
                    onClick = { selectedTab.intValue = 0 },
                    text = { Text(text = "Notes") }
                )
                Tab(
                    selected = selectedTab.intValue == 1,
                    onClick = { selectedTab.intValue = 1},
                    text = { Text(text = "Starred") }
                )
            }
            Spacer(modifier = Modifier
                .height(16.dp)
                .padding(16.dp)
                .padding(bottom = 40.dp))
            LazyColumn {
                if (selectedTab.intValue == 0) {
                    items(userNotes.size) { index ->
                        val note = userNotes[index]
                        NoteItem(note = note, onFavoriteClick = {})

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                } else {
                    items(starredNotes.size) { index ->
                        val note = starredNotes[index]
                        NoteItem(note = note, onFavoriteClick = {})

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

            }

        }

    }

}