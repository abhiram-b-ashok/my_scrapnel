package com.example.myscrapnel.views.create_scrapnel_page

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text


@Composable
fun PreviewScrapnelDialog(
    title: String,
    scrapnelText: String,
    year: String,
    month: String,
    day: String,
    hour: String,
    minute: String,
    onDismiss: () -> Unit,
    onSaveClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                onSaveClick()
                onDismiss()
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = {
            Text("Preview Scrapnel")
        },
        text = {
            Column {
                Text("Title: $title")
                Text("Date: $day/$month/$year")
                Text("Time: $hour:$minute")
                Text("Content:")
                Text(scrapnelText)

                if (scrapnelText.contains("🖼️")) {
                    Text("Images embedded in scrapnel text 👇")
                    scrapnelText.lines().filter { it.contains("🖼️") }.forEach {
                        Text(it, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    )
}
