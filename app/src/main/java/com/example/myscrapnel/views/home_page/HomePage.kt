package com.example.myscrapnel.views.home_page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
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

@Composable
fun Homepage(modifier: Modifier = Modifier) {
    val headerTitle by remember { mutableStateOf("My Scrapnel") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)

    )
    {
        Header(headerTitle, modifier=modifier)
        ChipsAndFilter()
        ScrapnelListScreen()

    }
}


@Composable
fun Header(title: String, modifier: Modifier) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .padding(8.dp)
            .then(modifier)
            .safeDrawingPadding()
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
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(0.5f),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Box {
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            ) {
                Box {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(8.dp),
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiary)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp)
                        ) {
                            Row {
                                Card(modifier = Modifier.padding(end = 4.dp)) {
                                    Image(
                                        painter = painterResource(id = R.drawable.image_ic),
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
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Card(Modifier.padding(top = 5.dp)) {
                                Text(
                                    text = "title",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.secondary),
                                    color = MaterialTheme.colorScheme.onTertiary,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ChipsAndFilter() {
    var isFiltering by remember { mutableStateOf(false) }
    var selectedChip by remember { mutableStateOf<String?>(null) }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        TitleChips(
            chipItems = listOf(
                "Travel",
                "Food",
                "Friends",
                "Family",
                "Breakfast",
                "Cricket",
                "Swimming",
                "Tea",
                "Temple"
            ),
            selectedChip = selectedChip,
            onChipSelected = { chipSelected ->
                selectedChip = if (selectedChip == chipSelected) null else chipSelected
            }
        )

        if (!isFiltering) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "Filter"
                )
            }
        }
    }
    if (isFiltering) {
        Text(
            text = "Clear Filter",
            modifier = Modifier.clickable(onClick = {})
        )
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TitleChips(
    chipItems: List<String>,
    selectedChip: String?,
    onChipSelected: (String) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(0.8f)
    ) {
        chipItems.forEach { chip ->
            FilterChip(
                selected = selectedChip == chip,
                onClick = { onChipSelected(chip) },
                label = { Text(chip) }
            )
        }
    }
}
