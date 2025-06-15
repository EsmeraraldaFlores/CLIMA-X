package com.example.staysunny.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.staysunny.R
import com.example.staysunny.databinding.FragmentPersonalInfoBinding
import com.example.staysunny.model.User
import com.example.staysunny.utils.FragmentCommunicator
import com.example.staysunny.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class PersonalInfoFragment : Fragment() {

    private var _binding: FragmentPersonalInfoBinding? = null
    private val binding get() = _binding!!
    private val profileVM by viewModels<ProfileViewModel>()
    private lateinit var uiBridge: FragmentCommunicator
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalInfoBinding.inflate(inflater, container, false)
        uiBridge = requireActivity() as FragmentCommunicator
        setupUI()
        return binding.root
    }

    private fun setupUI() = with(binding) {
        profileVM.getUserInfo()
        observeViewModel()

        bornDateTiet.apply {
            isFocusable = false
            isClickable = true
            setOnClickListener { showDatePicker() }
        }

        saveDataButton.setOnClickListener {
            val parsedDate = runCatching {
                dateFormat.parse(bornDateTiet.text.toString())
            }.getOrNull() ?: Date()

            profileVM.updateUserInfo(
                userFirstNameTiet.text.toString(),
                userLastNameTiet.text.toString(),
                userNameTiet.text.toString(),
                parsedDate
            )
        }

        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_personalInfoFragment_to_profileFragment)
        }
    }

    private fun observeViewModel() {
        profileVM.userInfo.observe(viewLifecycleOwner) { updateFormFields(it) }
        profileVM.loaderState.observe(viewLifecycleOwner) { uiBridge.showLoader(it) }
        profileVM.operationSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                startActivity(Intent(requireContext(), HomeActivity::class.java))
                activity?.finish()
            }
        }
    }

    private fun updateFormFields(user: User) = with(binding) {
        userFirstNameTiet.setText(user.name)
        userLastNameTiet.setText(user.lastName)
        userNameTiet.setText(user.userName)
        bornDateTiet.setText(dateFormat.format(user.bornDate))
    }

    private fun showDatePicker() {
        val today = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, y, m, d -> binding.bornDateTiet.setText(String.format("%02d/%02d/%04d", d, m + 1, y)) },
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
