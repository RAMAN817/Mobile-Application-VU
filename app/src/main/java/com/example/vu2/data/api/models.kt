package com.example.vu2.data.api

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val keypass: String
)

data class DashboardResponse(
    val entities: List<Map<String, String>>,
    val entityTotal: Int
)
