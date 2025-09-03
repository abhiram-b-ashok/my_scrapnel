package com.example.myscrapnel.views.scrapnel_page

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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
    val date = formatTimestampToString(timestamp)

    val pagerImageList = remember { mutableStateListOf<Uri>() }

    var showViewPager by remember { mutableStateOf(false) }
Box() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        val imagesList = mutableListOf<Uri>()
        LaunchedEffect(scrapnel) {
            pagerImageList.clear()
            content?.lines()?.forEach { line ->
                if (line.contains("🖼️")) {
                    pagerImageList.add(line.drop(4).toUri())
                }
            }
        }
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
            if (line.isBlank()) return@forEach

            if (line.contains("🖼️")) {
                val uri = line.drop(4).toUri()
                imagesList.add(uri)
//                pagerImageList.add(uri)

                if (imagesList.size == 3) {
                    ImageStack(
                        images = imagesList.toList(),
                        onClick = {
                            showViewPager = true
                        }
                    )
                    imagesList.clear()
                }
            } else {
                if (imagesList.isNotEmpty()) {
                    ImageStack(
                        images = imagesList.toList(),
                        onClick = {
                            showViewPager = true
                        }
                    )
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
            ImageStack(
                images = imagesList.toList(),
                onClick = {
                    showViewPager = true
                }
            )
        }


    }
    if (showViewPager && pagerImageList.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize().
                    align (Alignment.Center)
                .clickable { showViewPager = false },
            contentAlignment = Alignment.Center
        ) {

            Log.d("shankoliyt", "Main: ${pagerImageList.size}")
            when (pagerImageList.size) {
                1 -> ViewPageForSingleImage(pagerImageList)
                2 -> ViewPagerForTwoImage(pagerImageList)
                3 -> ViewPagerForThreeImage(pagerImageList)
                4 -> ViewPagerForFourImage(pagerImageList)
                else -> ViewPagerScreen(pagerImageList)
            }

        }

    }
}
}


@Composable
private fun ImageStack(images: List<Uri>, onClick: () -> Unit) {
    Box(modifier = Modifier.clickable { onClick() }) {
        if (images.size == 1) {
            SingleImageView(images.first())
        } else {
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
                    val size = rearrangedImages.size
                    val middleIndex = size / 2

                    val (xOffset, zRotation) = when {
                        size == 2 -> {
                            // For 2 images, slide both (one right, one left)
                            if (index == 0) {
                                30.dp to 20f
                            } else {
                                (-30).dp to -20f
                            }
                        }
                        size % 2 == 1 -> {
                            // Odd number of images - middle image straight, others slide
                            val isLeft = index > middleIndex
                            val isRight = index < middleIndex

                            val offsetAmount = (kotlin.math.abs(index - middleIndex)) * 30.dp
                            val rotationAngle = (kotlin.math.abs(index - middleIndex)) * 20f

                            when {
                                isLeft -> -offsetAmount to -rotationAngle
                                isRight -> offsetAmount to rotationAngle
                                else -> 0.dp to 0f
                            }
                        }
                        else -> {
                            // Even number > 2 fallback, slide images with offset and rotation
                            val offsetAmount = (kotlin.math.abs(index - middleIndex)) * 30.dp
                            val rotationAngle = (kotlin.math.abs(index - middleIndex)) * 20f
                            val isLeft = index > middleIndex
                            val isRight = index < middleIndex

                            when {
                                isLeft -> -offsetAmount to -rotationAngle
                                isRight -> offsetAmount to rotationAngle
                                else -> 0.dp to 0f
                            }
                        }
                    }

                    ImageCard(
                        uri = uri,
                        contentDescription = "$uri",
                        modifier = Modifier
                            .offset(x = xOffset)
                            .graphicsLayer { rotationZ = zRotation }
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



@SuppressLint("SuspiciousIndentation")
@Composable
fun ViewPagerScreen(imageList: List<Uri>) {
    var selectedImage by remember { mutableStateOf<Uri?>(null) }

    val initialPage = Int.MAX_VALUE / 2
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { Int.MAX_VALUE }
    )

    val itemWidthDp = 131.dp + 15.dp
    val itemWidthPx = with(LocalDensity.current) { itemWidthDp.toPx() }

    val pageOffset = pagerState.currentPageOffsetFraction
    val currentPage = pagerState.currentPage
    val relativeScroll = (currentPage - initialPage) + pageOffset

    val listSize = imageList.size


    Box(modifier = Modifier.fillMaxSize()) {

        val backgroundItemsCount = 50

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            for (i in 0 until backgroundItemsCount) {

                val centerIndex = backgroundItemsCount / 2
                val offsetFromCenter = (i - centerIndex)

                val baseX = offsetFromCenter * itemWidthPx
                val scrollShift = relativeScroll * itemWidthPx * 0.8f
                val totalX = baseX + scrollShift

                    Box(
                    modifier = Modifier
                        .size(width = 131.dp, height = 150.dp)
                        .graphicsLayer {
                            translationX = totalX
                        }
                        .align(Alignment.Center)
                ) {
                    ImageBack(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 80.dp),
            pageSpacing = 16.dp
        )
        { page ->

            val realPage = page % listSize
            val density = LocalDensity.current.density

            val rotationYi = when {
                page < pagerState.currentPage -> -30f
                page > pagerState.currentPage -> 30f
                else -> 0f
            }

            val scale = if (page == pagerState.currentPage) 1f else 0.85f

            val animatedRotation by animateFloatAsState(
                targetValue = rotationYi,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessLow
                ),                label = "rotation"
            )

            val animatedScale by animateFloatAsState(
                targetValue = scale,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                ),                label = "scale"
            )

            ImagePagers(
                image = imageList[realPage],
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = animatedScale
                        scaleY = animatedScale
                        rotationY = animatedRotation
                        cameraDistance = 12 * density
                    }
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray)
                    .clickable {
                        selectedImage = imageList[realPage]
                    }
            )
        }

        if (selectedImage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.9f))
                    .clickable { selectedImage = null },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = selectedImage,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight(0.8f)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}


