package com.example.myscrapnel.views.scrapnel_page

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import coil.Coil
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myscrapnel.R
import com.example.myscrapnel.room_db.ScrapnelDatabase
import com.example.myscrapnel.room_db.ScrapnelEntity
import com.example.myscrapnel.utils.convertTimestampToDateTimeComponents
import com.example.myscrapnel.utils.formatTimestampToString
import com.example.myscrapnel.viewmodels.ViewScrapnelRepository
import com.example.myscrapnel.viewmodels.ViewScrapnelViewModel
import com.example.myscrapnel.viewmodels.ViewScrapnelViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun ScrapnelPage(modifier: Modifier = Modifier, navController: NavController, timestamp: Long?) {
    val context = LocalContext.current
    val database = Room.databaseBuilder(
        context,
        ScrapnelDatabase::class.java,
        "scrapnel_db"
    ).build()
    val scrapnelRepository = ViewScrapnelRepository(database.dao())
    val scrapnelViewModelFactory = ViewScrapnelViewModelFactory(scrapnelRepository)
    val scrapnelViewModel: ViewScrapnelViewModel = viewModel(factory = scrapnelViewModelFactory)
    val scrapnel by scrapnelViewModel.scrapnel.collectAsState()

    LaunchedEffect(Unit) {
        println("ScrapnelPage received timestamp: $timestamp")
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
            .border(BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground))
    ) {
        IconButton(onClick = {
            navController.navigate("home") {
            }
        }) {
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
    val scrollState = rememberScrollState()

    val timestamp = scrapnel?.timeStamp
    val title = scrapnel?.title
    val content = scrapnel?.content
    val imagesList = mutableListOf<Uri>()
    val date = formatTimestampToString(timestamp)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
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
                text = "$date, ",
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

    if (images.size == 1) {
        SingleImageView(images.first())
    } else {
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
                } else {
                    ImageCard(
                        uri = uri,
                        contentDescription = "$uri",
                        modifier = Modifier
                            .offset(y = -offsetAmount, x = -offsetAmount)
                            .graphicsLayer { rotationZ = -rotationAngle }
                    )
                }


            }
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
        shape = RoundedCornerShape(0.dp)
    ) {
        Box {
            AsyncImage(
                model = uri,
                contentDescription = contentDescription,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .clip(RoundedCornerShape(0.dp))
                    .border(BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground))
                    .padding(5.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun SingleImageView(uri: Uri) {

    val context = LocalContext.current
    var isPortrait by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(uri) {
        withContext(Dispatchers.IO) {
            try {
                val request = ImageRequest.Builder(context)
                    .data(uri)
                    .allowHardware(false)
                    .build()

                val result = Coil.imageLoader(context).execute(request)
                val drawable = result.drawable
                drawable?.let {
                    val width = it.intrinsicWidth
                    val height = it.intrinsicHeight

                    isPortrait = height > width
                }
            } catch (e: Exception) {
                e.printStackTrace()
                isPortrait = null
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        when (isPortrait) {
            true -> {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .aspectRatio(2f / 3f),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .background(color = Color.White)
                            .clip(RoundedCornerShape(0.dp))
                            .border(BorderStroke(4.dp, MaterialTheme.colorScheme.onBackground))
                            .padding(10.dp),
                    )
                }
            }

            false -> {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .background(color = Color.White)
                            .clip(RoundedCornerShape(0.dp))
                            .border(BorderStroke(4.dp, MaterialTheme.colorScheme.onBackground))
                            .padding(10.dp),
                    )
                }
            }

            null -> {
                CircularProgressIndicator()
            }
        }
    }
}