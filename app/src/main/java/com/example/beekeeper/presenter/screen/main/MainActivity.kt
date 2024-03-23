package com.example.beekeeper.presenter.screen.main

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.beekeeper.presenter.event.main.MainActivityEvents
import com.example.beekeeper.presenter.extension.showSnackBar
import com.example.beekeeper.presenter.model.drawer_menu.Option
import com.example.beekeeper.presenter.model.drawer_menu.Options
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var optionsAdapter: OptionsRecyclerAdapter
    private val viewModel: MainViewModel by viewModels()
    private lateinit var options: MutableList<Option>

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeNavigationEvents()

        handleIntent(intent)
        intent.extras?.clear()
        intent = Intent()

        viewModel.onEvent(MainActivityEvents.ReadDarkMode)
        observeDarkMode()
        requestPermission()


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

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.damagedBeehivesFragment, R.id.navigation_home, R.id.shareOrGetFragment, R.id.savedAnalyticsFragment -> {
                    binding.appBarMain.contentMain.navView.visibility = View.VISIBLE
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }

                else -> {
                    binding.appBarMain.contentMain.navView.visibility = View.GONE
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
            }
        }

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
        intent.extras?.clear()
        setIntent(Intent())
    }

    private fun initRecycler() {
        optionsAdapter = OptionsRecyclerAdapter {
            when (it.type) {
                Options.DARK_MODE -> {
                    val navController = findNavController(R.id.nav_host_fragment_activity_main)
                    navController.navigate(R.id.themesBottomSheetFragment)
                }

                Options.LOG_OUT -> {
                    viewModel.onEvent(MainActivityEvents.LogOutEvent)
                    binding.root.showSnackBar("LogOut")
                    navigateToLoginFragmentClearingBackStack()
                }

                Options.PROFILE -> {
                    val navController = findNavController(R.id.nav_host_fragment_activity_main)
                    navController.navigate(R.id.profileFragment)
                }

                Options.CHANGE_PASSWORD -> {
                    val navController = findNavController(R.id.nav_host_fragment_activity_main)
                    navController.navigate(R.id.changePasswordFragment)
                }

                Options.LANGUAGE -> TODO()
            }

        }
        binding.apply {
            optionsRecyclerView.adapter = optionsAdapter
            optionsRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            options = mutableListOf(
                Option("Language", Options.LANGUAGE, icon = R.drawable.ic_language),
                Option("Themes", Options.DARK_MODE, icon = R.drawable.ic_themes),
                Option("Profile", Options.PROFILE, icon = R.drawable.ic_beekeeper_24),
                Option("Log out", Options.LOG_OUT, icon = R.drawable.ic_log_out),

                )
            optionsAdapter.submitList(options)
        }
    }

    private fun navigateToLoginFragmentClearingBackStack() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val navControllerId = navController.graph.id
        navController.popBackStack(navControllerId, true)
        navController.navigate(R.id.loginFragment)

    }

    private fun observeDarkMode() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.darkModeFlow.collect { isDarkModeEnabled ->
                    applyTheme(isDarkModeEnabled)
                }
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeNavigationEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pageNavigationEvent
                    .debounce(1500)
                    .collect { event ->
                        handleNavigation(event)
                    }
            }
        }
    }

    private fun handleNavigation(event: MainViewModel.MessagePageNavigationEvents?) {
        event?.let {
            when (it) {
                is MainViewModel.MessagePageNavigationEvents.NavigateToReportDetailsPage -> {
                    openReportDetailsFragment(it.reportId)
                    viewModel.onEvent(MainActivityEvents.ResetNavigationToNull)
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


    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermission.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
        }

    }

    private fun handleIntent(intent: Intent) {
        intent.extras?.let { bundle ->
            if (bundle.containsKey("reportId")) {
                val reportId = bundle.getString("reportId")
                viewModel.onEvent(
                    MainActivityEvents.IntentReceivedWithReportId(
                        reportId?.toInt() ?: 1
                    )
                )
            }
        }
    }

    private fun openReportDetailsFragment(reportId: Int) {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val bundle = Bundle().apply {
            putInt("id", reportId) // Make sure the key matches the argument name in your nav graph
        }
        navController.navigate(R.id.damageReportDetailsFragment, bundle)
    }


}