package com.example.beekeeper.presenter.screen.saved_analytics


import androidx.fragment.app.viewModels
import com.example.beekeeper.databinding.FragmentSavedAnalyticsBinding
import com.example.beekeeper.presenter.adapter.saved_analytics.SavedAnalyticsRecyclerAdapter
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedAnalyticsFragment : BaseFragment<FragmentSavedAnalyticsBinding>(FragmentSavedAnalyticsBinding::inflate) {

    private val viewModel: SavedAnalyticsViewModel by viewModels()
    private lateinit var analyticsRecyclerAdapter: SavedAnalyticsRecyclerAdapter

}