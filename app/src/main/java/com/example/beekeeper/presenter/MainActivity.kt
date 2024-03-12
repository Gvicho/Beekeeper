package com.example.beekeeper.presenter

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beekeeper.R
import com.example.beekeeper.databinding.ActivityMainBinding
import com.example.beekeeper.presenter.adapter.options.OptionsRecyclerAdapter
import com.example.beekeeper.presenter.model.drawer_menu.Options
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var optionsAdapter: OptionsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.appBarMain.contentMain.navView
        // Hide the ActionBar
        supportActionBar?.hide()
        initRecycler()

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.savedAnalyticsFragment,
                R.id.damagedBeehivesFragment,
                R.id.shareOrGetFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, _ ->
            when (destination.id) {
                R.id.splashFragment, R.id.loginFragment, R.id.registrationFragment, R.id.resetPasswordFragment, R.id.addReportFragment -> {
                    binding.appBarMain.contentMain.navView.visibility = View.GONE
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }

                else -> {
                    binding.appBarMain.contentMain.navView.visibility = View.VISIBLE
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
            }
        }

    }

    private fun initRecycler() {
        optionsAdapter = OptionsRecyclerAdapter()
        binding.apply {
            optionsRecyclerView.adapter = optionsAdapter
            optionsRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            val options = mutableListOf(
                Options.LANGUAGE,
                Options.LOG_OUT,
                Options.CHANGE_PASSWORD,
                Options.DARK_MODE
            )
            optionsAdapter.submitList(options)
        }
    }


}