@Composable
fun ImagePagers(image: Uri, modifier: Modifier) {
    AsyncImage(
        model = image,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}

@Composable
fun ImageBack(modifier: Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color = MaterialTheme.colorScheme.onBackground)
    )
}




@Composable
fun ViewPagerForFourImage(list: List<Uri>) {
    var selectedImage by remember { mutableStateOf<Uri?>(null) }

    val initialPage = Int.MAX_VALUE / 2
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { Int.MAX_VALUE }
    )

    val itemWidthDp = 200.dp + 220.dp
    val itemWidthPx = with(LocalDensity.current) { itemWidthDp.toPx() }

    val pageOffset = pagerState.currentPageOffsetFraction
    val currentPage = pagerState.currentPage
    val relativeScroll = (currentPage - initialPage) + pageOffset

    val listSize = list.size
    Box(modifier = Modifier.fillMaxSize()) {
        val backgroundItemsCount = 20

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            for (i in 0 until backgroundItemsCount) {

                val centerIndex = backgroundItemsCount / 2
                val offsetFromCenter = (i - centerIndex)

                val baseX = offsetFromCenter * itemWidthPx
                val scrollShift = relativeScroll * itemWidthPx * 1f
                val totalX = baseX + scrollShift

                Box(
                    modifier = Modifier
                        .size(width = 200.dp, height = 200.dp)
                        .graphicsLayer {
                            translationX = totalX
                        }
                        .align(Alignment.Center)
                ) {
                    ImageBack(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }


        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal =90.dp),
            pageSpacing = 36.dp
        )
        { page ->

            val realPage = page % listSize

            val rotationYi = when {
                page < pagerState.currentPage -> -45f
                page > pagerState.currentPage -> 45f
                else -> 0f
            }

            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

            val scale = 1f - 0.15f * kotlin.math.min(1f, kotlin.math.abs(pageOffset))

            val animatedRotation by animateFloatAsState(
                targetValue = rotationYi,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessLow
                ),                label = "rotation"
            )

            val animatedScale by animateFloatAsState(
                targetValue = scale,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "scale"
            )


            ImagePagers(
                image = list[realPage],
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = animatedScale
                        scaleY = animatedScale
                        rotationY = animatedRotation
                    }
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray)
                    .clickable {
                        selectedImage = list[realPage]
                    }
            )
        }
        if (selectedImage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.9f))
                    .clickable { selectedImage = null },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = selectedImage,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight(0.8f)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}



