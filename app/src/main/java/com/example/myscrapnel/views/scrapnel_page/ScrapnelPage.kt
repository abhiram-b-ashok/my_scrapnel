package com.example.myscrapnel.views.scrapnel_page

import android.net.Uri
import android.os.Build
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.room.Room
import coil.compose.AsyncImage
import com.example.myscrapnel.R
import com.example.myscrapnel.room_db.ScrapnelDatabase
import com.example.myscrapnel.room_db.ScrapnelEntity
import com.example.myscrapnel.utils.convertTimestampToDateTimeComponent
import com.example.myscrapnel.utils.convertTimestampToDateTimeComponents
import com.example.myscrapnel.viewmodels.ViewScrapnelRepository
import com.example.myscrapnel.viewmodels.ViewScrapnelViewModel
import com.example.myscrapnel.viewmodels.ViewScrapnelViewModelFactory


@Composable
fun ScrapnelPage(modifier: Modifier =Modifier, navController: NavController, timestamp: Long?)
{
    val context = LocalContext.current
    val database = Room.databaseBuilder(
        context,
        ScrapnelDatabase::class.java,
        "scrapnel_db"
    ).build()
    val scrapnelRepository = ViewScrapnelRepository(database.dao())
    val scrapnelViewModelFactory = ViewScrapnelViewModelFactory(scrapnelRepository)
    val scrapnelViewModel : ViewScrapnelViewModel = viewModel(factory = scrapnelViewModelFactory)
    val scrapnel by scrapnelViewModel.scrapnel.collectAsState()
    LaunchedEffect(Unit) {
       scrapnelViewModel.loadScrapnel(timestamp)
    }

    Column {
        Header(modifier, navController, timestamp)
        Main(scrapnel)
    }

}

@Composable
private fun Header(
    modifier: Modifier,
    navController: NavController,
    timestamp: Long?
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

        IconButton(onClick = { navController.navigate("create?timestamp=$timestamp") }) {
            Icon(
                painter = painterResource(R.drawable.ic_edit),
                contentDescription = "Edit",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(24.dp)
            )
        }

    }
}

@Composable
private fun Main(scrapnel: ScrapnelEntity?) {
    val timestamp = scrapnel?.timeStamp
    val dateAndTime = convertTimestampToDateTimeComponent(timestamp)
    val title = scrapnel?.title
    val content = scrapnel?.content

    var year by remember { mutableStateOf(dateAndTime[2]) }
    var month by remember { mutableStateOf(dateAndTime[1]) }
    var day by remember { mutableStateOf(dateAndTime[0]) }
    var hour by remember { mutableStateOf(dateAndTime[3]) }
    var minute by remember { mutableStateOf(dateAndTime[4]) }
    val imagesList = mutableListOf<Uri>()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(

                text = if (title.isNullOrEmpty()) "Untitled" else title,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .weight(1f),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

        }
        Row(modifier = Modifier.padding(bottom = 10.dp)) {
            Text(
                text = "$month, ",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "$day, ",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = year,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "$hour:",
                modifier = Modifier.padding(start = 5.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = minute,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        content?.lines()?.forEach { line ->
            if (line.isBlank()) {
                return@forEach
            }

            if (line.contains("🖼️")) {
                imagesList.add(line.drop(4).toUri())

                if (imagesList.size == 3) {
                    ImageStack(images = imagesList)
                    imagesList.clear()
                }
            } else {
                if (imagesList.isNotEmpty()) {
                    ImageStack(images = imagesList)
                    imagesList.clear()
                }

                Text(
                    text = line,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        if (imagesList.isNotEmpty()) {
            ImageStack(images = imagesList)
        }
        }

    }

@Composable
private fun ImageStack(images: List<Uri>) {

//    val rearrangedImages = remember {images.toMutableList()}
    val rearrangedImages = remember {
        mutableStateListOf<Uri>().apply { addAll(images) }
    }

    val imageHeight = 150.dp
    val offsetPerImage = 20.dp
    val maxHeight = imageHeight + (rearrangedImages.size - 1) * offsetPerImage
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(maxHeight)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        rearrangedImages.forEachIndexed { index, uri ->

            val offsetAmount = (rearrangedImages.size - 1 - index) * 20.dp
            val rotationAngle = (rearrangedImages.size - 1 - index) * 10f

            val isTop = index == rearrangedImages.lastIndex

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                ImageCard(
                    uri = uri,
                    contentDescription = "$uri",
                    modifier = Modifier
                        .offset(y = -offsetAmount, x = -offsetAmount)
                        .graphicsLayer { rotationZ = -rotationAngle }
                        .then(if (isTop) Modifier.clickable {
                            val top = rearrangedImages.removeLast()
                            rearrangedImages.add(0, top)
                        }
                        else Modifier)
                )
            } else { ImageCard(
                uri = uri,
                contentDescription = "$uri",
                modifier = Modifier
                    .offset(y = -offsetAmount, x = -offsetAmount)
                    .graphicsLayer { rotationZ = -rotationAngle }
            )}


        }
    }
}

@Composable
private fun ImageCard(
    uri: Uri,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(0.35f)
            .height(130.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box {
            AsyncImage(
                model = uri,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }
    }
}
