package com.jacknguyen.readerapp.screen.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.jacknguyen.readerapp.model.UserReaderApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginScreenViewModel : ViewModel() {
    //val loadingState  = MutableStateFlow(LoadingState.IDLE)
    private val auth: FirebaseAuth = Firebase.auth
    private val _loadingState = MutableLiveData(false)
    val loadingState: LiveData<Boolean> = _loadingState
    fun signInWithEmailAndPassword(email: String, password: String,home: ()-> Unit) =viewModelScope.launch{
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Đăng nhập thành công
                        Log.d("FB", "signInWithEmail:hahahahaha ${task.result}")
                        home()
                    } else {
                        Log.d("FB", "signInWithEmail:failure ${task.result}")
                    }
                }
        } catch (e: Exception) {
            Log.d("FB", "signInWithEmail:failure ${e.message}")
        }
    }

    fun createUserWithEmailAndPassword(email: String, password: String, home: () -> Unit) = viewModelScope.launch {
        if (_loadingState.value== false){
            _loadingState.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //Tách huy @hamulet.com
                        val displayName = task.result?.user?.email?.split("@")?.get(0)
                        createUserForService(displayName)
                        // Tạo tài khoản thành công
                        Log.d("FB", "createUserWithEmail:success ${task.result}")
                        home()
                    } else {
                        Log.d("FB", "createUserWithEmail:failure ${task.exception?.message}")
                    }
                    _loadingState.value = false
                }
                .addOnFailureListener { e ->
                    Log.d("FB", "createUserWithEmail:failure ${e.message}")
                    _loadingState.value = false
                }
        }
    }

    private fun createUserForService(displayName: String?) {
        val userId = auth.currentUser?.uid
        val userData = UserReaderApp(
            userId = userId.toString(),
            displayName = displayName.toString(),
            avatarUrl =  "",
            quote = "ahhahahahahah",
            profession = "Developer",
            id = null
        ).toMap()

        FirebaseFirestore.getInstance().collection("users").add(userData)
    }
}
