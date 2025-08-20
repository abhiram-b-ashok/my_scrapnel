package com.example.myscrapnel.models.scrapnel_tf_content




//viewModelScope.launch {
//    myScrapnelDao.insertScrapnel(
//        Scrapnel(
//            title = title,
//            content = scrapnelTextField,
//            timestamp = getDateTimeString(year, month, day, hour, minute)
//        )
//    )
//}

//@Entity
//data class Scrapnel(
//    @PrimaryKey(autoGenerate = true) val id: Long = 0,
//    val title: String,
//    val timestamp: String
//)
//@Entity(
//    foreignKeys = [ForeignKey(
//        entity = Scrapnel::class,
//        parentColumns = ["id"],
//        childColumns = ["scrapnelId"],
//        onDelete = ForeignKey.CASCADE
//    )]
//)
//data class ScrapnelContentItem(
//    @PrimaryKey(autoGenerate = true) val id: Long = 0,
//    val scrapnelId: Long,
//    val contentType: ContentType, // TEXT or IMAGE
//    val content: String, // Either text or image URI
//    val orderIndex: Int
//)
//
//enum class ContentType {
//    TEXT, IMAGE
//}
//@Query("SELECT * FROM ScrapnelContentItem WHERE scrapnelId = :scrapnelId ORDER BY orderIndex ASC")
//suspend fun getContentItemsForScrapnel(scrapnelId: Long): List<ScrapnelContentItem>
//
data class ScrapnelUiModel(
    val title: String,
    val fullText: String,
    val firstImageUri: String?, // null if no image
    val timeStamp: Long,
    val createdAt: Long
)
