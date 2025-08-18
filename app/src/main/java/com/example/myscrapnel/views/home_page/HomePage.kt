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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.sp
import com.example.myscrapnel.R


@Composable
fun Homepage(modifier: Modifier = Modifier) {
    val headerTitle by remember { mutableStateOf("My Scrapnel") }

    var isShowDialog by remember { mutableStateOf(false) }
    var isDeleting by remember { mutableStateOf(false) }
    var isFiltering by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Header(
            headerTitle,
            modifier = modifier,
            showDialog = { isShowDialog = true },
            dismissDialog = { isShowDialog = false
                isFiltering = false   },
            startDelete = { isDeleting = !isDeleting }
        )

        Main(isDeleting = isDeleting)

        FilterDialog(
            onFilterApplied = { filterType, filterValue ->
                isShowDialog = false
            },
            isShowDialog = isShowDialog,
            onDismiss = { isShowDialog = false
                isFiltering = false}
        )
    }
}



@Composable
private fun Main(isDeleting: Boolean) {
    val scrapnels: List<String> = listOf(
        "This was one of the most memorable day...", "This was one of the most memorable day...", "This was one of the most memorable day...", "This was one of the most memorable day...",
    )
//    val scrapnels : List<String> = emptyList()

    Column {
        ChipsAndFilter()
        ScrapnelListScreen(scrapnels, isDeleting, onCreateScrapnelClick = {})
    }
}


@Composable
private fun Header(
    title: String,
    modifier: Modifier,
    showDialog: () -> Unit,
    dismissDialog: () -> Unit,
    startDelete: () -> Unit,
) {
    var isFiltering by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(
                PaddingValues(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                    start = 15.dp,
                    end = 15.dp,
                    bottom = 8.dp
                )
            )
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineLarge
        )
        Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { startDelete() }) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(20.dp)
                )
            }

            if (!isFiltering){
                IconButton(onClick = { showDialog()
                isFiltering = true}) {
                    Icon(
                        painter = painterResource(R.drawable.ic_filter),
                        contentDescription = "Filter",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            else
            {

                Text(text = "Clear", modifier = Modifier.padding(start = 4.dp)
                    .clickable(onClick = {
                        isFiltering = false}),
                    color = MaterialTheme.colorScheme.onBackground)
            }


        }
    }
}



@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    Homepage()
}


@Composable
fun ScrapnelListScreen(
    scrapnels: List<String>,
    isDeleting: Boolean,
    onCreateScrapnelClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (scrapnels.isEmpty()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                FloatingActionButton(
                    onClick = { onCreateScrapnelClick() },
                    modifier = Modifier,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape,
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Create Scrapnel", modifier = Modifier.size(30.dp))
                }

                Text(
                    text = "Start creating \n your day...",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 20.dp),
                    textAlign = TextAlign.Center
                )
            }        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp),
            ) {
                items(scrapnels.size) {
                    ScrapnelCard(scrapnels[it], isDeleting)
                }
            }
            FloatingActionButton(
                onClick = { onCreateScrapnelClick() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Scrapnel", modifier = Modifier.size(30.dp))
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

    Card(modifier = Modifier.height(215.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
       ) {
        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(12.dp)
            ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                                .height(79.dp),
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(79.dp)
                                    .padding(end = 6.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.image),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillBounds,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

//                        Text(
//                            text = firstTextViewText,
//                            maxLines = 4,
//                            overflow = TextOverflow.Clip,
//                            modifier = Modifier
//                                .weight(1f)
//                                .fillMaxHeight(),
//                            color = MaterialTheme.colorScheme.onSurface,
//                            style = MaterialTheme.typography.bodyMedium,
//                            onTextLayout = { textLayoutResult ->
//                                if (!isOverFlowHandled && textLayoutResult.didOverflowHeight) {
//                                    val lastVisibleCharIndex = textLayoutResult.getLineEnd(
//                                        lineIndex = textLayoutResult.lineCount - 1,
//                                        visibleEnd = true
//                                    )
//                                    firstTextViewText = fullText.substring(0, lastVisibleCharIndex)
//                                    secondTextViewText = fullText.substring(lastVisibleCharIndex)
//                                    isOverFlowHandled = true
//                                }
//                            }
//                        )
                        }

//                    Text(
//                        text = secondTextViewText,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(top = 5.dp)
//                            .heightIn(min = 48.dp),
//                        color = MaterialTheme.colorScheme.onSurface,
//                        style = MaterialTheme.typography.bodyMedium,
//                        maxLines = 4,
//                        overflow = TextOverflow.Ellipsis
//                    )
                        Text(
                            text = firstTextViewText,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(5.dp)
                            )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.primary)

                        ) {
                            Text(
                                text = "title of the scrapnel",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }



            }
            if (isDeleting) {
                Checkbox(
                    modifier = Modifier
                        .align(Alignment.TopEnd),
                    checked = isChecked,
                    onCheckedChange = { isChecked = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.primary,
                        checkmarkColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }

            if (isChecked) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Red.copy(alpha = 0.4f))
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_cross),
                        contentDescription = "Delete",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(100.dp),
                    )
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

