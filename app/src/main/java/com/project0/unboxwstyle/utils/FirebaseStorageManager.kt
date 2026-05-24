package com.project0.unboxwstyle.utils

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage

object FirebaseStorageManager {

    private val storage =
        FirebaseStorage.getInstance()

    fun uploadImage(
        uri: Uri,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        val ref = storage.reference.child(
            "wardrobe/${System.currentTimeMillis()}"
        )

        ref.putFile(uri)
            .continueWithTask {
                ref.downloadUrl
            }
            .addOnSuccessListener {
                onSuccess(it.toString())
            }
            .addOnFailureListener {
                onFailure(it)
            }
    }
}