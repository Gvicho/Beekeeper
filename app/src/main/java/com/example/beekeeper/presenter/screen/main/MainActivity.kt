package com.example.beekeeper.presenter.screen.main

import android.Manifest
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beekeeper.R
import com.example.beekeeper.databinding.ActivityMainBinding
import com.example.beekeeper.presenter.adapter.options.OptionsRecyclerAdapter
import com.example.beekeeper.presenter.event.main.MainActivityEvents
import com.example.beekeeper.presenter.extension.loadImage
import com.example.beekeeper.presenter.model.drawer_menu.Option
import com.example.beekeeper.presenter.model.drawer_menu.Options
import com.example.beekeeper.presenter.model.user.UserDataUI
import com.example.beekeeper.presenter.state.configurations.AppConfigurationsState
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.util.Locale


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
        bindUserProfileInfoObserver()
        listeners()
        handleIntent(intent)
        intent.extras?.clear()
        intent = Intent()

        viewModel.onEvent(MainActivityEvents.ReadDarkMode)
        viewModel.onEvent(MainActivityEvents.ReadLanguageConfiguration)
        requestPermission()
        bindConfigurationFlow()


        val navView: BottomNavigationView = binding.appBarMain.contentMain.navView
        initRecycler()

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.damagedBeehivesFragment, R.id.navigation_home, R.id.shareOrGetFragment, R.id.savedAnalyticsFragment -> {
                    binding.appBarMain.contentMain.coordinator.visibility = View.VISIBLE
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }

                else -> {
                    binding.appBarMain.contentMain.coordinator.visibility = View.GONE
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
            }
        }

    }

    private fun bindUserProfileInfoObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userProfileState.collect { user ->
                    bindUserHeader(user)
                }
            }
        }
    }

    private fun bindUserHeader(userDataUI: UserDataUI) {
        val tvName: TextView =
            binding.drawerNav.getHeaderView(0).findViewById(R.id.tvDrawerUserName)
        val tvMail: TextView = binding.drawerNav.getHeaderView(0).findViewById(R.id.tvHeaderMail)
        val profile: AppCompatImageView =
            binding.drawerNav.getHeaderView(0).findViewById(R.id.imageViewHeaderProfile)
        userDataUI.let {
            tvName.text = it.name
            tvMail.text = it.email

            if (it.image.isNotEmpty()) profile.loadImage(it.image)
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

                Options.LANGUAGE -> {
                    val navController = findNavController(R.id.nav_host_fragment_activity_main)
                    navController.navigate(R.id.languagesBottomSheet)
                }
            }

        }
        binding.apply {
            optionsRecyclerView.adapter = optionsAdapter
            optionsRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            options = getOptions()
            optionsAdapter.submitList(options)
        }
    }

    private fun navigateToLoginFragmentClearingBackStack() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val navControllerId = navController.graph.id
        navController.popBackStack(navControllerId, true)
        navController.navigate(R.id.loginFragment)

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

    private fun openAddReportsFragment() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navController.navigate(R.id.addReportFragment)
    }


    private fun listeners() {
        binding.appBarMain.contentMain.btnAddReport.setOnClickListener {
            openAddReportsFragment()
        }
    }

    private fun bindConfigurationFlow(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.configurationsState.collect { configuration ->
                    handleConfigurationState(configuration)
                    }
            }
        }
    }

    private fun handleConfigurationState(configuration: AppConfigurationsState){

        applyTheme(configuration.theme)
        setLocale(configuration.languages)

    }

    private fun setLocale(language: Boolean) {
        val languageCode =
            if (language) {
                getString(R.string.ka)
            } else {
                getString(R.string.en)
            }
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }

    private fun getOptions() = mutableListOf(
        Option(
            getString(R.string.languages),
            Options.LANGUAGE,
            icon = R.drawable.ic_language
        ),
        Option(getString(R.string.themes), Options.DARK_MODE, icon = R.drawable.ic_themes),
        Option(
            getString(R.string.profile),
            Options.PROFILE,
            icon = R.drawable.ic_beekeper_drawer
        ),
        Option(getString(R.string.log_out), Options.LOG_OUT, icon = R.drawable.ic_log_out),

        )
}