package com.example.shoppingadmin.Data.PushNotification

import android.content.Context
import android.util.Log
import com.example.shoppingadmin.Common.USER_FCM_TOKEN
import com.example.shoppingadmin.R
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

class PushNotification @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val context : Context
) {
    private var accessToken: String = ""
    private val client = OkHttpClient()
    private val coroutine = CoroutineScope(Dispatchers.IO)

    fun sendNotificationToAllUser(
        productName: String,
        imageUrl: String
    ) {
        coroutine.launch {
            try {
                val token = getTokkens()
                if (token.isNotEmpty()){
                    sendNotifications(token, productName, imageUrl)
                }
            }catch (e: Exception){
                Log.d("TAG", "sendNotificationToAllUser: ${e.message.toString()}")
            }
        }
    }

    //User FCM Token
    private suspend fun getTokkens() : List<String> = withContext(Dispatchers.IO){

        try {
            Log.d("FCM_DEBUG", "Fetching tokens from collection: $USER_FCM_TOKEN")
            val snapshot = firestore.collection(USER_FCM_TOKEN).get().await()
            val tokens = snapshot.mapNotNull {
                Log.d("FCM_DEBUG", "Found token document: ${it.id}")
                it.getString("token")
            }
            Log.d("FCM_DEBUG", "Retrieved ${tokens.size} tokens")
            tokens.forEach { Log.d("FCM_DEBUG", "Token: ${it.take(5)}...") }
            tokens
        } catch (e: Exception) {
            Log.e("FCM_DEBUG", "Error getting tokens", e)
            emptyList()
        }
    }

    init {
        coroutine.launch {
            accessToken()
        }
    }
    private suspend fun accessToken(){
        withContext(Dispatchers.IO) {
            try {
                val stream = context.resources.openRawResource(R.raw.apikey)
                var credentails = GoogleCredentials.fromStream(stream)
                    .createScoped("https://www.googleapis.com/auth/firebase.messaging")
                credentails.refresh()
                accessToken = credentails.accessToken.tokenValue
            } catch (e: Exception){
                Log.d("TAG", "accessToke: ${e.message.toString()}")
            }
        }
    }

    private suspend fun sendNotifications(
        tokens: List<String>,
        productName: String,
        imageUrl: String
    ){
        Log.d("FCM_DEBUG", "Starting to send ${tokens.size} notifications")
try {
    tokens.forEach { token->
        Log.d("FCM_DEBUG", "Sending to token: ${token.take(5)}...")

        val json = JSONObject().apply {
            put("message", JSONObject().apply {
                put("token", token)
                put("notification", JSONObject().apply {
                    put("title", "New Product Added")
                    put("body", "$productName is now available")
                    put("image", imageUrl)
                })
                put("data", JSONObject().apply {
                    put("title", "New Product Added")
                    put("body", "$productName is now available")
                    put("image", imageUrl)
                })
            })
        }
        val body = json.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .header("Authorization", "Bearer $accessToken")
            .url("https://fcm.googleapis.com/v1/projects/shoppingapp-e449c/messages:send")
            .post(body)
            .build()
        Log.d("FCM_DEBUG", "Request prepared, sending...")


        try {
            val response = withContext(Dispatchers.IO) {
                client.newCall(request).execute()
            }
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                Log.d("FCM_DEBUG", "Success! Response: $responseBody")
            } else {
                val errorBody = response.body?.string()
                Log.e("FCM_DEBUG", "Error response: ${response.code} - $errorBody")
            }
        }catch (e: Exception){
            Log.d("TAG", "sendNotifications: ${e.message.toString()}")
        }
    }
} catch (e: Exception){
    Log.e("FCM_DEBUG", "Error sending notification", e)

}

            }


}
