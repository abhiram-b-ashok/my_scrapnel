package com.example.myscrapnel.models.scrapnel_tf_content

data class ScrapnelUiModel(
    val title: String,
    val fullText: String,
    val firstImageUri: String?,
    val timeStamp: Long,
    val createdAt: Long
)
