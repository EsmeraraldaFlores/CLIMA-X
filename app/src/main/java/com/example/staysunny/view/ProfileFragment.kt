package com.example.staysunny.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.staysunny.R
import com.example.staysunny.databinding.FragmentProfileBinding
import com.example.staysunny.model.User
import com.example.staysunny.utils.FragmentCommunicator
import com.example.staysunny.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale


@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var communicator: FragmentCommunicator
    private val viewModel by viewModels<ProfileViewModel>()
    val locale = Locale("es", "MX")
    val dateFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", locale)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this
        _binding = FragmentProfileBinding.inflate(inflater,container, false)
        communicator = requireActivity() as HomeActivity
        setupView()
        return binding.root
    }

    private fun setupView() {
        setupObservers()
        viewModel.getUserInfo()
        binding.Updatebutton.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_personalInfoFragment)
        }
    }

    private fun setupObservers() {
        viewModel.userInfo.observe(viewLifecycleOwner) { user ->
            updateUI(user)
        }
        viewModel.loaderState.observe(viewLifecycleOwner) { loaderState ->
            communicator.showLoader(loaderState)
        }
    }

    private fun updateUI(user: User) {
        binding?.apply {
            userFullNameLabel.text = "${user.name} ${user.lastName}"
            userNameLabel.text = user.userName
            userBornDateLabel.text = dateFormat.format(user.bornDate)
        }
    }
}