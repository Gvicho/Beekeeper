package com.example.beekeeper.presenter.screen.themes

import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.beekeeper.R
import com.example.beekeeper.databinding.FragmentThemesBottomSheetBinding
import com.example.beekeeper.presenter.base_fragment.BaseBottomSheetFragment
import com.google.android.datatransport.runtime.logging.Logging.d
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class ThemesBottomSheetFragment :
    BaseBottomSheetFragment<FragmentThemesBottomSheetBinding>(FragmentThemesBottomSheetBinding::inflate) {

        private val viewModel: ThemesViewModel by viewModels()
        private var isDarkThemeEnabled = false


    override fun setUp() {
        viewModel.readDarkMode()



    }

    override fun listeners() {


        binding.cbDarkMode.setOnCheckedChangeListener { _, isChecked ->

            if(isChecked){
                binding.cbLightMode.isChecked = false
                applyTheme(true)
            }

        }

        binding.cbLightMode.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                binding.cbDarkMode.isChecked = false
                applyTheme(false)
            }

        }

        binding.btnSave.setOnClickListener {
            viewModel.writeDarkMode(isDarkThemeEnabled)
            dismiss()
        }
    }


    private fun applyTheme(isDarkModeEnabled: Boolean) {
        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }


    }

    private fun setUpPreview(isDarkModeEnabled: Boolean){

        if (isDarkModeEnabled){
            binding.cbDarkMode.isChecked = true
            binding.ivThemePreview.setImageResource(R.drawable.screen_dark_mode)
        }else{
            binding.cbLightMode.isChecked = true
            binding.ivThemePreview.setImageResource(R.drawable.screen_light_mode)
        }

    }


    override fun bindObserves() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.darkModeFlow.collect { isDarkModeEnabled ->
                        d("observed", "fdsafads")
                    setUpPreview(isDarkModeEnabled)
                    isDarkThemeEnabled = isDarkModeEnabled
                    d("observed", "fdsafads")
                }
            }
        }
    }




}