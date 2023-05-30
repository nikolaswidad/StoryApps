package com.nikolaswidad.storyappsnew.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import com.nikolaswidad.storyappsnew.ViewModelFactory
import com.nikolaswidad.storyappsnew.data.Resource
import com.nikolaswidad.storyappsnew.databinding.ActivityLoginBinding
import com.nikolaswidad.storyappsnew.preferences.UserPreferences
import com.nikolaswidad.storyappsnew.ui.main.MainActivity
import com.nikolaswidad.storyappsnew.ui.utils.hide
import com.nikolaswidad.storyappsnew.ui.utils.show
import com.nikolaswidad.storyappsnew.ui.utils.showToast

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var preferences: UserPreferences
    private lateinit var factory: ViewModelFactory
    private val viewModel: AuthViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setView()
        setupViewModel()
        getSession()

        binding.btnLogin.setOnClickListener {
            validate()
        }
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)
        preferences = UserPreferences(this)
    }

    private fun getSession() {
        if (!preferences.getToken().isNullOrEmpty()) {
            startActivity(Intent(this, MainActivity::class.java).also {
                finish()
            })
        }
    }

    private fun validate() {
        val email = binding.edLoginEmail.text.toString()
        val password = binding.edLoginPassword.text.toString()

        when {
            email.isEmpty() -> {
                binding.edLoginEmail.error = "Input Your Email"
                binding.edLoginEmail.requestFocus()
            }
            password.isEmpty() -> {
                binding.edLoginPassword.error = "Input Your Password"
                binding.edLoginPassword.requestFocus()
            }
            else -> {
                login()
            }
        }
    }

    private fun login() {
        val email = binding.edLoginEmail.text.toString()
        val password = binding.edLoginPassword.text.toString()

        viewModel.login(email, password).observe(this) { response ->
            if (response != null) {
                when (response) {
                    is Resource.Loading -> {
                        binding.progressCircular.show()
                    }
                    is Resource.Success -> {
                        binding.progressCircular.hide()
                        showToast(response.data.message)
                        val data = response.data
                        preferences.setToken(data.loginResult.token)
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra(MainActivity.EXTRA_DATA, data.loginResult.token)
                        startActivity(intent)
                        finish()
                    }
                    is Resource.Error -> {
                        binding.progressCircular.hide()
                        showToast(response.error)
                    }
                }
            }
        }
    }

    private fun setView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

        ObjectAnimator.ofFloat(binding.ivLogo, View.TRANSLATION_X, -30F, 30F).apply {
            duration = 5000
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
        }.start()
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(250)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(250)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(250)
        val register = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(250)

        AnimatorSet().apply {
            playSequentially(
                emailEditTextLayout,
                passwordEditTextLayout,
                login,
                register
            )
            startDelay = 500
        }.start()
    }
}