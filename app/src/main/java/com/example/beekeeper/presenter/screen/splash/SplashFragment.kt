package com.example.beekeeper.presenter.screen.splash

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.beekeeper.R
import com.example.beekeeper.databinding.FragmentSplashScreenBinding
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.extension.safeNavigateWithArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashScreenBinding>(FragmentSplashScreenBinding::inflate) {

    private val viewModel: SplashViewModel by viewModels()


    override fun loadData() {
        viewModel.navigateToNextScreen()
    }

    override fun bindObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.navigationEvent.collect{ splashNavigationEvent->
                    handleNavigationEvent(splashNavigationEvent)
                }
            }
        }
    }

    private fun handleNavigationEvent(event:SplashViewModel.SplashNavigationEvent){
        when(event){
            is SplashViewModel.SplashNavigationEvent.NavigateToHome ->{
                navigateToHomePage()
            }
            is SplashViewModel.SplashNavigationEvent.NavigateToLogin ->{
                navigateToLoginPage()
            }
        }
    }

    private fun navigateToHomePage(){
        findNavController().safeNavigateWithArgs(R.id.action_splashFragment_to_navigation_home2)
    }

    private fun navigateToLoginPage(){
        findNavController().safeNavigateWithArgs(R.id.action_splashFragment_to_loginFragment)
    }

}