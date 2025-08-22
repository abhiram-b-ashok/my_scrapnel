package com.example.myscrapnel.views.home_page


import android.R.attr.text
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.room.Room
import coil.compose.rememberAsyncImagePainter
import com.example.myscrapnel.R
import com.example.myscrapnel.models.scrapnel_ui_model.ScrapnelUiModel
import com.example.myscrapnel.room_db.ScrapnelDatabase
import com.example.myscrapnel.room_db.ScrapnelEntity
import com.example.myscrapnel.utils.convertDdMmYyyyToTimestamp
import com.example.myscrapnel.viewmodels.ViewScrapnelRepository
import com.example.myscrapnel.viewmodels.ViewScrapnelViewModel
import com.example.myscrapnel.viewmodels.ViewScrapnelViewModelFactory
import kotlinx.coroutines.delay


@Composable
fun Homepage(modifier: Modifier = Modifier, navController: NavController) {
    var headerTitle by remember { mutableStateOf("My Scrapnel") }

    var isShowDialog by remember { mutableStateOf(false) }
    var isDeleting by remember { mutableStateOf(false) }
    var isFiltering by remember { mutableStateOf(false) }
    var filterKey by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current
    val database = Room.databaseBuilder(
        context,
        ScrapnelDatabase::class.java,
        "scrapnel_db"
    ).build()
    val scrapnelRepository = ViewScrapnelRepository(database.dao())
    val scrapnelViewModelFactory = ViewScrapnelViewModelFactory(scrapnelRepository)
    val scrapnelViewModel: ViewScrapnelViewModel = viewModel(factory = scrapnelViewModelFactory)
    LaunchedEffect(isDeleting) {
        if (!isDeleting) {
            scrapnelViewModel.clearSelectedItems()
        }
    }
    fun stopDeleteMode() {
        isDeleting = false
        scrapnelViewModel.clearSelectedItems()
    }



    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Header(
            headerTitle,
            modifier = modifier,
            showDialog = { isShowDialog = true },
            dismissDialog = {
                isShowDialog = false
                isFiltering = false
            },
            startDelete = { isDeleting = !isDeleting },
            isFiltering = isFiltering,
            onClearFilter = {
                filterKey = ""
                headerTitle = "My Scrapnel"
                isFiltering = false
            },
            scrapnelViewModel,
            stopDeleteMode = { stopDeleteMode() },
            isDeleting
        )

        Main(isDeleting = isDeleting, navController, scrapnelViewModel, isFiltering, filterKey)

        FilterDialog(
            onFilterApplied = { filterType, filterValue, filterDisplay ->
                isShowDialog = false
                isFiltering = true
                filterKey = filterValue
                headerTitle = "$filterDisplay"
            },
            isShowDialog = isShowDialog,
            onDismiss = { isShowDialog = false }
        )
    }
}


@Composable
private fun Header(
    title: String,
    modifier: Modifier,
    showDialog: () -> Unit,
    dismissDialog: () -> Unit,
    startDelete: () -> Unit,
    isFiltering: Boolean,
    onClearFilter: () -> Unit,
    scrapnelViewModel: ViewScrapnelViewModel,
    stopDeleteMode: () -> Unit,
    isDeleting: Boolean
) {

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

            if (isDeleting) {
                Text(
                    text = "Delete",
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clickable {
                            scrapnelViewModel.deleteSelectedItems()
                            stopDeleteMode()

                        },
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            IconButton(onClick = {
                startDelete()
            }) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(20.dp)
                )
            }

            if (!isFiltering) {
                IconButton(onClick = { showDialog() }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_filter),
                        contentDescription = "Filter",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {

                Text(
                    text = "Clear",
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clickable {
                            scrapnelViewModel.loadScrapnels()
                            scrapnelViewModel.loadChipTitles()
                            onClearFilter()

                        },
                    color = MaterialTheme.colorScheme.onBackground
                )

            }


        }
    }
}


@Composable
private fun Main(
    isDeleting: Boolean,
    navController: NavController,
    scrapnelViewModel: ViewScrapnelViewModel,
    isFiltering: Boolean,
    filterKey: String
) {

    val chipTitles by scrapnelViewModel.chipTitles.collectAsState()

    LaunchedEffect(Unit) {
        scrapnelViewModel.loadChipTitles()
    }

    Column {
        ChipsAndFilter(chipTitles, isFiltering)
        ScrapnelListScreen(filterKey, isDeleting, navController, scrapnelViewModel)
    }
}


