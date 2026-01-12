  package com.example.shoppingadmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.shoppingadmin.Presentation.Navigation.AppNavigation
import com.example.shoppingadmin.ui.theme.ShoppingAdminTheme
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject

  class MainActivity : ComponentActivity() {
      private val firebaseAuth: FirebaseAuth by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingAdminTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    //AddCategoryScreen()
                //AddProductScreen()
                    //AddBannerModel()

                    AppNavigation(firebaseAuth)
                }
            }
        }
    }
}

