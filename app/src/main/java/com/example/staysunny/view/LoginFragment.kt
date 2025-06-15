package com.example.staysunny.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.staysunny.databinding.FragmentLoginBinding
import com.example.staysunny.utils.FragmentCommunicator
import com.example.staysunny.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel by viewModels<LoginViewModel>()
    private lateinit var bridge: FragmentCommunicator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        bridge = requireActivity() as FragmentCommunicator
        initListeners()
        observeViewModel()
        return binding.root
    }

    private fun initListeners() = with(binding) {
        btLogin.setOnClickListener {
            val email = tietEmail.text.toString()
            val password = tietPassword.text.toString()
            loginViewModel.requestLogin(email, password)
        }

        tvAccount.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }

        tvForgot.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToResetPasswordFragment())
        }

        tietEmail.addTextChangedListener {
            tfEmail.error = if (it.isNullOrEmpty()) "Email required" else null
        }

        tietPassword.addTextChangedListener {
            tfPassword.error = if (it.isNullOrEmpty()) "Password required" else null
        }
    }

    private fun observeViewModel() {
        loginViewModel.loaderState.observe(viewLifecycleOwner) {
            bridge.showLoader(it)
        }

        loginViewModel.sessionValid.observe(viewLifecycleOwner) { success ->
            if (success) {
                startActivity(Intent(requireContext(), HomeActivity::class.java))
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

