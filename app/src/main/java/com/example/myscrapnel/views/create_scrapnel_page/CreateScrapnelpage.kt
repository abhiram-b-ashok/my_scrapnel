package com.example.myscrapnel.views.create_scrapnel_page

import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myscrapnel.R
import java.util.Calendar


@Composable
fun CreateScrapnelPage(modifier: Modifier = Modifier) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Header(
            modifier = modifier,
        )

        Main(modifier = modifier)


    }

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
                modifier = Modifier.size(18.dp)
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
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun Main(modifier: Modifier) {
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    val year by remember { mutableStateOf("$currentYear") }
    val month by remember { mutableStateOf("${currentMonth+1}") }
    val day by remember { mutableStateOf("$currentDay") }
    val hour by remember { mutableStateOf("00") }
    val minute by remember { mutableStateOf("00") }


    Column(modifier = Modifier.padding(16.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedTextField(
                onValueChange = {}, value = year,
                label = { Text("Year") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                onValueChange = {}, value = month,
                label = { Text("Month") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                onValueChange = {}, value = day,
                label = { Text("Day") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                onValueChange = {}, value = hour,
                label = { Text("Hour") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                onValueChange = {}, value = minute,
                label = { Text("Minute") },
                modifier = Modifier.weight(1f)

            )
        }
        OutlinedTextField(
            onValueChange = {}, value = "Title",
            label = { Text("Title", maxLines = 1) },
            modifier = Modifier.padding(top = 16.dp)

        )
        Box()
        {

            OutlinedTextField(
                onValueChange = {}, value = "Write Scrapnel",
                label = { Text("Write Scarpnel") },
                modifier = Modifier.padding(top = 16.dp).fillMaxWidth().height(300.dp)
            )

            Row {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_text),
                        contentDescription = "Camera",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(20.dp)
                    )

                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_image),
                        contentDescription = "Location",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_save),
                        contentDescription = "Link",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_preview),
                        contentDescription = "Emoji",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }


    }
}