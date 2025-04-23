package com.example.profileapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.profileapp.data.User
import com.example.profileapp.data.UserDatabase
import com.example.profileapp.databinding.ActivityUpdateProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProfileBinding
    private lateinit var database: UserDatabase
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = UserDatabase.getDatabase(this)
        userId = intent.getIntExtra("USER_ID", 0)

        loadUserData()

        binding.btnUpdate.setOnClickListener {
            updateUser()
        }
    }

    private fun loadUserData() {
        CoroutineScope(Dispatchers.IO).launch {
            val user = database.userDao().getUserById(userId)
            runOnUiThread {
                if (user != null) {
                    binding.etName.setText(user.name)
                    binding.etEmail.setText(user.email)
                    binding.etPhone.setText(user.phone)
                    binding.etAddress.setText(user.address)
                }
            }
        }
    }

    private fun updateUser() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val existingUser = database.userDao().getUserById(userId)
            if (existingUser != null) {
                val updatedUser = existingUser.copy(
                    name = name,
                    email = email,
                    phone = phone,
                    address = address
                )
                database.userDao().updateUser(updatedUser)
                runOnUiThread {
                    Toast.makeText(this@UpdateProfileActivity, "Profile updated", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@UpdateProfileActivity, HomeActivity::class.java)
                    intent.putExtra("USER_ID", userId)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}