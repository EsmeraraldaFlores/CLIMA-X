package com.example.staysunny.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.staysunny.R
import com.example.staysunny.databinding.FragmentRegisterBinding
import com.example.staysunny.utils.FragmentCommunicator
import com.example.staysunny.viewModel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val registerVM by viewModels<RegisterViewModel>()
    private lateinit var bridge: FragmentCommunicator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        bridge = requireActivity() as FragmentCommunicator
        initUI()
        observeVM()
        return binding.root
    }

    private fun initUI() = with(binding) {
        btBack.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        btRegister.setOnClickListener {
            val email = tietEmail.text.toString()
            val pass = tietPassword.text.toString()
            registerVM.requestRegister(email, pass)
        }
    }

    private fun observeVM() {
        registerVM.loaderState.observe(viewLifecycleOwner) {
            bridge.showLoader(it)
        }

        registerVM.createdUser.observe(viewLifecycleOwner) { userId ->
            val direction = RegisterFragmentDirections
                .actionRegisterFragmentToPersonalInformationVariant(userId)
            findNavController().navigate(direction)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
