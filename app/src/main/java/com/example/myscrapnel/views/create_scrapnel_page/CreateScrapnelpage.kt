package com.example.myscrapnel.views.create_scrapnel_page


import android.os.Build
import android.util.Log
import android.widget.NumberPicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.room.Room
import com.example.myscrapnel.R
import com.example.myscrapnel.room_db.ScrapnelDatabase
import com.example.myscrapnel.room_db.ScrapnelEntity
import com.example.myscrapnel.utils.convertTimestampToDateTimeComponent
import com.example.myscrapnel.utils.convertTimestampToDateTimeComponents
import com.example.myscrapnel.utils.copyImageToInternalStorage
import com.example.myscrapnel.utils.extractDateFromTimestamp
import com.example.myscrapnel.utils.getTimestamp
import com.example.myscrapnel.viewmodels.MyScrapnelViewModel
import com.example.myscrapnel.viewmodels.MyScrapnelViewModelFactory
import com.example.myscrapnel.viewmodels.SaveScrapnelRepository
import kotlinx.coroutines.delay
import java.util.Calendar


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun CreateScrapnelPage(modifier: Modifier = Modifier,navController: NavController,itemToEditFromScrapnel: Long?) {
    val calendar = Calendar.getInstance()
    var year by remember { mutableStateOf("${calendar.get(Calendar.YEAR)}") }
    var month by remember { mutableStateOf("${calendar.get(Calendar.MONTH) + 1}") }
    var day by remember { mutableStateOf("${calendar.get(Calendar.DAY_OF_MONTH)}") }
    var hour by remember { mutableStateOf("00") }
    var minute by remember { mutableStateOf("00") }
    var title by remember { mutableStateOf("") }
    var scrapnelTextField by remember { mutableStateOf("") }
    var showTimePicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val database = Room.databaseBuilder(
        context,
        ScrapnelDatabase::class.java,
        "scrapnel_db"
    ).build()
    val repository = SaveScrapnelRepository(database.dao())
    val factory = MyScrapnelViewModelFactory(repository)
    val viewModel: MyScrapnelViewModel = viewModel(factory = factory)
    val itemToEdit = viewModel.theScrapnelToEdit.collectAsState().value
    val convertedTimeStamp = convertTimestampToDateTimeComponents(itemToEdit?.timeStamp ?: 0L)


    LaunchedEffect(itemToEditFromScrapnel)
    {
        if (itemToEditFromScrapnel != null) {
           viewModel.loadTheScrapnelToEdit(itemToEditFromScrapnel)
        }
    }


    LaunchedEffect(itemToEdit)
    {
        if (itemToEdit != null)
        {
            title = itemToEdit.title
            scrapnelTextField = itemToEdit.content
            year = convertedTimeStamp[2]
            month = convertedTimeStamp[1]
            day = convertedTimeStamp[0]
            hour = convertedTimeStamp[3]
            minute = convertedTimeStamp[4]

        }
    }



    Column(modifier = Modifier.fillMaxSize()) {
        Header(modifier = modifier,navController)

        Main(
            year = year,
            month = month,
            day = day,
            hour = hour,
            minute = minute,
            onTimeClick = { showTimePicker = true },
            onDateClick = { showDatePicker = true },
            viewModel = viewModel,
            titlee = title,
            scrapnelTextFieldToEdit = scrapnelTextField,
            itemToEdit = itemToEdit,
            navController
        )
    }

    TimeSelectDialog(
        isShowDialog = showTimePicker,
        onDismiss = { showTimePicker = false },
        onTimeSelected = { selectedHour, selectedMinute ->
            hour = "%02d".format(selectedHour)
            minute = "%02d".format(selectedMinute)
        }
    )

    DateSelectDialog(
        isShowDialog = showDatePicker,
        onDismiss = { showDatePicker = false },
        onDateSelected = { selectedYear, selectedMonthIndex, selectedDay ->
            year = "$selectedYear"
            month = "${selectedMonthIndex + 1}"
            day = "$selectedDay"
        }
    )
}