//        if (!isFiltering) {
//
//            Card(
//                modifier = Modifier
//                    .height(45.dp)
//                    .width(55.dp),
//                shape = RoundedCornerShape(
//                    topStart = 20.dp,
//                    topEnd = 0.dp,
//                    bottomStart = 20.dp,
//                    bottomEnd = 0.dp
//                )
//            ) {
//                Box(
//                    modifier = Modifier
//                        .background(brush = verticalGradientBrushDark)
//                        .fillMaxSize()
//                )
//                {
//                    IconButton(
//                        onClick = {}, modifier = Modifier
//                            .padding(2.dp)
//                            .fillMaxSize()
//                    ) {
//                        Icon(
//                            painter = painterResource(R.drawable.ic_filter),
//                            contentDescription = "Filter",
//                            modifier = Modifier.size(24.dp),
//
//                            )
//                    }
//                }
//
//            }
//
//
//        }
//        if (isFiltering) {
//
//            Card(
//                modifier = Modifier
//                    .height(45.dp)
//                    .width(55.dp),
//                shape = RoundedCornerShape(
//                    topStart = 20.dp,
//                    topEnd = 0.dp,
//                    bottomStart = 20.dp,
//                    bottomEnd = 0.dp
//                )
//            ) {
//                Box(
//                    modifier = Modifier
//                        .background(brush = verticalGradientBrushDark)
//                        .fillMaxSize()
//                )
//                {
//                    Text(
//                        text = "Clear",
//                        modifier = Modifier
//                            .clickable(onClick = {})
//                            .padding(top = 12.dp)
//                            .fillMaxSize(),
//                        color = MaterialTheme.colorScheme.onSecondary,
//                        style = MaterialTheme.typography.titleSmall,
//                        textAlign = TextAlign.Center
//
//                    )
//                }
//
//            }
//        }
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
                modifier = Modifier.padding(start = 8.dp, end = 2.dp),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    selectedContainerColor = MaterialTheme.colorScheme.secondary,
                    labelColor = MaterialTheme.colorScheme.onPrimary,
                    selectedLabelColor = MaterialTheme.colorScheme.onSecondary,
                ),
                border = null,
                shape = CircleShape

            )
        }
    }
}


@Composable
fun FilterDialog(
    onFilterApplied: (String, String) -> Unit,
    isShowDialog: Boolean,
    onDismiss: () -> Unit
) {
    var titleInput by remember { mutableStateOf("") }
    var dateInput by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("title") }

    if (isShowDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text("Filter Options") },
            text = {
                Column {
                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = {
                            titleInput = it
                            if (it.isNotEmpty()) dateInput = ""
                            selectedFilter = "title"
                        },
                        label = { Text("Filter by Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = dateInput.isEmpty()
                    )
                    OutlinedTextField(
                        value = dateInput,
                        onValueChange = {
                            dateInput = it
                            if (it.isNotEmpty()) titleInput = ""
                            selectedFilter = "date"
                        },
                        label = { Text("yyyy/mm/dd") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = titleInput.isEmpty(),

                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val value = if (selectedFilter == "title") titleInput else dateInput
                        if (value.isNotBlank()) onFilterApplied(selectedFilter, value)
                        onDismiss()
                    }
                ) {
                    Text("Apply Filter")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { onDismiss()
                }) {
                    Text("Cancel")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
        )
    }
}




