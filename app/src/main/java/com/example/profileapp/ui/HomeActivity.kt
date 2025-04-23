package com.example.profileapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.profileapp.data.User
import com.example.profileapp.data.UserDatabase
import com.example.profileapp.databinding.ActivityHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var database: UserDatabase
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = UserDatabase.getDatabase(this)
        userId = intent.getIntExtra("USER_ID", 0)

        loadUserData()

        binding.btnUpdate.setOnClickListener {
            val intent = Intent(this, UpdateProfileActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun loadUserData() {
        CoroutineScope(Dispatchers.IO).launch {
            val user = database.userDao().getUserById(userId)
            runOnUiThread {
                if (user != null) {
                    binding.tvName.text = user.name
                    binding.tvEmail.text = user.email
                    binding.tvPhone.text = user.phone
                    binding.tvAddress.text = user.address
                }
            }
        }
    }
}