@Composable
private fun Header(
    modifier: Modifier,
    navController: NavController
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
        IconButton(onClick = {  navController.navigate("home") {
        }}) {
            Icon(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = " My Scrapnel",
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
private fun Main(
    year: String,
    month: String,
    day: String,
    hour: String,
    minute: String,
    onTimeClick: () -> Unit,
    onDateClick: () -> Unit,
    viewModel: MyScrapnelViewModel,
    titlee: String,
    scrapnelTextFieldToEdit: String,
    itemToEdit: ScrapnelEntity? = null,
    navController: NavController

) {
//    var title by remember { mutableStateOf(titlee) }
//    var scrapnelTextField by remember { mutableStateOf(TextFieldValue(scrapnelTextFieldToEdit)) }
    var title by remember(titlee) { mutableStateOf(titlee) }
    var scrapnelTextField by remember(scrapnelTextFieldToEdit) { mutableStateOf(TextFieldValue(scrapnelTextFieldToEdit)) }

    var isText by remember { mutableStateOf(true) }
    var isImage by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }
    var isPreview by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    val monthName = months[month.toInt()-1].name
    val context = LocalContext.current
    val timestamp = getTimestamp(year, month, day, hour, minute)

    val scrapnelEntity = ScrapnelEntity(
        itemToEdit?.timeStamp ?: timestamp,
        title,
        scrapnelTextField.text,
        itemToEdit?.createdAt ?: System.currentTimeMillis()
    )


    val pickImages =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(3)) { uris ->
            if (uris.isNotEmpty()) {
                val imagePaths = uris.mapNotNull { uri ->
                    copyImageToInternalStorage(context, uri)
                }

                if (imagePaths.isNotEmpty()) {
                    val imageUrisText = imagePaths.joinToString(separator = "\n") { path -> "🖼️ file://$path" }

                    val newText = scrapnelTextField.text + "\n$imageUrisText\n"
                    scrapnelTextField = TextFieldValue(
                        text = newText,
                        selection = TextRange(newText.length) // Cursor at end
                    )

                }
            }
        }

    var isSaveFailed by remember { mutableStateOf(false) }
    val saveResult by viewModel.saveResult.collectAsState()
    var showSuccessDialog by remember { mutableStateOf(false) }
    var foundTimeStamp by remember { mutableStateOf<Long?>(null) }
    Log.d("FoundTimeStamp:", "FoundTimeStamp: $foundTimeStamp")

    var isEditing by remember { mutableStateOf(false) }

    LaunchedEffect(isEditing) {
        if (isEditing) {
            viewModel.loadTheScrapnelToEdit(foundTimeStamp!!)

        }
    }

    LaunchedEffect(saveResult) {
        saveResult.onSuccess { saved ->
            if (saved.result == true) {
                showSuccessDialog = true
                delay(2000)
                showSuccessDialog = false
                navController.navigate("home")
            }
        }.onFailure { error ->
            val message = error.message
            if (message?.startsWith("Conflict:") == true) {
                foundTimeStamp = message.removePrefix("Conflict:").toLongOrNull()
                isSaveFailed = true
            } else {

                isSaveFailed = true
            }
        }
    }



    Column(modifier = Modifier.padding(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(
                modifier = Modifier
                    .weight(0.5f)
            ) {
                OutlinedTextField(
                    value = year,
                    onValueChange = {},
                    label = { Text("Year") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Box(modifier = Modifier
                    .clickable { onDateClick() }
                    .matchParentSize())
                {

                }
            }

            Box(
                modifier = Modifier
                    .weight(0.5f)
            ) {
                OutlinedTextField(
                    value = month,
                    onValueChange = {},
                    label = { Text("Month") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Box(modifier = Modifier
                    .clickable { onDateClick() }
                    .matchParentSize())
                {

                }
            }

            Box(
                modifier = Modifier
                    .weight(0.5f)
            ) {
                OutlinedTextField(
                    value = day,
                    onValueChange = {},
                    label = { Text("Day") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Box(modifier = Modifier
                    .clickable { onDateClick() }
                    .matchParentSize())
                {

                }

            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(top = 16.dp)
        ) {

            Box(
                modifier = Modifier
                    .weight(0.25f)
            ) {
                OutlinedTextField(
                    value = hour,
                    onValueChange = {},
                    label = { Text("Hour") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )
                Box(modifier = Modifier
                    .clickable { onTimeClick() }
                    .matchParentSize())
            }
            Box(
                modifier = Modifier
                    .weight(0.25f)
            ) {
                OutlinedTextField(
                    value = minute,
                    onValueChange = {},
                    label = { Text("Minute") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )
                Box(modifier = Modifier
                    .clickable { onTimeClick() }
                    .matchParentSize())
                {

                }
            }
        }

        val maxTitleLength = 27

        OutlinedTextField(
            value = title,
            onValueChange = {
                if (it.length <= maxTitleLength) {
                    title = it
                }
            },
            label = { Text("Title") },
            modifier = Modifier.padding(top = 16.dp),
            maxLines = 1,
        )


        Box(modifier = Modifier.padding(top = 16.dp)
        )
        {
            val focusManager = LocalFocusManager.current
            val focusRequester = remember { FocusRequester() }


            OutlinedTextField(
                value = scrapnelTextField,
                onValueChange = { scrapnelTextField = it },
                label = { Text("Write Scrapnel") },
                textStyle = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp).focusRequester(focusRequester)
                ,
                singleLine = false
            )


            Row(modifier = Modifier.align(androidx.compose.ui.Alignment.BottomEnd)) {
                    IconButton(onClick = { isText = !isText
                        if (!isText)
                        {
                            focusManager.clearFocus()
                        }
                        else{
                            focusRequester.requestFocus()
                        }

                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_text),
                            contentDescription = "Camera",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(20.dp)
                        )

                    }
                    IconButton(onClick = {isImage= ! isImage
                        isText = false
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_image),
                            contentDescription = "Location",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(onClick = {  if (title.isBlank() || scrapnelTextField.text.isBlank()) {
                        showErrorDialog = true
                    } else {
                        isSaving = !isSaving
                    }}) {
                        Icon(
                            painter = painterResource(R.drawable.ic_save),
                            contentDescription = "Link",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(onClick = { if (title.isBlank() || scrapnelTextField.text.isBlank()) {
                        showErrorDialog = true
                    } else {
                        isPreview = true
                    }
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_preview),
                            contentDescription = "Emoji",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            if (isImage) {
                Box(
                    modifier = Modifier
                        .align(androidx.compose.ui.Alignment.Center)
                        .size(200.dp, 150.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xAA000000)),
                    contentAlignment = androidx.compose.ui.Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                    ) {
                        IconButton(onClick = {
                            pickImages.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                            isImage = false
                        }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Select Image",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        Text(
                            text = "Select Images",
                            modifier = Modifier.padding(top = 4.dp),
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }
                }
            }

            if (isPreview) {
               PreviewScrapnel(
                   title = title,
                   scrapnelText = scrapnelTextField.text,
                   year = year,
                   month = monthName,
                   day = day,
                   hour = hour,
                   minute = minute,
                   onDismiss = { isPreview = false },
               )
            }

            if (showErrorDialog) {
                AlertDialog(
                    onDismissRequest = { showErrorDialog = false },
                    confirmButton = {
                        Button(onClick = { showErrorDialog = false }) {
                            Text("OK")
                        }
                    },
                    title = { Text("Validation Error", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground) },
                    text = { Text("Enter All Fields", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground) },
                    containerColor = MaterialTheme.colorScheme.background
                )
            }
        }

        FilledTonalButton(
            shape = RoundedCornerShape(16.dp),
            onClick = {
                if (title.isBlank() || scrapnelTextField.text.isBlank()) {
                    showErrorDialog = true
                } else {
                    isSaving = true
                    if (itemToEdit != null) {
                        viewModel.updateScrapnel(scrapnelEntity)
                    } else {
                        viewModel.saveScrapnel(scrapnelEntity)
                    }

                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 36.dp),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = "Save Scrapnel",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
    if (isSaveFailed) {
        AlertDialog(
            onDismissRequest = { isSaveFailed = false },
            confirmButton = {
                TextButton(onClick = { isSaveFailed = false }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { isSaveFailed = false
                    viewModel.loadTheScrapnelToEdit(foundTimeStamp!!)
                    isEditing =true
                }) {
                    Text("Edit")
                }
            },
            title = { Text("Sorry can't save this") },
            text = { Text("Please change the time \nalready saved a scrapnel in this time gap") },
            containerColor = MaterialTheme.colorScheme.background,
            textContentColor = MaterialTheme.colorScheme.onBackground
        )
    }
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            confirmButton = {
                TextButton(onClick = { showSuccessDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text("Saved Successfully") },
            text = { Text("Your Scrapnel was saved.") },
            containerColor = MaterialTheme.colorScheme.background,
            textContentColor = MaterialTheme.colorScheme.onBackground
        )
    }

}



@Composable
fun DateSelectDialog(
    isShowDialog: Boolean,
    onDismiss: () -> Unit,
    onDateSelected: (year: Int, monthIndex: Int, day: Int) -> Unit
) {
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    var selectedDay by remember { mutableStateOf(currentDay) }
    var selectedMonthIndex by remember { mutableStateOf(currentMonth) }
    var selectedYear by remember { mutableStateOf(currentYear) }

    var days by remember {
        mutableStateOf((1..months[selectedMonthIndex].numberOfDays).toList())
    }

    val monthList = months.map { it.name }
    val years = (1970..2025).toList()

    if (isShowDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                IconButton(onClick = {
                    onDateSelected(selectedYear, selectedMonthIndex, selectedDay)
                    onDismiss()
                }, modifier = Modifier.padding(end = 16.dp)) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null,  tint = MaterialTheme.colorScheme.onBackground)
                }
            },
            dismissButton = {
                IconButton(onClick = { onDismiss() }, modifier = Modifier.padding(end = 16.dp)) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = null,  tint = MaterialTheme.colorScheme.onBackground)
                }
            },
            text = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    NumberPickerView(
                        value = selectedYear,
                        range = years,
                        onValueChange = { selectedYear = it }
                    )
                    NumberPickerStringView(
                        displayedValues = monthList,
                        value = selectedMonthIndex,
                        onValueChange = {
                            selectedMonthIndex = it
                            days = (1..months[it].numberOfDays).toList()
                            if (selectedDay > days.last()) selectedDay = days.last()
                        }
                    )
                    NumberPickerView(
                        value = selectedDay,
                        range = days,
                        onValueChange = { selectedDay = it }
                    )

                }
            },
            containerColor = MaterialTheme.colorScheme.background
        )
    }
}


@Composable
fun TimeSelectDialog(
    isShowDialog: Boolean,
    onDismiss: () -> Unit,
    onTimeSelected: (hour: Int, minute: Int) -> Unit
) {
    var selectedHour by remember { mutableStateOf(0) }
    var selectedMinute by remember { mutableStateOf(0) }

    val hourStrings = (0..23).map { it.toString().padStart(2, '0') }
    val minuteStrings = (0..59).map { it.toString().padStart(2, '0') }

    if (isShowDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                IconButton(onClick = {
                    onTimeSelected(selectedHour, selectedMinute)
                    onDismiss()
                }, modifier = Modifier.padding(end = 16.dp)) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground)
                }
            },
            dismissButton = {
                IconButton(onClick = { onDismiss() }, modifier = Modifier.padding(end = 16.dp)) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground)
                }
            },
            text = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    NumberPickerStringView(
                        displayedValues = hourStrings,
                        value = selectedHour,
                        onValueChange = { selectedHour = it }
                    )

                    NumberPickerStringView(
                        displayedValues = minuteStrings,
                        value = selectedMinute,
                        onValueChange = { selectedMinute = it }
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        )
    }
}


@Composable
fun NumberPickerStringView(
    displayedValues: List<String>,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    val context = LocalContext.current
    AndroidView(
        factory = { ctx ->
            NumberPicker(context).apply {
                minValue = 0
                maxValue = displayedValues.size - 1
                this.value = value

                setOnValueChangedListener { _, _, newVal ->
                    onValueChange(newVal)
                }
                setPadding(16, 8, 8, 8)
            }
        },
        update = { picker ->
            picker.displayedValues = displayedValues.toTypedArray()
            picker.value = value
            picker.setPadding(0, 2, 0, 2)

        },
        modifier = Modifier.width(100.dp)
    )
}

@Composable
fun NumberPickerView(value: Int, range: List<Int>, onValueChange: (Int) -> Unit) {
    val context = LocalContext.current
    AndroidView(
        factory = { ctx ->
            NumberPicker(context).apply {
                minValue = range.first()
                maxValue = range.last()
                this.value = value

                setOnValueChangedListener { _, _, newVal ->
                    onValueChange(newVal)
                }
            }
        },
        update = { picker ->
            picker.minValue = range.first()
            picker.maxValue = range.last()
            picker.value = value
        },
        modifier = Modifier.width(35.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun ShowCreateScrapnelPage() {
    TimeSelectDialog(isShowDialog = true, onDismiss = {}, onTimeSelected = { _, _ -> })

}

data class Months(val id: Int, val name: String, val numberOfDays: Int)

val months = listOf(
    Months(1, "Jan", 31),
    Months(2, "Feb", 28),
    Months(3, "Mar", 31),
    Months(4, "Apr", 30),
    Months(5, "May", 31),
    Months(6, "Jun", 30),
    Months(7, "Jul", 31),
    Months(8, "Aug", 31),
    Months(9, "Sep", 30),
    Months(10, "Oct", 31),
    Months(11, "Nov", 30),
    Months(12, "Dec", 31)
)


