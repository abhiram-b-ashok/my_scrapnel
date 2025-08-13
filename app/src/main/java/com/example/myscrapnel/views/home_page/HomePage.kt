package com.example.myscrapnel.views.home_page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myscrapnel.R
import com.example.myscrapnel.archivo

@Composable
fun Homepage(modifier: Modifier = Modifier) {
    val headerTitle by remember { mutableStateOf("My Scrapnel") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    )
    {
        Header(headerTitle)
        ChipsAndFilter()
        ScrapnelListScreen()

    }
}


@Composable
fun Header(title: String) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title, modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.headlineLarge
        )
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    Homepage()
}


@Composable
fun ChipsAndFilter() {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondary)
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
    }
}

@Composable
fun ScrapnelListScreen() {
    Box {
        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            items(10) {
                ScrapnelCard("ybububiubnuiniiunoinoinoihnkjnlknlj.knl,nllinoinmooinmoimnoinmoijionoi")
            }
        }
    }
}

@Composable
fun ScrapnelCard(fullText: String) {
    var firstTextViewText by remember { mutableStateOf(fullText) }
    var secondTextViewText by remember { mutableStateOf("") }
    var isOverFlowHandled by remember { mutableStateOf(false) }

    LaunchedEffect(fullText) {
        firstTextViewText = fullText
        secondTextViewText = ""
        isOverFlowHandled = false
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(0.5f)
            .height(200.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Box {
            Card(
                modifier = Modifier.padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            ) {
                Box {
                    Card(modifier = Modifier.padding(8.dp),
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiary) ){
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp)
                        ) {
                            Row {
                                Card(modifier = Modifier.padding(end = 4.dp)) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_launcher_background),
                                        contentDescription = null,
                                        modifier = Modifier.size(60.dp)
                                    )
                                }


                                Text(
                                    text = firstTextViewText,
                                    maxLines = 3,
                                    modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.onSecondary,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontFamily = archivo,
                                    onTextLayout = { textLayoutResult ->
                                        if (!isOverFlowHandled && textLayoutResult.didOverflowHeight) {
                                            val lastVisibleCharIndex = textLayoutResult.getLineEnd(
                                                lineIndex = textLayoutResult.lineCount - 1,
                                                visibleEnd = true
                                            )
                                            firstTextViewText = fullText.substring(0, lastVisibleCharIndex)
                                            secondTextViewText = fullText.substring(lastVisibleCharIndex)
                                            isOverFlowHandled = true
                                        }
                                    }
                                )
                            }

                            if (secondTextViewText.isNotEmpty()) {
                                Text(
                                    text = secondTextViewText,
                                    modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.onSecondary,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontFamily = archivo,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Card {
                                Text(
                                    text = "title",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.secondary),
                                    color = MaterialTheme.colorScheme.onTertiary,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontFamily = archivo,
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}

