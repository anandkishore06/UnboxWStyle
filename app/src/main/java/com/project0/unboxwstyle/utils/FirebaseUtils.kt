package com.project0.unboxwstyle.utils

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage

object FirebaseUtils {

    private val storage =
        FirebaseStorage.getInstance()

    fun uploadImage(
        uri: Uri,
        onSuccess: (String) -> Unit
    ) {

        val ref = storage.reference
            .child("wardrobe/${System.currentTimeMillis()}")

        ref.putFile(uri)
            .continueWithTask {
                ref.downloadUrl
            }
            .addOnSuccessListener {
                onSuccess(it.toString())
            }
    }
}