@Composable
fun ScrapnelListScreen(
    filterKey: String,
    isDeleting: Boolean,
    navController: NavController,
    scrapnelViewModel: ViewScrapnelViewModel,
) {
    val scrapnelItems by scrapnelViewModel.scrapnelItems.collectAsState()
    var isLoading by remember { mutableStateOf(true) }
    var isDataLoaded by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        delay(500)
        isDataLoaded = true
    }

    LaunchedEffect(filterKey) {
        isLoading = true
        if (filterKey.isNotEmpty()) {
            scrapnelViewModel.loadFilteredScrapnels(filterKey)
        } else {
            scrapnelViewModel.loadScrapnels()
        }
        isLoading = false
    }




    Box(modifier = Modifier.fillMaxSize()) {
        if (isDataLoaded) {
                Text(text = "")
        } else {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (scrapnelItems.isEmpty()) {
            EmptyScreen(navController, Modifier.align(Alignment.Center))
        } else {
            ScrapnelGrid(scrapnelItems, isDeleting, scrapnelViewModel, navController)
            FloatingActionButton(
                onClick = { navController.navigate("create") },

                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Create Scrapnel",
                    modifier = Modifier.size(30.dp)
                )
            }
        }

    }
}

@Composable
fun EmptyScreen(navController: NavController, modifier: Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.fillMaxWidth()
    ) {
        FloatingActionButton(
            onClick = { navController.navigate("create") },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = CircleShape,
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Create Scrapnel",
                modifier = Modifier.size(30.dp)
            )
        }
        Text(
            text = "Start creating \n your day...",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 20.dp),
            textAlign = TextAlign.Center
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun ScrapnelGrid(
    items: List<ScrapnelUiModel>,
    isDeleting: Boolean,
    scrapnelViewModel: ViewScrapnelViewModel,
    navController: NavController
) {

    val selectedItems = remember { mutableStateMapOf<Long, Boolean>() }


    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(items.size, key = { items[it].timeStamp }) { index ->
            val item = items[index]
            val isChecked = selectedItems[item.timeStamp] == true
            ScrapnelCard(
                item, isDeleting, isChecked, onCheckedChange = { checked ->
                    selectedItems[item.timeStamp] = checked
                    scrapnelViewModel.selectCheckedItems(item, checked)
                },
                navController
            )
        }
    }
}


@Composable
fun ScrapnelCard(
    item: ScrapnelUiModel, isDeleting: Boolean, isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit, navController: NavController
) {
    var firstTextViewText by remember { mutableStateOf(item.fullText) }
    var secondTextViewText by remember { mutableStateOf("") }
    var isOverFlowHandled by remember { mutableStateOf(false) }
    var fullText by remember { mutableStateOf(item.fullText) }


    Card(
        modifier = Modifier.height(215.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = {
                    navController.navigate("scrapnel/${item.timeStamp}")
                    Log.d("Navigation", "Navigating with timestamp: ${item.timeStamp}")

                })

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
                    val hasText = item.fullText.isNotBlank()

                    Row(
                        modifier = Modifier
                            .padding( horizontal = 5.dp)
                            .height(79.dp),
                    ) {
                        item.firstImageUri?.let { imageUri ->
                            Card(
                                modifier = Modifier
                                    .width(79.dp)
                                    .height(79.dp)
                                    .padding(top = 5.dp, end = 5.dp)
                            ) {

                                Image(
                                    painter = rememberAsyncImagePainter(model = imageUri),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                        }

                        Text(
                            text = firstTextViewText,
                            maxLines = 4,
                            overflow = TextOverflow.Clip,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyMedium,
                            onTextLayout = { textLayoutResult ->
                                if (!isOverFlowHandled && textLayoutResult.didOverflowHeight) {
                                    val lastVisibleCharIndex = textLayoutResult.getLineEnd(
                                        lineIndex = textLayoutResult.lineCount - 1,
                                        visibleEnd = true
                                    ).coerceAtMost(item.fullText.length)

                                    firstTextViewText =
                                        item.fullText.substring(0, lastVisibleCharIndex)
                                    secondTextViewText =
                                        item.fullText.substring(lastVisibleCharIndex)
                                    isOverFlowHandled = true
                                }
                            }

                        )
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

//                        val hasText = item.fullText.isNotBlank()

                    Text(
                        text = secondTextViewText,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .heightIn(min = 48.dp)
                    )


                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 6.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f))

                ) {
                    Text(
                        text = item.title,
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
            if (isDeleting) {
                Checkbox(
                    modifier = Modifier
                        .align(Alignment.TopEnd),
                    checked = isChecked,

                    onCheckedChange = onCheckedChange,
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
fun ChipsAndFilter(chipsList: List<String>, isFiltering: Boolean) {
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
                chipItems = chipsList,
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
    onFilterApplied: (String, String, String) -> Unit,
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
                        label = { Text("dd/mm/yyyy") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = titleInput.isEmpty(),

                        )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val filterValue = if (selectedFilter == "title") {
                            titleInput
                        } else {
                            convertDdMmYyyyToTimestamp(dateInput).toString().dropLast(8)
                        }

                        val filterDisplayText = if (selectedFilter == "title") {
                            titleInput
                        } else {
                            dateInput
                        }

                        if (filterValue.isNotBlank()) {
                            onFilterApplied(selectedFilter, filterValue, filterDisplayText)
                        }
                        titleInput = ""
                        dateInput = ""
                        onDismiss()
                    }
                ) {
                    Text("Apply Filter")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = {
                    onDismiss()
                    titleInput = ""
                    dateInput = ""

                }) {
                    Text("Cancel")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
        )
    }


}




