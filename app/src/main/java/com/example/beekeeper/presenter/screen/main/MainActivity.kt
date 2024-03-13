package com.example.beekeeper.presenter.screen.main

import android.os.Bundle
import android.util.Log.d
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beekeeper.R
import com.example.beekeeper.databinding.ActivityMainBinding
import com.example.beekeeper.presenter.adapter.options.OptionsRecyclerAdapter
import com.example.beekeeper.presenter.model.Option
import com.example.beekeeper.presenter.model.drawer_menu.Options
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var optionsAdapter: OptionsRecyclerAdapter
    private val viewModel: MainViewModel by viewModels()
    private lateinit var options: MutableList<Option>

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
        optionsAdapter = OptionsRecyclerAdapter{
            viewModel.writeDarkMode(it)
            viewModel.readDarkMode()
            observeDarkMode()
            applyTheme(it)
        }
        binding.apply {
            optionsRecyclerView.adapter = optionsAdapter
            optionsRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                options = mutableListOf(
                Option(Options.LANGUAGE),
                Option(Options.LOG_OUT),
                Option(Options.CHANGE_PASSWORD),
                Option(Options.DARK_MODE),
            )
            optionsAdapter.submitList(options)
        }
    }
    private fun observeDarkMode() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.darkModeFlow.collect { isDarkModeEnabled ->
                    options[3].status =  isDarkModeEnabled
                    d("ObservingDark", options[3].toString())
                    optionsAdapter.submitList(options)
                }
            }
        }
    }


    private fun applyTheme(isDarkModeEnabled: Boolean) {
        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }


    }




}