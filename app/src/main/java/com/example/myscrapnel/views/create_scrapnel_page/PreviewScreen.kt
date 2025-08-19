package com.example.myscrapnel.views.create_scrapnel_page

import android.R.attr.text
import android.R.attr.top
import android.net.Uri
import android.util.Log
import android.util.Log.i
import androidx.collection.emptyLongSet
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
fun PreviewScreen(
    title: String,
    scrapnelText: String,
    year: String,
    month: String,
    day: String,
    hour: String,
    minute: String,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismiss() }, )
    {
        Card(
            modifier = Modifier
                .width(300.dp)
                .height(500.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            val scrollState = rememberScrollState()

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
                            .padding(4.dp)
                            .weight(1f),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    IconButton(onDismiss) {
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
                        modifier = Modifier,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "$day, ",
                        modifier = Modifier,
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
                        modifier = Modifier,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                }

                val imagesList = mutableListOf<Uri>()

                scrapnelText.lines().forEachIndexed { index, string ->

                    if (string.contains("🖼️")) {

                        imagesList.add(string.drop(4).toUri())

                        if (imagesList.size == 3) {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(top = 30.dp, bottom = 20.dp), contentAlignment =
                                    Alignment.Center
                            ) {

                                imagesList.forEachIndexed { index, string ->
                                    val offsetAmount = (imagesList.size - 1 - (index + 1)) * 20.dp
                                    val rotationAngle = (imagesList.size - 1 - (index + 1)) * 35f
                                    ImageCard(
                                        uri = string,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .offset(x = -offsetAmount, y = -offsetAmount)
                                            .graphicsLayer {
                                                rotationZ = -rotationAngle
                                            }
                                    )
                                }
                            }
                            imagesList.clear()
                        }

                    } else {

                        if (imagesList.size >= 1) {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(top=10.dp, bottom = 20.dp)
                                , contentAlignment =
                                    Alignment.Center
                            ) {
                                imagesList.forEachIndexed { index, string ->
                                    if (imagesList.size == 1) {
                                        ImageCard(
                                            uri = string,
                                            contentDescription = null,
                                            modifier = Modifier
                                        )
                                    } else {
                                        val offsetAmount = (imagesList.size - 1 - (index + 1)) * 20.dp
                                        val rotationAngle = (imagesList.size - 1 - (index + 1)) * 35f
                                        ImageCard(
                                            uri = string,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .offset(x = -offsetAmount, y = -offsetAmount)
                                                .graphicsLayer {
                                                    rotationZ = -rotationAngle
                                                }
                                        )
                                    }
                                }
                                imagesList.clear()
                            }
                        } else {
                            Box( modifier =
                                Modifier
                                    .fillMaxWidth()
                                   , contentAlignment =
                                Alignment.CenterStart)
                            {
                                Text(text = string, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)

                            }


                        }


                    }


                }
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
        shape = RoundedCornerShape(8.dp)
    ) {
        Box {
            AsyncImage(
                model = uri,
                contentDescription = contentDescription,
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPreview() {
    PreviewScreen(
        title = "Nice evening",
        scrapnelText = "xdddcdcdcdcdcdcdeeeedjzbajzbajzabzjazbajkzbskjzbsjzbajkzanbzkjabzkjabjbzkjabzajkbjkbe",
        "2025",
        "July",
        "19",
        "12",
        "30",
        onDismiss = {})
}