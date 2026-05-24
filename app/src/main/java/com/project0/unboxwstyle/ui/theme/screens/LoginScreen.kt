package com.project0.unboxwstyle.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.project0.unboxwstyle.R
import com.project0.unboxwstyle.ui.theme.Accent
import java.util.concurrent.TimeUnit

@Composable
fun LoginScreen(
    navController: NavController
) {

    var phoneNumber by remember {
        mutableStateOf("")
    }

    var otp by remember {
        mutableStateOf("")
    }

    var verificationId by remember {
        mutableStateOf("")
    }

    var otpSent by remember {
        mutableStateOf(false)
    }

    var loading by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    val auth = remember { FirebaseAuth.getInstance() }

    val webClientId = remember {
        context.resources.getString(
            R.string.web_client_id
        )
    }

    val oneTapClient = remember {
        Identity.getSignInClient(context)
    }

    val signInRequest = remember {
        BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(webClientId)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
    }

    val googleLauncher =
        rememberLauncherForActivityResult(
            contract =
                androidx.activity.result.contract
                    .ActivityResultContracts
                    .StartIntentSenderForResult()
        ) { result ->

            try {

                val credential =
                    oneTapClient
                        .getSignInCredentialFromIntent(
                            result.data
                        )

                val idToken =
                    credential.googleIdToken

                if (idToken != null) {

                    val firebaseCredential =
                        GoogleAuthProvider.getCredential(
                            idToken,
                            null
                        )

                    auth.signInWithCredential(
                        firebaseCredential
                    )

                        .addOnSuccessListener {

                            loading = false

                            navController.navigate("main")
                        }
                }

            } catch (e: Exception) {

                loading = false

                Toast.makeText(
                    context,
                    "Google sign-in failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = rememberAsyncImagePainter(
                "https://images.unsplash.com/photo-1496747611176-843222e1e57c"
            ),

            contentDescription = null,

            modifier = Modifier.fillMaxSize(),

            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.2f),
                            Color.Black.copy(alpha = 0.92f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(
                    rememberScrollState()
                ),

            verticalArrangement = Arrangement.Bottom
        ) {

            Text(
                text = "UnboxWStyle",
                color = Color.White,
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "AI powered fashion styling ✨",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 18.sp
            )

            Spacer(
                modifier = Modifier.height(40.dp)
            )

            Card(
                shape = RoundedCornerShape(32.dp),

                colors = CardDefaults.cardColors(
                    containerColor =
                        Color.White.copy(alpha = 0.08f)
                )
            ) {

                Column(
                    modifier = Modifier.padding(22.dp)
                ) {

                    OutlinedButton(

                        onClick = {

                            loading = true

                            oneTapClient.beginSignIn(
                                signInRequest
                            )

                                .addOnSuccessListener { result ->

                                    googleLauncher.launch(

                                        androidx.activity.result.IntentSenderRequest.Builder(
                                            result.pendingIntent.intentSender
                                        ).build()
                                    )
                                }

                                .addOnFailureListener {

                                    loading = false

                                    Toast.makeText(
                                        context,
                                        "Google sign-in unavailable",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),

                        shape = RoundedCornerShape(18.dp)
                    ) {

                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = null
                        )

                        Spacer(
                            modifier = Modifier.width(10.dp)
                        )

                        Text(
                            text = "Continue with Google",
                            fontSize = 16.sp
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(22.dp)
                    )

                    Text(
                        text = "OR",
                        color = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.align(
                            Alignment.CenterHorizontally
                        )
                    )

                    Spacer(
                        modifier = Modifier.height(22.dp)
                    )

                    OutlinedTextField(

                        value = phoneNumber,

                        onValueChange = {

                            if (it.length <= 10) {

                                phoneNumber =
                                    it.filter { char ->
                                        char.isDigit()
                                    }
                            }
                        },

                        label = {
                            Text("Mobile Number")
                        },

                        leadingIcon = {

                            Text(
                                text = "+91",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        },

                        placeholder = {
                            Text("9876543210")
                        },

                        modifier = Modifier.fillMaxWidth(),

                        shape = RoundedCornerShape(18.dp),

                        singleLine = true
                    )

                    if (otpSent) {

                        Spacer(
                            modifier = Modifier.height(18.dp)
                        )

                        OutlinedTextField(

                            value = otp,

                            onValueChange = {

                                otp = it
                            },

                            label = {
                                Text("Enter OTP")
                            },

                            modifier = Modifier.fillMaxWidth(),

                            shape = RoundedCornerShape(18.dp),

                            singleLine = true
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )

                    Button(
                        onClick = {

                            loading = true

                            if (!otpSent) {

                                val options =
                                    PhoneAuthOptions
                                        .newBuilder(auth)

                                        .setPhoneNumber(
                                            "+91$phoneNumber"
                                        )

                                        .setTimeout(
                                            60L,
                                            TimeUnit.SECONDS
                                        )

                                        .setActivity(
                                            context as Activity
                                        )

                                        .setCallbacks(

                                            object :
                                                PhoneAuthProvider
                                                .OnVerificationStateChangedCallbacks() {

                                                override fun onVerificationCompleted(
                                                    credential: PhoneAuthCredential
                                                ) {

                                                    auth.signInWithCredential(
                                                        credential
                                                    )

                                                        .addOnSuccessListener {

                                                            loading = false

                                                            navController.navigate("main")
                                                        }
                                                }

                                                override fun onVerificationFailed(
                                                    e: FirebaseException
                                                ) {

                                                    loading = false

                                                    Toast.makeText(
                                                        context,
                                                        "Verification failed",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }

                                                override fun onCodeSent(
                                                    verificationIdValue: String,
                                                    token: PhoneAuthProvider.ForceResendingToken
                                                ) {

                                                    loading = false

                                                    otpSent = true

                                                    verificationId =
                                                        verificationIdValue

                                                    Toast.makeText(
                                                        context,
                                                        "OTP Sent",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        )

                                        .build()

                                PhoneAuthProvider
                                    .verifyPhoneNumber(
                                        options
                                    )

                            } else {

                                val credential =
                                    PhoneAuthProvider.getCredential(
                                        verificationId,
                                        otp
                                    )

                                auth.signInWithCredential(
                                    credential
                                )

                                    .addOnSuccessListener {

                                        loading = false

                                        navController.navigate("main")
                                    }

                                    .addOnFailureListener {

                                        loading = false

                                        Toast.makeText(
                                            context,
                                            "Invalid OTP",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }

                        },

                        enabled =
                            if (!otpSent)
                                phoneNumber.length == 10
                            else
                                otp.length >= 6,

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = Accent
                        )
                    ) {

                        if (loading) {

                            CircularProgressIndicator(
                                color = Color.White
                            )

                        } else {

                            Text(
                                text =
                                    if (!otpSent)
                                        "Send OTP"
                                    else
                                        "Verify OTP",

                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(40.dp)
            )
        }
    }
}