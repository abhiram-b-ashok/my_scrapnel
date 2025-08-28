package com.example.myscrapnel.views.create_scrapnel_page


import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import androidx.core.net.toUri
import coil.Coil
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

@Composable
private fun ImageStack(images: List<Uri>) {
    if (images.size == 1) {
        SingleImageView(uri = images.first())
    } else {
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
                val size = rearrangedImages.size
                val middleIndex = size / 2

                val (xOffset, zRotation) = when {
                    size == 2 -> {
                        // For 2 images, slide both:
                        if (index == 0) {
                            30.dp to 20f  // first image slide right + rotate right
                        } else {
                            (-30).dp to -20f  // second image slide left + rotate left
                        }
                    }
                    size % 2 == 1 -> {
                        // Odd number of images - keep your original logic
                        val isMiddle = index == middleIndex
                        val isLeft = index > middleIndex
                        val isRight = index < middleIndex

                        val offsetAmount = (kotlin.math.abs(index - middleIndex)) * 20.dp
                        val rotationAngle = (kotlin.math.abs(index - middleIndex)) * 15f

                        when {
                            isLeft -> -offsetAmount to -rotationAngle
                            isRight -> offsetAmount to rotationAngle
                            else -> 0.dp to 0f
                        }
                    }
                    else -> {
                        // For even number > 2, fallback to your existing logic or adjust accordingly
                        val offsetAmount = (kotlin.math.abs(index - middleIndex)) * 20.dp
                        val rotationAngle = (kotlin.math.abs(index - middleIndex)) * 15f
                        val isLeft = index > middleIndex
                        val isRight = index < middleIndex

                        when {
                            isLeft -> -offsetAmount to -rotationAngle
                            isRight -> offsetAmount to rotationAngle
                            else -> 0.dp to 0f
                        }
                    }
                }


                    ImageCard(
                        uri = uri,
                        contentDescription = "$uri",
                        modifier = Modifier
                            .offset(x = xOffset)
                            .graphicsLayer { rotationZ = zRotation }
                    )
                }
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
        shape = RoundedCornerShape(0.dp)
    ) {
        Box {
            AsyncImage(
                model = uri,
                contentDescription = contentDescription,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .clip(RoundedCornerShape(0.dp))
                    .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground))
                    .padding(3.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun SingleImageView(uri: Uri) {
    val context = LocalContext.current
    var isPortrait by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(uri) {
        withContext(Dispatchers.IO) {
            try {
                val request = ImageRequest.Builder(context)
                    .data(uri)
                    .allowHardware(false)
                    .build()

                val result = Coil.imageLoader(context).execute(request)
                val drawable = result.drawable
                drawable?.let {
                    val width = it.intrinsicWidth
                    val height = it.intrinsicHeight

                    isPortrait = height > width
                }
            } catch (e: Exception) {
                e.printStackTrace()
                isPortrait = null
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        when (isPortrait) {
            true -> {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .aspectRatio(2f / 3f),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .background(color = Color.White)
                            .clip(RoundedCornerShape(0.dp))
                            .border(BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground))
                            .padding(5.dp),
                    )
                }
            }

            false -> {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .background(color = Color.White)
                            .clip(RoundedCornerShape(0.dp))
                            .border(BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground))
                            .padding(5.dp),
                    )
                }
            }

            null -> {
                CircularProgressIndicator()
            }
        }
    }
}
