package com.example.vu2.ui.dashboard

import com.example.vu2.data.repository.DashboardRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var repository: DashboardRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = DashboardViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() {
        assertTrue(viewModel.uiState.value is DashboardUiState.Loading)
    }

    @Test
    fun `successful fetch emits Success state with entities`() = runTest {
        val fakeEntities = listOf(
            mapOf("name" to "Kotlin", "type" to "Language", "description" to "Modern JVM language"),
            mapOf("name" to "Retrofit", "type" to "Library", "description" to "HTTP client for Android")
        )
        coEvery { repository.getEntities("technology") } returns Result.success(fakeEntities)

        viewModel.loadEntities("technology")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is DashboardUiState.Success)
        assertEquals(2, (state as DashboardUiState.Success).entities.size)
    }

    @Test
    fun `failed fetch emits Error state`() = runTest {
        coEvery {
            repository.getEntities("technology")
        } returns Result.failure(Exception("Network error. Are you connected?"))

        viewModel.loadEntities("technology")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is DashboardUiState.Error)
        assertEquals("Network error. Are you connected?", (state as DashboardUiState.Error).message)
    }

    @Test
    fun `loadEntities calls repository with correct keypass`() = runTest {
        coEvery { repository.getEntities(any()) } returns Result.success(emptyList())

        viewModel.loadEntities("technology")
        advanceUntilIdle()

        coVerify { repository.getEntities("technology") }
    }

    @Test
    fun `success state contains correct entity data`() = runTest {
        val fakeEntities = listOf(
            mapOf("name" to "Android", "year" to "2008", "description" to "Mobile OS by Google")
        )
        coEvery { repository.getEntities(any()) } returns Result.success(fakeEntities)

        viewModel.loadEntities("technology")
        advanceUntilIdle()

        val state = viewModel.uiState.value as DashboardUiState.Success
        assertEquals("Android", state.entities[0]["name"])
        assertEquals("2008", state.entities[0]["year"])
    }

    @Test
    fun `empty entity list still emits Success state`() = runTest {
        coEvery { repository.getEntities(any()) } returns Result.success(emptyList())

        viewModel.loadEntities("technology")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is DashboardUiState.Success)
        assertEquals(0, (state as DashboardUiState.Success).entities.size)
    }
}
