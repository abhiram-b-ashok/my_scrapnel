package com.example.myscrapnel.views.create_scrapnel_page


import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import androidx.core.net.toUri

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
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

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = title,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
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

                scrapnelText.lines().forEach { line ->
                    if (line.isBlank()) {
                        return@forEach
                    }

                    if (line.contains("🖼️")) {
                        imagesList.add(line.drop(4).toUri())

                        if (imagesList.size == 3) {
                            ImageStack(images = imagesList)
                            imagesList.clear()
                        }
                    } else {
                        if (imagesList.isNotEmpty()) {
                            ImageStack(images = imagesList)
                            imagesList.clear()
                        }

                        Text(
                            text = line,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                if (imagesList.isNotEmpty()) {
                    ImageStack(images = imagesList)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun ImageStack(images: List<Uri>) {

//    val rearrangedImages = remember {images.toMutableList()}
    val rearrangedImages = remember {
        mutableStateListOf<Uri>().apply { addAll(images) }
    }

    val imageHeight = 120.dp
    val offsetPerImage = 20.dp
    val maxHeight = imageHeight + (rearrangedImages.size - 1) * offsetPerImage
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(maxHeight)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        rearrangedImages.forEachIndexed { index, uri ->

            val offsetAmount = (rearrangedImages.size - 1 - index) * 20.dp
            val rotationAngle = (rearrangedImages.size - 1 - index) * 10f

            val isTop = index == rearrangedImages.lastIndex

            ImageCard(
                uri = uri,
                contentDescription = "$uri",
                modifier = Modifier
                    .offset(y = -offsetAmount, x = -offsetAmount)
                    .graphicsLayer { rotationZ = -rotationAngle }
                    .then(if (isTop) Modifier.clickable {
                        val top = rearrangedImages.removeLast()
                        rearrangedImages.add(0, top)
                    }
                    else Modifier)
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
