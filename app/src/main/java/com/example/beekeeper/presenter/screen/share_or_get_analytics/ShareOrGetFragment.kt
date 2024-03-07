package com.example.beekeeper.presenter.screen.share_or_get_analytics

import androidx.navigation.fragment.findNavController
import com.example.beekeeper.R
import com.example.beekeeper.databinding.FragmentShareOrGetBinding
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShareOrGetFragment : BaseFragment<FragmentShareOrGetBinding>(FragmentShareOrGetBinding::inflate) {

    override fun bind() {
        binding.scanBtn.setOnClickListener{
            findNavController().navigate(R.id.action_shareOrGetFragment_to_scanBottomSheet)
        }
    }

}