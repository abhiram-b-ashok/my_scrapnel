package com.example.myscrapnel.views.create_scrapnel_page


import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import androidx.core.net.toUri

@Composable
fun PreviewScrapnel(
    title: String,
    scrapnelText: String,
    year: String,
    month: String,
    day: String,
    hour: String,
    minute: String,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .width(300.dp)
                .height(500.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            val scrollState = rememberScrollState()
            val imagesList = mutableListOf<Uri>()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {

                // Title Row
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = title,
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(1f),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "close preview",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                // Date and time row
                Row(modifier = Modifier.padding(bottom = 10.dp)) {
                    Text(
                        text = "$month, ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "$day, ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "$year",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "$hour:",
                        modifier = Modifier.padding(start = 5.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "$minute",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                // Body content (text and images)
                scrapnelText.lines().forEach { line ->
                    if (line.contains("🖼️")) {
                        imagesList.add(line.drop(4).toUri())

                        // Show stacked if exactly 3
                        if (imagesList.size == 3) {
                            ImageStack(images = imagesList.toList())
                            imagesList.clear()
                        }
                    } else {
                        // Show images if any before next text
                        if (imagesList.isNotEmpty()) {
                            ImageStack(images = imagesList.toList())
                            imagesList.clear()
                        }

                        // Show the text
                        Text(
                            text = line,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                // Show any remaining images
                if (imagesList.isNotEmpty()) {
                    ImageStack(images = imagesList.toList())
                }
            }
        }
    }
}

@Composable
fun ImageStack(images: List<Uri>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        images.forEachIndexed { index, uri ->
            val offsetAmount = (images.size - 1 - index) * 12.dp
            val rotationAngle = (images.size - 1 - index) * 5f

            ImageCard(
                uri = uri,
                contentDescription = null,
                modifier = Modifier
                    .offset(x = -offsetAmount, y = -offsetAmount)
                    .graphicsLayer { rotationZ = -rotationAngle }
            )
        }
    }
}

@Composable
private fun ImageCard(
    uri: Uri,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(0.35f)
            .height(100.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box {
            AsyncImage(
                model = uri,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }
    }
}
