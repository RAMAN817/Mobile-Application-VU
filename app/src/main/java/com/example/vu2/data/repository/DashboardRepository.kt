package com.example.vu2.data.repository

import com.example.vu2.data.api.ApiService
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getEntities(keypass: String): Result<List<Map<String, String>>> {
        return try {
            val response = apiService.getDashboard(keypass)
            if (response.isSuccessful) {
                val entities = response.body()?.entities
                if (entities != null) Result.success(entities)
                else Result.failure(Exception("No data returned from server"))
            } else {
                Result.failure(Exception("Failed to load data (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error. Are you connected?"))
        }
    }
}
