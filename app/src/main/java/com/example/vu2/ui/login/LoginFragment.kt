package com.example.vu2.ui.login

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.vu2.R
import com.example.vu2.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        binding.btnLogin.setOnClickListener {
            viewModel.login(
                username = binding.etUsername.text.toString().trim(),
                password = binding.etPassword.text.toString().trim()
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is LoginUiState.Idle -> showIdle()
                        is LoginUiState.Loading -> showLoading()
                        is LoginUiState.ValidationError -> showValidationError(state)
                        is LoginUiState.Success -> navigateToDashboard(state.keypass)
                        is LoginUiState.Error -> showError(state.message)
                    }
                }
            }
        }
    }

    private fun showIdle() {
        binding.progressBar.isVisible = false
        binding.btnLogin.isEnabled = true
        binding.tvError.isVisible = false
        binding.tilUsername.error = null
        binding.tilPassword.error = null
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.btnLogin.isEnabled = false
        binding.tvError.isVisible = false
        binding.tilUsername.error = null
        binding.tilPassword.error = null
    }

    private fun showValidationError(state: LoginUiState.ValidationError) {
        binding.progressBar.isVisible = false
        binding.btnLogin.isEnabled = true
        binding.tilUsername.error = state.usernameError
        binding.tilPassword.error = state.passwordError
    }

    private fun showError(message: String) {
        binding.progressBar.isVisible = false
        binding.btnLogin.isEnabled = true
        binding.tvError.isVisible = true
        binding.tvError.text = message
    }

    private fun navigateToDashboard(keypass: String) {
        val bundle = Bundle().apply {
            putString("keypass", keypass)
        }
        findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
