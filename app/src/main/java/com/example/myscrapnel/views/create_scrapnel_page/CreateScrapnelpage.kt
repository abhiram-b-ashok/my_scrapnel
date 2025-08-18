package com.example.myscrapnel.views.create_scrapnel_page

import android.R.attr.label
import android.R.attr.text
import android.net.Uri
import android.util.Log
import android.widget.NumberPicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myscrapnel.R
import java.util.Calendar
import java.util.stream.Collectors.toList
import kotlin.time.Duration.Companion.days


@Composable
fun CreateScrapnelPage(modifier: Modifier = Modifier) {
    val calendar = Calendar.getInstance()
    var year by remember { mutableStateOf("${calendar.get(Calendar.YEAR)}") }
    var month by remember { mutableStateOf("${calendar.get(Calendar.MONTH) + 1}") }
    var day by remember { mutableStateOf("${calendar.get(Calendar.DAY_OF_MONTH)}") }
    var hour by remember { mutableStateOf("00") }
    var minute by remember { mutableStateOf("00") }


    var showTimePicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Header(modifier = modifier)

        Main(
            year = year,
            month = month,
            day = day,
            hour = hour,
            minute = minute,
            onTimeClick = { showTimePicker = true },
            onDateClick = { showDatePicker = true }
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
        IconButton(onClick = { }) {
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
        IconButton(onClick = { }) {
            Icon(
                painter = painterResource(R.drawable.ic_cancel),
                contentDescription = "Cancel",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun Main(
    year: String,
    month: String,
    day: String,
    hour: String,
    minute: String,
    onTimeClick: () -> Unit,
    onDateClick: () -> Unit

) {
    var title by remember { mutableStateOf("") }

    var isSaving by remember { mutableStateOf(false) }
    var isText by remember { mutableStateOf(true) }
    var isImage by remember { mutableStateOf(false) }
    var isPreview by remember { mutableStateOf(false) }
    var scrapnelTextField by remember { mutableStateOf("") }






//    val pickImages =
//        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(3)) { uris ->
//            if (uris.isNotEmpty()) {
//                Log.d("PhotoPicker", "Number of items selected: ${uris.size}")
//                imagesLists.clear()
//                imagesLists.addAll(uris)
//                val imageUrisText = uris.joinToString(separator = ", ")
//                imageText = imageUrisText
//                Log.d("PhotoPicker", "Selected URIs: $imagesLists")
//            } else {
//                Log.d("PhotoPicker", "No media selected")
//            }
//        }

    val pickImages =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(3)) { uris ->
            if (uris.isNotEmpty()) {
                val imageUrisText = uris.joinToString(separator = "\n") { uri -> "🖼️ $uri" }
                // Append to the existing text
                scrapnelTextField += "\n$imageUrisText\n"


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

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title", maxLines = 1) },
            modifier = Modifier.padding(top = 16.dp)
        )

        Box(modifier = Modifier                    .padding(top = 16.dp)
        )
        {
//                OutlinedTextField(
//
//                    onValueChange = { imageText = it }, value = imageText,
//                    label = { Text("Write Scarpnel") },
//                    modifier = Modifier
//                        .padding(top = 16.dp)
//                        .fillMaxWidth()
//                        .height(250.dp)
//                )
            OutlinedTextField(
                value = scrapnelTextField,
                onValueChange = { scrapnelTextField = it },
                label = { Text("Write Scrapnel") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                singleLine = false
            )




            Row(modifier = Modifier.align(androidx.compose.ui.Alignment.BottomEnd)) {
                    IconButton(onClick = { isText = !isText }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_text),
                            contentDescription = "Camera",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(20.dp)
                        )

                    }
                    IconButton(onClick = {isImage= ! isImage }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_image),
                            contentDescription = "Location",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(onClick = { isSaving= ! isSaving }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_save),
                            contentDescription = "Link",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(onClick = { isPreview = !isPreview }) {
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
                PreviewScrapnelDialog(
                    title = title,
                    scrapnelText = scrapnelTextField,
                    year = year,
                    month = month,
                    day = day,
                    hour = hour,
                    minute = minute,
                    onDismiss = { isPreview = false },
                    onSaveClick = {
                        // Call your DB insert function here
                        isSaving = true
                        // Maybe reset fields after saving?
                    }
                )
            }



        }

        FilledTonalButton(
            shape = RoundedCornerShape(16.dp),
            onClick = {}
            ,
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


