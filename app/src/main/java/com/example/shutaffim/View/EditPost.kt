import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.sharp.Face
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.shutaffim.Model.Post
import com.example.shutaffim.R
import com.example.shutaffim.Screen
import com.example.shutaffim.ViewModel.AuthViewModel
import com.example.shutaffim.ViewModel.PostsVM


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPost(
    navController: NavController,
    postsVM: PostsVM,
    authVM: AuthViewModel
) {
    var user = authVM.currentUser.observeAsState()


    var userId by remember {
        mutableStateOf( "")
    }
    userId = user.value?.email ?: ""

    var city by remember {
        mutableStateOf("")
    }
    var street by remember {
        mutableStateOf("")
    }
    var house_num by remember {
        mutableStateOf("")
    }
    var current_partner by remember {
        mutableStateOf("")
    }
    var max_partner by remember {
        mutableStateOf("")
    }
    var tags by remember {
        mutableStateOf("")
    }
    var price by remember {
        mutableStateOf("")
    }
    var about_apartment by remember {
        mutableStateOf("")
    }

    val post = postsVM.currPost.observeAsState()
    city = post.value?.city ?: ""
    street = post.value?.street ?: ""
    house_num = post.value?.house_num.toString()
    current_partner = post.value?.curr_roommates.toString()
    max_partner = post.value?.max_roommates.toString()
    price = post.value?.price.toString()
    about_apartment = post.value?.about ?: ""
    tags = postsVM.tagsToString(post.value?.tags ?: emptyList())

    val imagesToDelete = remember {
        mutableStateOf(mutableListOf<String>())
    }
    var picHasChanged by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val img: Bitmap =
        BitmapFactory.decodeResource(
            context.resources,
            R.drawable.logo_background
        )//default image
    val bitmap = remember { mutableStateOf(img) }
    val newImages = remember { mutableStateOf(listOf<Bitmap>()) }

    val launchImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, uri)
            } else {
                val source =
                    uri?.let { it1 -> ImageDecoder.createSource(context.contentResolver, it1) }
                bitmap.value = source?.let { it1 -> ImageDecoder.decodeBitmap(it1) }!!
            }
            picHasChanged = true
            newImages.value = newImages.value + bitmap.value
        }
    }





    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Edit Post",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screen.PostScreen.route)
                    }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                actions = {

                    IconButton(onClick = {
                        postsVM.deletePost(post.value?.id ?: "", navController)

                    }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "delete post"
                        )
                    }


                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = MaterialTheme.colorScheme.surface,
                    actionIconContentColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(start = 4.dp, end = 4.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,


                ) {


                ElevatedCard {

                    //city
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                        value = city,
                        onValueChange = { city = it },
                        label = { Text(text = "city") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        leadingIcon = {
                            Image(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                            )
                        },
                        singleLine = true
                    )



                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ){
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            /* street*/
                            OutlinedTextField(
                                value = street,
                                onValueChange = { street = it },
                                label = {
                                    Text(
                                        text = "street",
                                        style = TextStyle(fontSize = 15.sp) // Set the desired font size
                                    )
                                        },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                                    cursorColor = MaterialTheme.colorScheme.primary
                                ),
                                leadingIcon = {
                                    Image(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = "",
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                                    )
                                },
                                singleLine = true
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            /* nuber_house*/
                            OutlinedTextField(
                                value = house_num,
                                onValueChange = { house_num = it.filter { it.isDigit() } },
                                label = {
                                    Text(
                                        text = "house number",
                                        style = TextStyle(fontSize = 15.sp) // Set the desired font size
                                    )
                                },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                                    cursorColor = MaterialTheme.colorScheme.primary
                                ),
                                leadingIcon = {
                                    Image(
                                        imageVector = Icons.Filled.LocationOn,
                                        contentDescription = "",
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                                    )
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number
                                )
                            )
                        }
                    }//row Adrees && number

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ){
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            //num_partner
                            OutlinedTextField(
                                value = current_partner,
                                onValueChange = { current_partner = it.filter { it.isDigit() } },
                                label = {
                                    Text(
                                        text = "current roomates",
                                        style = TextStyle(fontSize = 14.sp) // Set the desired font size
                                    )
                                        },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                                    cursorColor = MaterialTheme.colorScheme.primary
                                ),
                                leadingIcon = {
                                    Image(
                                        imageVector = Icons.Sharp.Face,
                                        contentDescription = "",
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                                    )
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number
                                )
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            OutlinedTextField(
                                value = max_partner,
                                onValueChange = { max_partner = it.filter { it.isDigit() } },
                                label = {
                                    Text(
                                        text = "max roommates",
                                        style = TextStyle(fontSize = 14.sp) // Set the desired font size
                                    )
                                },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                                    cursorColor = MaterialTheme.colorScheme.primary
                                ),
                                leadingIcon = {
                                    Image(
                                        imageVector = Icons.Sharp.Face,
                                        contentDescription = "",
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                                    )
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number
                                )

                            )
                        }
                    }//row partner

                    Spacer(modifier = Modifier.height(8.dp))

                    //price
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                        value = price,
                        onValueChange = { price = it.filter { it.isDigit() } }, // Only allow numeric input
                        label = { Text(text = "price") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        leadingIcon = {
                            Image(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                            )

                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    //hashtag
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                        value = tags,
                        onValueChange = { tags = it },
                        label = { Text(text = "tag1,tag2...") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        leadingIcon = {
                            Image(
                                imageVector = Icons.Default.Search,
                                contentDescription = "",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                            )
                        },
                        singleLine = false
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    //Information about the apartment
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp)
                            .height(128.dp),
                        value = about_apartment,
                        onValueChange = { about_apartment = it },
                        label = { Text(text = "about") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        leadingIcon = {
                            Image(
                                imageVector = Icons.Default.Create,
                                contentDescription = "",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                            )
                        },

                        singleLine = false,  // Allow multiple lines
                        keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,  // Allow any text input
                        imeAction = ImeAction.Done,  // Change to another action if needed

                        ),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.height(8.dp))


                }//ElevatedCard filed
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Current Pictures",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                ElevatedCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        LazyRow(
                            modifier = Modifier.weight(1f)
                        ) {
                            items(post.value?.pictures ?: emptyList()) { picture ->
                                ExistingImageItem(pictureURL = picture.pictureUrl) {
                                    if (imagesToDelete.value.contains(picture.pictureName)) {
                                        imagesToDelete.value.remove(picture.pictureName)
                                    } else {
                                        imagesToDelete.value.add(picture.pictureName)
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Add pictures",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                ElevatedCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        LazyRow(
                            modifier = Modifier.weight(1f)
                        ) {
                            items(newImages.value) { bitmap ->
                                ImageItem(bitmap = bitmap, onDelete = {
                                    newImages.value = newImages.value - bitmap
                                })
                            }
                        }
                        IconButton(
                            onClick = {
                                launchImage.launch("image/*")
                            }
                        )
                        {
                            Icon(
                                Icons.Default.AddPhotoAlternate,
                                contentDescription = "Add new picture",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }



                Spacer(modifier = Modifier.height(64.dp))
                Button(
                    onClick = {
                        val updatedPictures =
                            post.value?.pictures?.filterNot { it.pictureName in imagesToDelete.value }
                                ?: emptyList()
                        val newPost = Post(
                            id = post.value?.id ?: "",
                            date = post.value?.date ?: System.currentTimeMillis(),
                            city = city,
                            street = street,
                            house_num = house_num.toInt(),
                            curr_roommates = current_partner.toInt(),
                            max_roommates = max_partner.toInt(),
                            price = price.toInt(),
                            tags = postsVM.tagsToList(tags),
                            about = about_apartment,
                            userId = userId,
                            pictures = updatedPictures
                        )
                        postsVM.updatePost(
                            newPost,
                            newImages.value,
                            imagesToDelete.value,
                            navController
                        )
                        postsVM.resetPost()

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 64.dp, end = 64.dp, bottom = 8.dp),
                    enabled = city.isNotBlank() &&
                            street.isNotBlank() &&
                            current_partner.isNotBlank() &&
                            max_partner.isNotBlank() &&
                            price.isNotBlank() &&
                            about_apartment.isNotBlank() &&
                            price.toInt() >= 0 &&
                            price.toInt() <= 100000
                ) {
                    Text(text = "Save")
                }


            }//Column
            Column(
                modifier = Modifier
                    .height(800.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                if(postsVM.isLoading.value){
                    CircularProgressIndicator()
                }

            }


        }//content
    )//Scaffold
}

@Composable
fun ExistingImageItem(pictureURL: String, onDelete: () -> Unit) {
    val isDelete = remember { mutableStateOf(false) }
    ElevatedCard(
        modifier = Modifier
            .wrapContentSize()
            .padding(4.dp)
            .clickable {
                onDelete()
                isDelete.value = !isDelete.value
            },
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(pictureURL),
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )
            if (isDelete.value) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "delete",
                    tint = Color.Red,
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }

}