@Composable
fun ViewPagerForTwoImage(list: List<Uri>) {
    var selectedImage by remember { mutableStateOf<Uri?>(null) }

    val initialPage = Int.MAX_VALUE / 2
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { Int.MAX_VALUE }
    )

    val itemWidthDp = 200.dp + 220.dp
    val itemWidthPx = with(LocalDensity.current) { itemWidthDp.toPx() }

    val pageOffset = pagerState.currentPageOffsetFraction
    val currentPage = pagerState.currentPage
    val relativeScroll = (currentPage - initialPage) + pageOffset

    val listSize = list.size

    Box(modifier = Modifier.fillMaxSize()) {

        val backgroundItemsCount = 50
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            for (i in 0 until backgroundItemsCount) {

                val centerIndex = backgroundItemsCount / 2
                val offsetFromCenter = (i - centerIndex)

                val baseX = offsetFromCenter * itemWidthPx
                val scrollShift = relativeScroll * itemWidthPx * 1f
                val totalX = baseX + scrollShift

                Box(
                    modifier = Modifier
                        .size(width = 200.dp, height = 200.dp)
                        .graphicsLayer {
                            translationX = totalX
                        }
                        .align(Alignment.Center)
                ) {
                    ImageBack(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 100.dp),
            pageSpacing = 186.dp
        ){page ->

            val realPage = page % listSize
            val density = LocalDensity.current.density

            val rotationYi = when {
                page < pagerState.currentPage -> -40f
                page > pagerState.currentPage -> 40f
                else -> 0f
            }
            val rotationZi = when{
                page == pagerState.currentPage -> 360f
                else -> 0f
            }

            val scale = if (page == pagerState.currentPage) 1f else 0.85f

            val animatedRotation by animateFloatAsState(
                targetValue = rotationYi,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessLow
                ),                label = "rotation"
            )

            val animatedScale by animateFloatAsState(
                targetValue = scale,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                ),                label = "scale"
            )

            ImagePagers(
                image = list[realPage],
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = animatedScale
                        scaleY = animatedScale
                        rotationY = animatedRotation
                        rotationZ = rotationZi
                    }
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray)
                    .clickable {
                        selectedImage = list[realPage]
                    }
            )
        }
        if (selectedImage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.9f))
                    .clickable { selectedImage = null },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = selectedImage,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight(0.8f)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Composable
fun ViewPageForSingleImage(list: List<Uri>)
{
    var selectedImage by remember { mutableStateOf<Uri?>(list[0]) }
    if (selectedImage != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.9f))
                .clickable { selectedImage = null },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = selectedImage,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(0.8f)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Fit
            )
        }
    }

}

@Composable
fun ViewPagerForThreeImage(list: List<Uri>) {
    var selectedImage by remember { mutableStateOf<Uri?>(null) }

    val initialPage = Int.MAX_VALUE / 2
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { Int.MAX_VALUE }
    )

    val itemWidthDp = 300.dp
    val itemWidthPx = with(LocalDensity.current) { itemWidthDp.toPx() }

    val pageOffset = pagerState.currentPageOffsetFraction
    val currentPage = pagerState.currentPage
    val relativeScroll = (currentPage - initialPage) + pageOffset * 1.5f

    val listSize = list.size

    Box(modifier = Modifier.fillMaxSize()) {
        val backgroundItemsCount = 50
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            for (i in 0 until backgroundItemsCount) {
                val centerIndex = (backgroundItemsCount / 2)
                val offsetFromCenter = (i - centerIndex)

                val baseX = offsetFromCenter * itemWidthPx
                val scrollShift = relativeScroll * itemWidthPx
                val totalX = baseX + scrollShift

                Box(
                    modifier = Modifier
                        .size(width = 200.dp, height = 200.dp)
                        .graphicsLayer {
                            translationX = totalX - 100.dp.toPx()
                        }
                        .align(Alignment.Center)
                ) {
                    ImageBack(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 100.dp),
            pageSpacing = 186.dp
        ) { page ->
            val realPage = page % listSize
            val density = LocalDensity.current.density

            val rotationYi = when {
                page < pagerState.currentPage -> -40f
                page > pagerState.currentPage -> 40f
                else -> 0f
            }
            val rotationZi = when {
                page == pagerState.currentPage -> 360f
                else -> 0f
            }

            val scale = if (page == pagerState.currentPage) 1f else 0.85f

            val animatedRotation by animateFloatAsState(
                targetValue = rotationYi,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "rotation"
            )

            val animatedScale by animateFloatAsState(
                targetValue = scale,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "scale"
            )

            ImagePagers(
                image = list[realPage],
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = animatedScale
                        scaleY = animatedScale
                        rotationY = animatedRotation
                        rotationZ = rotationZi
                    }
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray)
                    .clickable {
                        selectedImage = list[realPage]
                    }
            )
        }

        if (selectedImage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.9f))
                    .clickable { selectedImage = null },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = selectedImage,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight(0.8f)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}





