package com.example.pdfassignment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pdfassignment.databinding.ActivityMainBinding
import com.example.pdfassignment.viewModel.AuthViewModel
import com.example.pdfassignment.views.HomeActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: AuthViewModel by viewModels()

    // Activity result launcher for Google Sign-In
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful) {
                val account = task.result
                viewModel.handleGoogleSignInResult(account)
                showToast("Google Signin Successfully")
            } else {
                showToast("Google sign in failed")
            }
        } else {
            showToast("Google sign in cancelled")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupClickListeners()
        observeViewModel()

    }

    private fun setupClickListeners() {
        // Email/Password sign up button
        binding.button.setOnClickListener {
            val email = binding.email.text?.toString() ?: ""
            val password = binding.pass.text?.toString() ?: ""
            viewModel.createUserWithEmailAndPassword(email, password)
        }

        // Google sign in button
        binding.google.setOnClickListener {
            val signInIntent = viewModel.getGoogleSignInIntent()
            googleSignInLauncher.launch(signInIntent)
        }
    }

    private fun observeViewModel() {
        // Observe authentication state
        viewModel.authState.observe(this) { state ->
            when (state) {
                AuthViewModel.AuthState.LOADING -> showLoading(true)
                AuthViewModel.AuthState.AUTHENTICATED -> handleAuthenticated()
                AuthViewModel.AuthState.UNAUTHENTICATED -> showLoading(false)
                AuthViewModel.AuthState.ERROR -> showLoading(false)
            }
        }

        // Observe user messages
        viewModel.userMessage.observe(this) { message ->
            message?.let {
                showToast(it)
                viewModel.clearUserMessage()
            }
        }
    }

    private fun handleAuthenticated() {
        showLoading(false)
        startActivity(Intent(this, HomeActivity::class.java))
        // Don't finish() here if you want the user to be able to go back to the login screen
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

        // Disable input fields and buttons during loading
        binding.email.isEnabled = !isLoading
        binding.pass.isEnabled = !isLoading
        binding.button.isEnabled = !isLoading
        binding.google.isEnabled = !isLoading
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}