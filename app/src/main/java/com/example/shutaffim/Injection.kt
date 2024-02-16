package com.example.shutaffim

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

object Injection {
    private val instanceFirestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val storageInstance: FirebaseStorage by lazy {
       FirebaseStorage.getInstance()
    }

    fun firestoreInstance(): FirebaseFirestore {
        return instanceFirestore
    }

    fun storageInstance():FirebaseStorage {
        return storageInstance
    }


}