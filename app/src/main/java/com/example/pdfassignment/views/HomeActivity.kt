package com.example.pdfassignment.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.semantics.text
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.example.pdfassignment.MainActivity
import com.example.pdfassignment.R
import com.example.pdfassignment.databinding.ActivityHomeBinding
import com.example.pdfassignment.model.localDB.User
import com.example.pdfassignment.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Check if a user is already signed in
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // No user is signed in, redirect to the sign-in screen (e.g., MainActivity)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return // Exit early since the user is not authenticated
        }

        displayUserDetails(currentUser)
        addUserToDatabase(currentUser)

        binding.btnLogOut.setOnClickListener {
            startActivity(Intent(this, SignOutActivity::class.java))
            finish()
        }
    }

    private fun displayUserDetails(user: FirebaseUser) {
        val name = user.displayName
        val email = user.email

        binding.tvUserName.text = "Hi $name"
        binding.tvUserEmail.text = email
    }

    private fun addUserToDatabase(user: FirebaseUser) {
        val userId = user.uid
        val name = user.displayName ?: ""
        val email = user.email ?: ""

        userViewModel.getUser(userId)

//        userViewModel.userFromDb.observe(this, Observer { userFromDb ->
//            if (userFromDb != null) {
//                Toast.makeText(this, "User found in the database, nothing to do.", Toast.LENGTH_SHORT).show()
//                Log.i("HomeActivity", "User found in the database, nothing to do.")
//            } else {
//                // User not found in the database, add it
//                Toast.makeText(this, "User not found in the database, adding it.", Toast.LENGTH_SHORT).show()
//                Log.i("HomeActivity", "User not found in the database, adding it.")
//                val newUser = User(userId = userId, userName = name, userEmail = email)
//                userViewModel.addUser(newUser)
//            }
//        })

        userViewModel.userFromDb.observe(this) { userFromDb ->
            userViewModel.userFromDb.removeObservers(this) // avoid multiple triggers

            if (userFromDb != null) {
                Toast.makeText(this, "User already in database.", Toast.LENGTH_SHORT).show()
                Log.i("HomeActivity", "User found in the database, nothing to do.")
            } else {
                Toast.makeText(this, "Adding new user to database.", Toast.LENGTH_SHORT).show()
                Log.i("HomeActivity", "User not found in the database, adding it.")
                val newUser = User(userId = userId, userName = name, userEmail = email)
                userViewModel.addUser(newUser)
            }
        }
    }
}