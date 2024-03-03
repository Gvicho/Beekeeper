package com.example.beekeeper.presenter.screen.splash

import android.os.Handler
import android.os.Looper
import androidx.navigation.fragment.findNavController
import com.example.beekeeper.R
import com.example.beekeeper.databinding.FragmentSplashScreenBinding
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashScreenBinding>(FragmentSplashScreenBinding::inflate) {

    override fun bind() {
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }, 2000) // Delay for 2 seconds
    }

}