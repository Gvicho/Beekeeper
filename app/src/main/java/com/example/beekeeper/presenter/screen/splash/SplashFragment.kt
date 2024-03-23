package com.example.beekeeper.presenter.screen.splash

import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.beekeeper.R
import com.example.beekeeper.databinding.FragmentSplashScreenBinding
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.event.splash.SplashEvent
import com.example.beekeeper.presenter.extension.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment :
    BaseFragment<FragmentSplashScreenBinding>(FragmentSplashScreenBinding::inflate) {

    private val viewModel: SplashViewModel by viewModels()


    override fun setUp() {
        viewModel.onEvent(SplashEvent.ReadDarkMode)
        observeDarkMode()
    }

    override fun loadData() {
        viewModel.onEvent(SplashEvent.NavigateToNextScreen)
    }

    override fun bind() {
        animation()
    }

    override fun bindObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationEvent.collect { splashNavigationEvent ->
                    handleNavigationEvent(splashNavigationEvent)
                }
            }
        }
    }

    private fun handleNavigationEvent(event: SplashViewModel.SplashNavigationEvent) {
        when (event) {
            is SplashViewModel.SplashNavigationEvent.NavigateToHome -> {
                navigateToHomePage()
            }

            is SplashViewModel.SplashNavigationEvent.NavigateToLogin -> {
                navigateToLoginPage()
            }
        }
    }

    private fun navigateToHomePage() {
        findNavController().safeNavigate(R.id.action_splashFragment_to_navigation_home2)
    }

    private fun navigateToLoginPage() {
        findNavController().safeNavigate(R.id.action_splashFragment_to_loginFragment)
    }

    private fun animation() {
        val slideDownAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_down)
        val slideInBottom = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_bottom)
        binding.logo.startAnimation(slideDownAnimation)
        binding.splashLable.startAnimation(slideInBottom)
    }

    private fun observeDarkMode() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.darkModeFlow.collect { status ->
                    applyTheme(status)
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