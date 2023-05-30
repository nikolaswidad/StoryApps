package com.nikolaswidad.storyappsnew.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import com.nikolaswidad.storyappsnew.ViewModelFactory
import com.nikolaswidad.storyappsnew.data.Resource
import com.nikolaswidad.storyappsnew.databinding.ActivityRegisterBinding
import com.nikolaswidad.storyappsnew.ui.utils.hide
import com.nikolaswidad.storyappsnew.ui.utils.show
import com.nikolaswidad.storyappsnew.ui.utils.showToast

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: AuthViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)
        setView()

        binding.btnRegister.setOnClickListener {
            validate()
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


        val title = ObjectAnimator.ofFloat(binding.tvCreateAccount, View.ALPHA, 1f).setDuration(250)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(250)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(250)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(250)
        val register = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(250)

        AnimatorSet().apply {
            playSequentially(
                title, nameEditTextLayout, emailEditTextLayout, passwordEditTextLayout, register
            )
            startDelay = 500
        }.start()
    }

    private fun validate() {
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()
        val name = binding.edRegisterName.text.toString()

        when {
            name.isEmpty() -> {
                binding.edRegisterName.error = "Input Your Name"
                binding.edRegisterName.requestFocus()
            }
            email.isEmpty() -> {
                binding.edRegisterEmail.error = "Input Your Email"
                binding.edRegisterEmail.requestFocus()
            }
            password.isEmpty() -> {
                binding.edRegisterPassword.error = "Input your Password"
                binding.edRegisterPassword.requestFocus()
            }
            else -> {
                register()
            }
        }
    }

    private fun register() {
        val name = binding.edRegisterName.text.toString()
        val email = binding.edRegisterName.text.toString()
        val password = binding.edRegisterPassword.text.toString()

        viewModel.register(name, email, password).observe(this) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding.progressCircular.show()
                }
                is Resource.Success -> {
                    binding.progressCircular.hide()
                    showToast(response.data.message.toString())
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