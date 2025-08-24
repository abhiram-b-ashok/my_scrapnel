package com.example.myscrapnel.models.scrapnel_ui_model

data class ScrapnelUiModel(
    val title: String,
    val fullText: String,
    val imageUris: List<String>?,
    val timeStamp: Long,
    val createdAt: Long,
    var isChecked: Boolean = false
)
