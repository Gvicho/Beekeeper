package com.example.beekeeper.presenter.screen.main

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Log.d
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
import com.example.beekeeper.presenter.model.Option
import com.example.beekeeper.presenter.model.drawer_menu.Options
import com.example.beekeeper.presenter.screen.themes.ThemesBottomSheetFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var optionsAdapter: OptionsRecyclerAdapter
    private val viewModel: MainViewModel by viewModels()
    private lateinit var options: MutableList<Option>

    private val  requestPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){}



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.readDarkMode()
        observeDarkMode()
        readPushToken()
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

        navController.addOnDestinationChangedListener { controller, destination, _ ->
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

    private fun initRecycler() {
        optionsAdapter = OptionsRecyclerAdapter{
            if(it.type == Options.DARK_MODE){
                val bottomSheet = ThemesBottomSheetFragment()
                bottomSheet.show(supportFragmentManager, bottomSheet.tag)
            }

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
                   applyTheme(isDarkModeEnabled)
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



    private fun readPushToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                d( "Fetching FCM registration token failed", task.exception.toString())
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result


            // Log and toast
            d("firebaseToken", "${token}")

        })
    }


    private fun requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermission.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
        }

    }








}