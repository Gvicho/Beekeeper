package com.example.beekeeper.presenter.screen.themes

import android.graphics.Color
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.beekeeper.R
import com.example.beekeeper.databinding.FragmentThemesBottomSheetBinding
import com.example.beekeeper.presenter.base_fragment.BaseBottomSheetFragment
import com.example.beekeeper.presenter.event.themes.ThemeEvents
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ThemesBottomSheetFragment :
    BaseBottomSheetFragment<FragmentThemesBottomSheetBinding>(FragmentThemesBottomSheetBinding::inflate) {

        private val viewModel: ThemesViewModel by viewModels()
        private var isDarkThemeEnabled = false


    override fun setUp() {
        viewModel.onEvent(ThemeEvents.ReadSavedThemeState)
    }

    override fun listeners() {


        binding.cbDarkMode.setOnCheckedChangeListener { _, isChecked ->

            if(isChecked){
                binding.cbLightMode.isChecked = false
                isDarkThemeEnabled = true
                setUpPreview()
            }else{
                binding.cbLightMode.isChecked = true
                isDarkThemeEnabled = false
                setUpPreview()
            }

        }

        binding.cbLightMode.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                binding.cbDarkMode.isChecked = false
                isDarkThemeEnabled = false
                setUpPreview()
            }else{
                binding.cbDarkMode.isChecked = true
                isDarkThemeEnabled = true
                setUpPreview()
            }

        }

        binding.btnSave.setOnClickListener {
            viewModel.onEvent(ThemeEvents.SaveThemeState(isDarkThemeEnabled))
            dismiss()
            applyTheme()
        }
    }


    private fun applyTheme() {
        if (isDarkThemeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun setUpPreview(){

        if (isDarkThemeEnabled){
            binding.cbDarkMode.isChecked = true
            binding.ivThemePreview.setImageResource(R.drawable.screen_dark_mode)
            // Set background to black
            binding.root.setBackgroundColor(Color.GRAY)
        }else{
            binding.cbLightMode.isChecked = true
            binding.ivThemePreview.setImageResource(R.drawable.screen_light_mode)
            // Set background to white
            binding.root.setBackgroundColor(Color.WHITE)
        }

    }


    override fun bindObserves() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.darkModeFlow.collect { isDarkModeEnabledStore ->
                    isDarkThemeEnabled = isDarkModeEnabledStore
                    setCurrentState()  // this will be collected only once
                }
            }
        }
    }


    private fun setCurrentState(){
        if(isDarkThemeEnabled){
            binding.cbLightMode.isChecked = false
            binding.cbDarkMode.isChecked = true
        }else{
            binding.cbLightMode.isChecked = true
            binding.cbDarkMode.isChecked = false
        }
        setUpPreview()
    }


}