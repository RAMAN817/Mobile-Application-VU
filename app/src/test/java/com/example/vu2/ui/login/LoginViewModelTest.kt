package com.example.vu2.ui.login

import com.example.vu2.data.api.ApiService
import com.example.vu2.data.api.LoginRequest
import com.example.vu2.data.api.LoginResponse
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
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var apiService: ApiService
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        apiService = mockk()
        viewModel = LoginViewModel(apiService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Idle`() {
        assertTrue(viewModel.uiState.value is LoginUiState.Idle)
    }

    @Test
    fun `empty username triggers validation error`() = runTest {
        viewModel.login("", "password123")
        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.ValidationError)
        val error = state as LoginUiState.ValidationError
        assertEquals("Username cannot be empty", error.usernameError)
    }

    @Test
    fun `empty password triggers validation error`() = runTest {
        viewModel.login("John", "")
        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.ValidationError)
        val error = state as LoginUiState.ValidationError
        assertEquals("Password cannot be empty", error.passwordError)
    }

    @Test
    fun `short password triggers validation error`() = runTest {
        viewModel.login("John", "abc")
        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.ValidationError)
        val error = state as LoginUiState.ValidationError
        assertEquals("Password must be at least 6 characters long", error.passwordError)
    }

    @Test
    fun `successful login emits Success state with keypass`() = runTest {
        coEvery {
            apiService.login(any())
        } returns Response.success(LoginResponse("technology"))

        viewModel.login("s1234567", "John")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.Success)
        assertEquals("technology", (state as LoginUiState.Success).keypass)
    }

    @Test
    fun `failed login emits Error state`() = runTest {
        coEvery {
            apiService.login(any())
        } returns Response.error(401, okhttp3.ResponseBody.create(null, "Unauthorized"))

        viewModel.login("s1234567", "WrongName")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is LoginUiState.Error)
    }

    @Test
    fun `network exception emits Error state`() = runTest {
        coEvery {
            apiService.login(any())
        } throws Exception("No internet")

        viewModel.login("s1234567", "John")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is LoginUiState.Error)
    }

    @Test
    fun `login calls api with correct credentials`() = runTest {
        coEvery {
            apiService.login(any())
        } returns Response.success(LoginResponse("technology"))

        viewModel.login("s1234567", "John")
        advanceUntilIdle()

        coVerify { apiService.login(LoginRequest("s1234567", "John")) }
    }
}
