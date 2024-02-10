package com.example.shutaffim.Model.Screen


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.example.shutaffim.Intersted
import com.example.shutaffim.Model.Post
import com.example.shutaffim.Model.User
import com.example.shutaffim.Model.UserType
import com.example.shutaffim.R
import com.example.shutaffim.RequestView
import com.example.shutaffim.Screen
import com.example.shutaffim.ViewModel.AuthViewModel
import com.example.shutaffim.ViewModel.PostsVM
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CustomSlider(
    modifier: Modifier = Modifier,
    sliderList: MutableList<String>,
    dotsActiveColor: Color = Color.DarkGray,
    dotsInActiveColor: Color = Color.LightGray,
    dotsSize: Dp = 10.dp,
    pagerPaddingValues: PaddingValues = PaddingValues(horizontal = 0.dp),
    imageCornerRadius: Dp = 4.dp,
    imageHeight: Dp = 200.dp,
) {
    val pagerState = rememberPagerState(pageCount = { sliderList.size })
    val scope = rememberCoroutineScope()

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = pagerPaddingValues,
            modifier = modifier.weight(1f)
        ) { page ->
            val pageOffset =
                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

            val scaleFactor =
                0.75f + (1f - 0.75f) * (1f - pageOffset.absoluteValue)

            Box(modifier = modifier
                .graphicsLayer {
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                }
                .alpha(
                    scaleFactor.coerceIn(0f, 1f)
                )
                .padding(0.dp)
                .clip(RoundedCornerShape(imageCornerRadius))) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .scale(
                            Scale.FILL
                        )
                        .crossfade(true)
                        .data(sliderList[page])
                        .build(),
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.pngwing_com),
                    error = painterResource(id = R.drawable.pngwing_com),
                    modifier = modifier
                        .height(imageHeight)
                        .alpha(if (pagerState.currentPage == page) 1f else 0.5f)
                )
            }
        }
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        sliderList.forEachIndexed { index, _ ->
        Box(
            modifier = modifier
                .padding(4.dp)
                .size(dotsSize)
                .clip(CircleShape)
                .background(
                    color = if (index == pagerState.currentPage) dotsActiveColor else dotsInActiveColor
                )
                .clickable {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayPost(navController: NavController,
                postsVM: PostsVM,
                authViewModel: AuthViewModel,
                ) {


    val default = Post(
        "0",
        "0",
        "0",
        "0",
        0,
        0,
        0,
        0,
        listOf(""),
        "",
        "",
        "",
        "",
        "",
        ""
    )
    val post by postsVM.currPost.observeAsState(default)
    val currUser by authViewModel.currentUser.observeAsState(default)



    when {
        post == default -> Text("Loading")
        currUser == default -> Text("Loading")

        else -> PostScreen(navController = navController,
            postsVM = postsVM,
            post,
            currUser as User,
            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(navController: NavController,
               postsVM: PostsVM,
               post: Post,
               currUser: User,

) {

    postsVM.getInterestedInPost(post.id)
    val interestedIdList = postsVM. interestedInPostId
    val listOfRequest by postsVM.interestedInPost.observeAsState(emptyList())

    val sliderList = remember {
        mutableListOf(
            "https://www.gstatic.com/webp/gallery/1.webp",
            "https://www.gstatic.com/webp/gallery/2.webp",
            "https://www.gstatic.com/webp/gallery/3.webp",
            "https://www.gstatic.com/webp/gallery/4.webp",
            "https://www.gstatic.com/webp/gallery/5.webp",
        )
    }

    var interestedClick by remember {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),

        topBar = {
            TopAppBar(
                title = { Text("Post") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currUser.type == UserType.Publisher.type)
                            navController.navigate(Screen.MyPostsScreen.route)
                        else
                            navController.navigate(Screen.PostsSearchScreen.route)
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },

                actions = {
                    if (currUser.type == UserType.Publisher.type) {
                        IconButton(onClick = {
                            postsVM.loadPost(post.id)
                            navController.navigate(Screen.EditPostScreen.route)
                        }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit")
                        }
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                ),
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(8.dp)
                .padding(innerPadding)


        ) {

            CustomSlider(sliderList = sliderList)//in rows
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 0.dp, top = 0.dp, end = 8.dp, bottom = 0.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${post.price}₪",
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 0.dp, top = 0.dp, end = 8.dp, bottom = 0.dp)
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${post.city}, ${post.street}, ${post.house_num}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 0.dp, top = 8.dp, end = 8.dp, bottom = 0.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tags",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 0.dp, top = 0.dp, end = 8.dp, bottom = 4.dp)
            ) {
                items(post.tags) { tag ->
                    Card(
                        modifier = Modifier
                            .padding(4.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            text = tag,
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                            ),
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 0.dp, top = 0.dp, end = 8.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "About the Apartment",
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Card(
                modifier = Modifier
                    .size(height = 150.dp, width = 400.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                BasicTextField(
                    value = post.about,
                    onValueChange = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }
            Spacer(modifier = Modifier.height(54.dp))
            if (currUser.type == UserType.Publisher.type) {
                Button(
                    onClick = { interestedClick = !interestedClick },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Text(
                        text = "List of interested",
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                        )
                    )

                    if (interestedClick) {
                        ModalBottomSheet(onDismissRequest = { interestedClick = false },
                            content = {
                                Intersted(navController = navController,listOfRequest)
                            }
                        )
                    }
                }
            } else {//if consumer
                if (interestedIdList.contains(currUser.email)){
                    Button(
                        onClick = { interestedClick = !interestedClick
                                    },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(

                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = "Already interested",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    }
                } else {
                    Button(
                        onClick = { interestedClick = !interestedClick

                                    },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        Text(
                            text = "i'm interested",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                            )
                        )

                        if (interestedClick) {
                            ModalBottomSheet(onDismissRequest = { interestedClick = false },
                                content = {
                                    RequestView(currUser, post)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PostScreenPreview() {
   // DisplayPost(navController = NavController(LocalContext.current), postsVM = PostsVM(), authViewModel = AuthViewModel())

}

