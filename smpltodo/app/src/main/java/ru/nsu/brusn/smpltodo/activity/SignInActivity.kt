package ru.nsu.brusn.smpltodo.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.nsu.brusn.smpltodo.BuildConfig
import ru.nsu.brusn.smpltodo.api.model.dto.request.SignInRequest
import ru.nsu.brusn.smpltodo.api.model.dto.response.JwtResponse
import ru.nsu.brusn.smpltodo.api.services.AuthService
import ru.nsu.brusn.smpltodo.databinding.ActivitySigninBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding

    private fun navigateToMainActivity() {
        Intent(this, MainActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .also {
                startActivity(it)
            }
    }

    private fun showShortToast(msg: String) {
        Toast.makeText(this@SignInActivity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun sendLoginRequest() {
        val etNickname = binding.etNickname
        val etPassword = binding.etPassword

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val authService : AuthService = retrofit.create(AuthService::class.java)
        val requestBody = SignInRequest(etNickname.text.toString(), etPassword.text.toString())

        authService.signIn(requestBody).enqueue(object: Callback<JwtResponse> {
            override fun onResponse(call: Call<JwtResponse>, response: Response<JwtResponse>) {
                val body = response.body()
                if (body == null) {
                    if (response.code() == 401) {
                        showShortToast("Invalid login/password")
                    } else {
                        showShortToast("Error while sign in")
                    }
                    return
                }

                if (response.isSuccessful) {
                    getSharedPreferences("auth", MODE_PRIVATE).edit().apply {
                        putString("jwt", body.data.jwt)
                        apply()
                    }
                    navigateToMainActivity()
                } else {
                    showShortToast(body.error.message)
                }
            }

            override fun onFailure(call: Call<JwtResponse>, t: Throwable) {
                Log.e("DEBUG", t.toString())
                showShortToast("Error while sign in")
            }
        })
    }

    private fun navigateToSignUpActivity() {
        Intent(this, SignUpActivity::class.java)
            .also {
                startActivity(it)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater, null,false)
        setContentView(binding.root)

        binding.btnSignIn.setOnClickListener {
            sendLoginRequest()
        }

        binding.btnSignUp.setOnClickListener {
            navigateToSignUpActivity()
        }
    }
}