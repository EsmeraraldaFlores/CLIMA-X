package com.example.staysunny.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.staysunny.databinding.ActivityHomeBinding
import com.example.staysunny.utils.FragmentCommunicator
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), FragmentCommunicator {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(binding.homeContainerView.id) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNav: BottomNavigationView = binding.navView
        bottomNav.setupWithNavController(navController)
    }

    override fun showLoader(value: Boolean) {
        binding.loaderContainerView.visibility =
            if (value) View.VISIBLE else View.GONE
    }
}
