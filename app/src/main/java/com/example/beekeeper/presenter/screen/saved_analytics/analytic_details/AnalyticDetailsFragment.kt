package com.example.beekeeper.presenter.screen.saved_analytics.analytic_details

import androidx.navigation.fragment.navArgs
import com.example.beekeeper.databinding.FragmentAnalyticDetailsBinding
import com.example.beekeeper.presenter.base_fragment.BaseFragment


class AnalyticDetailsFragment : BaseFragment<FragmentAnalyticDetailsBinding>(FragmentAnalyticDetailsBinding::inflate) {

    private val args: AnalyticDetailsFragmentArgs by navArgs()

    override fun bind() {
        val beehiveId = args.beehiveId
        binding.tvId.text = beehiveId.toString()
    }

}