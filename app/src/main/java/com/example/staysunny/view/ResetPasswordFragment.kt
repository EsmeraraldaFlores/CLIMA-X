package com.example.staysunny.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.staysunny.R
import com.example.staysunny.databinding.FragmentResetPasswordBinding
import com.example.staysunny.utils.FragmentCommunicator
import com.example.staysunny.viewModel.ResetPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordFragment : Fragment() {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!
    private val resetVM by viewModels<ResetPasswordViewModel>()
    private lateinit var uiBridge: FragmentCommunicator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        uiBridge = requireActivity() as FragmentCommunicator
        initListeners()
        observeViewModel()
        return binding.root
    }

    private fun initListeners() = with(binding) {
        btnReset.setOnClickListener {
            val emailInput = tietEmail.text.toString().trim()

            when {
                emailInput.isEmpty() -> {
                    Toast.makeText(requireContext(), "Please enter your email", Toast.LENGTH_SHORT).show()
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches() -> {
                    Toast.makeText(requireContext(), "Invalid email format", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    resetVM.sendPasswordResetEmail(emailInput)
                }
            }
        }

        btBack.setOnClickListener {
            findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
        }
    }

    private fun observeViewModel() {
        resetVM.loaderState.observe(viewLifecycleOwner) {
            uiBridge.showLoader(it)
        }

        resetVM.passwordResetState.observe(viewLifecycleOwner) { sent ->
            if (sent) {
                Toast.makeText(requireContext(), "Email sent! Check your inbox.", Toast.LENGTH_LONG).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
                }, 2000)
            } else {
                Toast.makeText(requireContext(), "Failed to send reset email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
