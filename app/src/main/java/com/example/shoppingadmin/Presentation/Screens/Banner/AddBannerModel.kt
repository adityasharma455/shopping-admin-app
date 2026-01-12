package com.example.shoppingadmin.Presentation.Screens.Banner

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.shoppingadmin.Domain.Models.BannerDataModel
import com.example.shoppingadmin.Presentation.ViewModel.MyViewModel
import com.example.shoppingadmin.uriToByteArray
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun AddBannerModel(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = koinViewModel<MyViewModel>(),
    navController: NavController,
    onBackClick : () -> Unit = {navController.popBackStack()}
) {
    val AddBannerModelPhotoState by viewModel.addBannerModelPhotoState.collectAsStateWithLifecycle()
    val AddBannerModelState by viewModel.addBannerModelState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var photoByteArray by remember { mutableStateOf<ByteArray?>(null) }

    val BannerImagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {uri ->
        photoUri = uri
        uri?.let {
            try {
                photoByteArray = it.uriToByteArray(context)
            }catch (e: Exception){
                Toast.makeText(context, "Error to Load Image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(AddBannerModelState) {
        when{
            AddBannerModelState.isSuccess != null ->{
                Toast.makeText(context, AddBannerModelState.isSuccess, Toast.LENGTH_SHORT).show()
                //Reset the BannerState
                viewModel.resetAddBannerModelState()
                //reset the ui state
                name = ""
                photoUri = null
                photoByteArray = null
                onBackClick()
            }
            AddBannerModelState.Error != null -> {
                Toast.makeText(context, AddBannerModelState.Error.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Banner Details")

        // Photo selection button
        if(photoUri == null) {
            Button(
                onClick = {
                    BannerImagePicker.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
            ) {
                Text("Select Image")
            }
        }

            //Image Preview
            photoUri?.let {uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = null
                )
            }
            when{
                AddBannerModelPhotoState.isLoading ->{
                    CircularProgressIndicator()
                }
                AddBannerModelPhotoState.isSuccess != null ->{
                    //Toast.makeText(context, "Photo Upload Successfully", Toast.LENGTH_SHORT).show()
                }
                AddBannerModelPhotoState.Error != null ->{
                    Toast.makeText(context, AddBannerModelPhotoState.Error.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            Spacer(modifier = Modifier.height(1.dp))

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                },
                label = {
                    Text("Name of Banner Organization")
                }
            )

            Spacer(modifier = Modifier.height(1.dp))
            Button(
                onClick = {
                    if(photoByteArray == null){
                        Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    viewModel.addBannerModelPhoto(
                        byteArray = photoByteArray!!,
                        onSuccess = {downloadUrl ->
                            Log.d("AddBannerScreen", "url ${downloadUrl}")
                            val data  = BannerDataModel(
                                name = name,
                                imageUrl = downloadUrl
                            )
                            viewModel.addBannerModel(data)
                        },
                        onError = {error ->{
                            Toast.makeText(context, "Image upload failed: $error", Toast.LENGTH_SHORT).show()
                        }
                        }
                    )

                }, enabled = name.isNotBlank()
            ) {
                if(AddBannerModelState.isLoading || AddBannerModelPhotoState.isLoading){
                    CircularProgressIndicator(color = Color.White)
                }else{
                    Text("Add Product")
                }
            }
    }
}