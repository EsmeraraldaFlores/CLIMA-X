package com.example.staysunny.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.staysunny.R
import com.example.staysunny.databinding.FragmentPermissionBinding
import com.example.staysunny.viewModel.PermissionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionFragment : Fragment() {

    private var _binding: FragmentPermissionBinding? = null
    private val binding get() = _binding!!
    private val permissionVM by viewModels<PermissionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPermissionBinding.inflate(inflater, container, false)
        initUI()
        return binding.root
    }

    private fun initUI() = with(binding) {
        fbContinue.setOnClickListener {
            permissionVM.triggerSuccessMessage()
            findNavController().navigate(R.id.action_permission_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
