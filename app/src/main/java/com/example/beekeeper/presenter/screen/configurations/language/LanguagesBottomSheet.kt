package com.example.beekeeper.presenter.screen.configurations.language

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.beekeeper.R
import com.example.beekeeper.databinding.BottomSheetLanguagesBinding
import com.example.beekeeper.presenter.base_fragment.BaseBottomSheetFragment
import com.example.beekeeper.presenter.event.configutrations.language.LanguageBottomSheetEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LanguagesBottomSheet: BaseBottomSheetFragment<BottomSheetLanguagesBinding>(BottomSheetLanguagesBinding::inflate) {

    private val viewModel:LanguagesViewModel by viewModels()

    override fun listeners() {
        setLanguagesOnListeners()
    }

    private fun setLanguagesOnListeners(){
        binding.apply {

            tvGeo.setOnClickListener {
                handleLanguageState(true)
                viewModel.onEvent(LanguageBottomSheetEvent.SaveLanguageMode(true))
            }


            tvEng.setOnClickListener {
                handleLanguageState(false)
                viewModel.onEvent(LanguageBottomSheetEvent.SaveLanguageMode(false))
            }

        }
    }

    override fun bindObserves() {
        bindLanguageStateObserver()
        bindNavigationObserver()
    }

    private fun bindLanguageStateObserver(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.languageState.collect{
                    handleLanguageState(it)
                }
            }
        }
    }

    private fun bindNavigationObserver(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.navigationFlow.collect{
                    handleNavigation(it)
                }
            }
        }
    }

    private fun handleLanguageState(language:Boolean){
        binding.apply {
            if(language){
                tvGeo.setBackgroundColor(requireContext().getColor(R.color.grey_transparent))
                tvEng.setBackgroundColor(requireContext().getColor(R.color.transparent))
            }else{
                tvGeo.setBackgroundColor(requireContext().getColor(R.color.transparent))
                tvEng.setBackgroundColor(requireContext().getColor(R.color.grey_transparent))
            }
        }
    }

    private fun handleNavigation(event: LanguagesViewModel.LanguageNavigationEvent){
        when(event){
            LanguagesViewModel.LanguageNavigationEvent.CloseBottomSheet -> closeLanguagesBottomSheet()
        }
    }

    private fun closeLanguagesBottomSheet(){
        dismiss()
    }

}