package com.example.scribe

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri



@Composable
fun FilePicker(
    coroutineScope: CoroutineScope,
    mimeTypeFilter: Array<String>,
    selectProfileActivity: ActivityResultLauncher<Array<String>>,
    selectPhotoActivity: ActivityResultLauncher<Array<String>>,
    profileImageBitmap: MutableState<ImageBitmap?>,
    photoImageBitmap: MutableList<ImageBitmap>
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Upload Your PDFs", style = TextStyle(fontSize = 30.sp))

            Spacer(modifier = Modifier.height(16.dp))

            AsyncImage(
                model = "https://th.bing.com/th/id/OIP.72WaWKn2a0V6cRgjt8r69QHaJG?w=153&h=188&c=7&r=0&o=5&dpr=2&pid=1.7",
                contentDescription = "PDF",
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
                    .clip(RectangleShape)
                    .clickable { selectProfileActivity.launch(mimeTypeFilter) }
            )


            Text("Tap to select PDF", style = MaterialTheme.typography.caption)

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                coroutineScope.launch {
                    selectPhotoActivity.launch(mimeTypeFilter)
                }
            }) {
                Spacer(Modifier.width(8.dp))
                Text("Upload File")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (photoImageBitmap.size > 0) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(),
                    contentPadding = PaddingValues(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    items(photoImageBitmap, itemContent = { photo ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            elevation = 8.dp
                        ) {
                            Image(
                                painter = BitmapPainter(photo),
                                contentDescription = "Photo image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(150.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        }
                    })
                }
            } else {
                Text("No PDFs uploaded. Tap the button above to upload PDFs.", style = MaterialTheme.typography.caption)
            }
        }
    }
}



fun Uri.getFileName (context: Context): String?
{
    val cursor = context.contentResolver.query(this, null, null, null, null)
    if (cursor == null || !cursor.moveToFirst()) return null

    val indexName = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    val fileName = cursor.getString(indexName)
    cursor.close()

    return fileName
}

fun Uri.getFile (context: Context): File?
{
    val fileDescriptor = context.contentResolver.openFileDescriptor(this, "r", null) ?: return null

    val file = File(context.cacheDir, getFileName(context)!!)
    val fileOutputStream = FileOutputStream(file)

    val fileInputStream = FileInputStream(fileDescriptor.fileDescriptor)
    fileInputStream.copyTo(fileOutputStream)
    fileDescriptor.close()

    return file
}
