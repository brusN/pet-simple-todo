package ru.nsu.brusn.smpltodo.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.nsu.brusn.smpltodo.BuildConfig
import ru.nsu.brusn.smpltodo.api.model.dto.request.SignUpRequest
import ru.nsu.brusn.smpltodo.api.model.dto.response.MessageResponse
import ru.nsu.brusn.smpltodo.api.services.AuthService
import ru.nsu.brusn.smpltodo.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    private fun navigateToSignInActivity() {
        Intent(this, SignInActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .also {
                startActivity(it)
            }
    }

    private fun showShortToast(msg: String) {
        Toast.makeText(this@SignUpActivity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun signup() {
        val etNickname = binding.etNickname
        val etPassword = binding.etPassword
        val etRepeatPassword = binding.etRepeatPassword

        if (etPassword.text.toString() != etRepeatPassword.text.toString()) {
            showShortToast("Passwords doesn't match")
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val authService: AuthService = retrofit.create(AuthService::class.java)
        val requestBody = SignUpRequest(etNickname.text.toString(), etPassword.text.toString())

        authService.signUp(requestBody).enqueue(object : Callback<MessageResponse> {
            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {

                if (response.errorBody() != null) {
                    val jsonObject = JSONObject(response.errorBody()?.string())
                    showShortToast(jsonObject.getJSONObject("error").getString("message"))
                    return
                }

                val body = response.body()!!
                if (response.isSuccessful) {
                    navigateToSignInActivity()
                } else {
                    showShortToast(body.error.message)
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Log.e("DEBUG", t.toString())
                showShortToast("Error while sign up")
            }
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            signup()
        }

        binding.btnSignIn.setOnClickListener {
            navigateToSignInActivity()
        }
    }
}

