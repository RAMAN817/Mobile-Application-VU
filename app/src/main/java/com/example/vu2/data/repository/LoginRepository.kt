package com.example.vu2.data.repository

import com.example.vu2.data.api.ApiService
import com.example.vu2.data.api.LoginRequest
import jakarta.inject.Inject

class LoginRepository @Inject constructor(
    private val apiService: ApiService
){
    suspend fun login(username: String, password: String): Result<String> {
        return try {
            val response = apiService.login(LoginRequest(username, password))
            if (response.isSuccessful) {
                val keypass = response.body()?.keypass
                if (keypass != null) Result.success(keypass)
                else Result.failure(Exception("Empty response from server"))
            } else {
                when (response.code()) {
                    401 -> Result.failure(Exception("Invalid credentials. Check your name and student ID."))
                    404 -> Result.failure(Exception("Endpoint not found. Check your auth URL."))
                    500 -> Result.failure(Exception("Server error. Try again in a moment."))
                    else -> Result.failure(Exception("Login failed (${response.code()})"))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error. Are you connected?"))
        }
    }
}