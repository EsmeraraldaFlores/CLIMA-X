package com.example.staysunny.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.staysunny.databinding.FragmentPersonalInformationVariantBinding
import com.example.staysunny.utils.FragmentCommunicator
import com.example.staysunny.viewModel.PersonalInformationVariantViewModel
import com.example.staysunny.view.OnboardingActivity
import com.example.staysunny.view.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class PersonalInformationVariantFragment : Fragment() {

    private var _binding: FragmentPersonalInformationVariantBinding? = null
    private val binding get() = _binding!!
    private lateinit var communicator: FragmentCommunicator
    private val viewModel by viewModels<PersonalInformationVariantViewModel>()
    private val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalInformationVariantBinding.inflate(inflater, container, false)
        communicator = requireActivity() as OnboardingActivity
        setupView()
        return binding.root
    }

    private fun setupView() {
        val userId = arguments?.let {
            PersonalInformationVariantFragmentArgs.fromBundle(it).userId
        }

        binding.tietBirthDate.apply {
            isFocusable = false
            isClickable = true
        }

        binding.btContinue.setOnClickListener {
            Log.e("BOTON", "HA ENTRADO EN EL BOTON")
            if (userId != null) {
                viewModel.createUserInfo(userId,
                    binding.tietName.text.toString(),
                    binding.tietLastNames.text.toString(),
                    binding.tietUsername.text.toString(),
                    format.parse(binding.tietBirthDate.text.toString()) ?: Date()
                )
            } else {
                Toast.makeText(requireContext(), "User ID is missing", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tietBirthDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(), { _, y, m, d ->
                val selectedDate = String.format("%02d/%02d/%04d", d, m + 1, y)
                binding.tietBirthDate.setText(selectedDate)
            }, year, month, day)

            datePicker.show()
        }

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.loaderState.observe(viewLifecycleOwner) {
            communicator.showLoader(it)
        }

        viewModel.operationSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                val action = PersonalInformationVariantFragmentDirections.actionPersonalInformationVariantToPermission()
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

