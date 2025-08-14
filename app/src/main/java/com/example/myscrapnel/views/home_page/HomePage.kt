package com.example.myscrapnel.views.home_page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myscrapnel.R
import com.example.myscrapnel.ui.theme.verticalGradientBrushDark

@Composable
fun Homepage(modifier: Modifier = Modifier) {
    val headerTitle by remember { mutableStateOf("My Scrapnel") }
    Column(
        modifier = Modifier
            .fillMaxSize()

    )
    {
        Header(headerTitle, modifier = modifier)
        Main()
    }
}

@Composable
fun Main() {
    Column {
        ChipsAndFilter()
        ScrapnelListScreen()
    }
}

@Composable
fun Header(title: String, modifier: Modifier) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
            .systemBarsPadding()
            .padding(15.dp)
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
        ) {
            items(10) {
                ScrapnelCard(
                    "This was one of the most memorable day in my life since I was a kid.",
                    true
                )
            }
        }
    }
}


@Composable
fun ScrapnelCard(fullText: String, isDeleting: Boolean) {

    var firstTextViewText by rememberSaveable { mutableStateOf(fullText) }
    var secondTextViewText by rememberSaveable { mutableStateOf("") }
    var isOverFlowHandled by rememberSaveable { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }

//    LaunchedEffect(fullText) {
//        firstTextViewText = fullText
//        secondTextViewText = ""
//        isOverFlowHandled = false
//    }


    Box(
        modifier = Modifier.height(200.dp)
            .clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.primary).padding(8.dp)
    ) {

        Box(modifier = Modifier.fillMaxSize().align(Alignment.Center)
            .clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.tertiary)) {

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
                                modifier = Modifier.size(60.dp),
                                contentScale= ContentScale.Crop
                            )
                        }


                        Text(
                            text = firstTextViewText,
                            maxLines = 3,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            color = MaterialTheme.colorScheme.onSecondary,
                            style = MaterialTheme.typography.bodyMedium,
                            onTextLayout = { textLayoutResult ->
                                if (!isOverFlowHandled && textLayoutResult.didOverflowHeight) {
                                    val lastVisibleCharIndex = textLayoutResult.getLineEnd(
                                        lineIndex = textLayoutResult.lineCount - 1,
                                        visibleEnd = true
                                    )
                                    firstTextViewText =
                                        fullText.substring(0, lastVisibleCharIndex)
                                    secondTextViewText =
                                        fullText.substring(lastVisibleCharIndex)
                                    isOverFlowHandled = true
                                }
                            }
                        )
                    }


                    Text(
                        text = secondTextViewText,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    Card(Modifier.padding(top = 5.dp), shape = RoundedCornerShape(5.dp)) {
                        Text(
                            text = "title",
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(brush = verticalGradientBrushDark),
                            color = MaterialTheme.colorScheme.onTertiary,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }

                }

        }

        if (isChecked) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Red.copy(alpha = 0.4f))
            )
            {

                Image(
                    painter = painterResource(R.drawable.ic_cross),
                    contentDescription = "Delete",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(100.dp),
                )
            }

        }
        if (isDeleting) {
            Checkbox(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(bottom = 10.dp, start = 10.dp),
                checked = isChecked, onCheckedChange = { isChecked = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.secondary,
                    uncheckedColor = MaterialTheme.colorScheme.onSecondary,
                    checkmarkColor = MaterialTheme.colorScheme.onSecondary
                )
            )
        }
    }
}


@Composable
fun ChipsAndFilter() {
    var isFiltering by remember { mutableStateOf(false) }
    var selectedChip by remember { mutableStateOf<String?>(null) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, end = 0.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            modifier = Modifier
                .horizontalScroll(enabled = true, state = rememberScrollState())
                .weight(1f)
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

        }

        if (!isFiltering) {

            Card(
                modifier = Modifier
                    .height(45.dp)
                    .width(55.dp),
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 0.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 0.dp
                )
            ) {
                Box(
                    modifier = Modifier
                        .background(brush = verticalGradientBrushDark)
                        .fillMaxSize()
                )
                {
                    IconButton(
                        onClick = {}, modifier = Modifier
                            .padding(2.dp)
                            .fillMaxSize()
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_filter),
                            contentDescription = "Filter",
                            modifier = Modifier.size(24.dp),

                            )
                    }
                }

            }


        }
        if (isFiltering) {

            Card(
                modifier = Modifier
                    .height(45.dp)
                    .width(55.dp),
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 0.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 0.dp
                )
            ) {
                Box(
                    modifier = Modifier
                        .background(brush = verticalGradientBrushDark)
                        .fillMaxSize()
                )
                {
                    Text(
                        text = "Clear",
                        modifier = Modifier
                            .clickable(onClick = {})
                            .padding(top = 12.dp)
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center

                    )
                }

            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TitleChips(
    chipItems: List<String>,
    selectedChip: String?,
    onChipSelected: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        chipItems.forEach { chip ->
            FilterChip(
                selected = selectedChip == chip,
                onClick = { onChipSelected(chip) },
                label = { Text(chip) },
                modifier = Modifier.padding(start = 8.dp, end = 2.dp)

            )
        }
    }
}


@Composable
fun FilterDialog() {

    var isShowDialog by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }

    Button(onClick = { isShowDialog = true }) {
        Text("Show Alert")
    }

    if (isShowDialog) {
        AlertDialog(
            onDismissRequest = { isShowDialog = false },
            title = { Text("Filter by") },
            text = {
                Column {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        label = { Text("title") }
                    )
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        label = { Text("date") }
                    )
                }
            },

            confirmButton = {
                Button(onClick = {
                    isShowDialog = false
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                IconButton(onClick = { isShowDialog = false }) {
                    Icon(Icons.Default.Clear, contentDescription = "Delete")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DialogPreview() {
    FilterDialog()
}