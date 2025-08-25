package com.example.myscrapnel.views.home_page


import android.R.attr.contentDescription
import android.R.attr.maxLines
import android.R.attr.text
import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.util.Log.e
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.core.net.toUri
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
import kotlin.collections.forEachIndexed


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
            isDeleting,
            scrapnelViewModel
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
    isDeleting: Boolean,
    viewScrapnelViewModel: ViewScrapnelViewModel
) {

    val selectedItems by viewScrapnelViewModel.selectedItemsToDelete.collectAsState()

    val hasSelectedItems = selectedItems.isNotEmpty()


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                PaddingValues(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                    start = 10.dp,
                    end = 10.dp,
                    bottom = 5.dp
                )
            )
            .border(BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground))
            .background(MaterialTheme.colorScheme.primary),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Text(
            text = title,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.headlineLarge
        )
        Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)) {

            if (isDeleting && hasSelectedItems) {
                Text(
                    text = "Delete",
                    modifier = Modifier
                        .clickable {
                            scrapnelViewModel.deleteSelectedItems()
                            stopDeleteMode()
                        },
                    color = Color.Red
                )
            }


            IconButton(onClick = {
                if (hasSelectedItems)
                {
                    scrapnelViewModel.clearSelectedItems()
                    stopDeleteMode()

                }
                startDelete()
            },
                modifier = Modifier.size(36.dp)) {
                Icon(
                    painter = painterResource(if (isDeleting) R.drawable.ic_delete_red else R.drawable.ic_delete),
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            if (!isFiltering) {
                IconButton(onClick = { showDialog()
                    stopDeleteMode()
                },
                    modifier = Modifier.size(36.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.ic_filter),
                        contentDescription = "Filter",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {

                Text(
                    text = "Clear",
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clickable {
                            scrapnelViewModel.loadScrapnels()
                            scrapnelViewModel.loadChipTitles()
                            onClearFilter()

                        },
                    color = MaterialTheme.colorScheme.onPrimary
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
        ChipsAndFilter(chipTitles, isFiltering, scrapnelViewModel)
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
        delay(1000)
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
        if (!isDataLoaded) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (isLoading) {
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
    LaunchedEffect(isDeleting) {
        if (!isDeleting) {
            selectedItems.clear()
        }
    }

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
    var showDeleteOverlay by remember { mutableStateOf(false) }

    LaunchedEffect(isChecked, isDeleting) {
        showDeleteOverlay = isChecked && isDeleting
    }

    Card(
        modifier = Modifier.height(215.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        border = BorderStroke(5.dp, MaterialTheme.colorScheme.onBackground)
    ) {
        Box(
            modifier = if (!isDeleting) Modifier
                .fillMaxSize()
                .clickable(onClick = {
                    navController.navigate("scrapnel/${item.timeStamp}")
                    Log.d("Navigation", "Navigating with timestamp: ${item.timeStamp}")

                }) else Modifier.fillMaxSize()

        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(12.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    val hasImage = !item.imageUris.isNullOrEmpty()
                    val hasText = item.fullText.isNotBlank()
                    val hasMultipleImages = item.imageUris?.size!! > 1
                    val uriList = item.imageUris.map { it.toUri() }



                    if (hasImage && hasMultipleImages && !hasText) {
                        MiniImageStack(uriList.take(3))
                    } else if (hasImage && hasText) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                                .height(79.dp),
                            horizontalArrangement = Arrangement.Absolute.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Card(
                                modifier = Modifier
                                    .width(79.dp)
                                    .height(79.dp)
                                    .padding(top = 5.dp, end = 5.dp),
                                shape = RoundedCornerShape(0.dp)

                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = item.imageUris[0]),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color = Color.White)
                                        .clip(RoundedCornerShape(0.dp))
                                        .border(
                                            BorderStroke(
                                                2.dp,
                                                MaterialTheme.colorScheme.onBackground
                                            )
                                        )
                                        .padding(5.dp),
                                    contentScale = ContentScale.FillBounds
                                )
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
                    } else if (hasImage) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(129.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Card(
                                modifier = Modifier
                                    .width(79.dp)
                                    .height(79.dp)
                                    .padding(5.dp),
                                shape = RoundedCornerShape(0.dp)
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = item.imageUris[0]),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color = Color.White)
                                        .clip(RoundedCornerShape(0.dp))
                                        .border(
                                            BorderStroke(
                                                2.dp,
                                                MaterialTheme.colorScheme.onBackground
                                            )
                                        )
                                        .padding(5.dp),
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                        }
                    } else if (hasText) {
                        val height = if (secondTextViewText.isNotBlank()) 79.dp else 129.dp
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(height),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = firstTextViewText,
                                maxLines = 4,
                                overflow = TextOverflow.Clip,
                                modifier = Modifier
                                    .padding(horizontal = 12.dp),
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
                    }


                    if (secondTextViewText.isNotBlank()) {
                        Text(
                            text = secondTextViewText,
                            maxLines = 5,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                                .heightIn(min = 53.dp)
                        )
                    }


                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp)
                        .clip(RoundedCornerShape(0.dp))
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.75f),
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                                )
                            )
                        )
                        .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary))

                ) {
                    Text(
                        text = item.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center,
                        maxLines = 1, overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge.copy(drawStyle = Stroke(width = 4f))
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

            if (showDeleteOverlay) {
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
fun ChipsAndFilter(
    chipsList: List<String>,
    isFiltering: Boolean,
    scrapnelViewModel: ViewScrapnelViewModel
) {
    var isFiltering by remember { mutableStateOf(false) }
    var selectedChip by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(selectedChip) {
        if (selectedChip != null) {
            scrapnelViewModel.loadScrapnelsByTitle(selectedChip!!)
        } else {
            scrapnelViewModel.loadScrapnels()
        }
    }

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
                    containerColor = MaterialTheme.colorScheme.background,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    labelColor = MaterialTheme.colorScheme.onBackground,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
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
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)

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

@Composable
fun MiniImageStack(images: List<Uri>) {

    Box(
        modifier = Modifier
            .height(129.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        images.forEachIndexed { index, uri ->
            val offsetY = (images.size - 1 - index) * 10.dp
            val offsetX = (images.size - 1 - index) * 5.dp
            val rotation = (images.size - 1 - (index - 1)) * 15f

            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .offset(y = -offsetY, x = -offsetX)
                    .graphicsLayer { rotationZ = -rotation }
                    .size(80.dp)
                    .background(color = Color.White)
                    .clip(RoundedCornerShape(0.dp))
                    .border(BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground))
                    .padding(5.dp),

                )
        }
    }
}




