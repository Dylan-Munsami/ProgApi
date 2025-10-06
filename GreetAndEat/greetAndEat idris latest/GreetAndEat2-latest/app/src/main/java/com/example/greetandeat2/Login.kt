package com.example.greetandeat2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginBtn: Button
    private lateinit var googleBtn: Button
    private lateinit var registerBtn: Button

    private val RC_SIGN_IN = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Firebase Auth instance
        auth = FirebaseAuth.getInstance()

        // Views
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginBtn = findViewById(R.id.loginBtn)
        googleBtn = findViewById(R.id.googleBtn)
        registerBtn = findViewById(R.id.registerBtn)

        // Google Sign-In configuration
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("187103160430-oc5i3k2gdeqej5fodpne796b6ulee401.apps.googleusercontent.com") // Replace with your client ID
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Normal Login
        loginBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and password required", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }

        // Google Sign-In (always show account chooser)
        googleBtn.setOnClickListener {
            googleSignInClient.signOut().addOnCompleteListener {
                val signInIntent = googleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        }

        // Go to Register page
        registerBtn.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Welcome back ${user?.email}", Toast.LENGTH_SHORT).show()

                    // ✅ First login reward
                    if (!RewardsManager.isRewardUnlocked(this, "first_login")) {
                        RewardsManager.unlockReward(this, "first_login")
                    }

                    // ✅ Daily check-in
                    RewardsManager.handleDailyCheckIn(this)

                    startActivity(Intent(this, Home::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google Sign-In failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Google Login success: ${user?.displayName}", Toast.LENGTH_SHORT).show()

                    // ✅ First login reward
                    if (!RewardsManager.isRewardUnlocked(this, "first_login")) {
                        RewardsManager.unlockReward(this, "first_login")
                    }

                    // ✅ Daily check-in
                    RewardsManager.handleDailyCheckIn(this)

                    startActivity(Intent(this, Home::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Google Auth failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
