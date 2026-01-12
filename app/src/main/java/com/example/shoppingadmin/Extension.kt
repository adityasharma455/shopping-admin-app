package com.example.shoppingadmin

import android.content.Context
import android.net.Uri
import okio.IOException

@Throws(IOException::class)
fun Uri.uriToByteArray(context: Context): ByteArray{
   return context.contentResolver.openInputStream(this)?.use {stream ->
        stream.buffered().readBytes()
    } ?: throw IOException("Could not open input stream for URI")
}