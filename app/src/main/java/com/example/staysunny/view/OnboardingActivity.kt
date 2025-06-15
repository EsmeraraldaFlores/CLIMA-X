package com.example.staysunny.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.staysunny.databinding.ActivityOnboardingBinding
import com.example.staysunny.utils.FragmentCommunicator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity(), FragmentCommunicator {

    private lateinit var binding: ActivityOnboardingBinding
    // Navegación opcional comentada
    // private lateinit var appBarConfig: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
        setSupportActionBar(binding.toolbar)

        val controller = findNavController(R.id.nav_host_fragment_content_onboarding)
        appBarConfig = AppBarConfiguration(controller.graph)
        setupActionBarWithNavController(controller, appBarConfig)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab)
                .show()
        }
        */
    }

    /*
    override fun onSupportNavigateUp(): Boolean {
        val controller = findNavController(R.id.nav_host_fragment_content_onboarding)
        return controller.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }
    */

    override fun showLoader(value: Boolean) {
        binding.loaderContainerView.visibility = if (value) View.VISIBLE else View.GONE
    }
}
