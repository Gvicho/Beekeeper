package com.example.beekeeper.presenter.screen.damaged_beehives.damage_report_details

import android.graphics.Color
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.ekn.gruzer.gaugelibrary.Range
import com.example.beekeeper.databinding.FragmentDamageReportDetailsBinding
import com.example.beekeeper.presenter.adapter.ImagesPager2Adapter
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.event.damage_beehives.DamageReportDetailsPageEvent
import com.example.beekeeper.presenter.extension.showSnackBar
import com.example.beekeeper.presenter.model.damaged_beehives.DamageReportUI
import com.example.beekeeper.presenter.state.damage_report.details.DamageReportDetailsPageState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.relex.circleindicator.CircleIndicator3

@AndroidEntryPoint
class DamageReportDetailsFragment :
    BaseFragment<FragmentDamageReportDetailsBinding>(FragmentDamageReportDetailsBinding::inflate) {

    private val viewModel: DamageReportDetailsViewModel by viewModels()

    private val args: DamageReportDetailsFragmentArgs by navArgs()

    private lateinit var imagesPager2Adapter: ImagesPager2Adapter
    private lateinit var viewPager2: ViewPager2
    private lateinit var indicator: CircleIndicator3

    override fun setUp() {
        viewModel.onEvent(DamageReportDetailsPageEvent.GetReportDetailsEvent(args.id))
        bindDetailsStateObserver()
        viewPager2 = binding.viewPager2
        indicator = binding.indicator
        setUpDamageIndicator()
    }

    private fun setUpDamageIndicator(){
        val range = Range()
        range.color = Color.parseColor("#00b20b")
        range.from = 0.0
        range.to = 1.0

        val range2 = Range()
        range2.color = Color.parseColor("#E3E500")
        range2.from = 1.01
        range2.to = 3.0

        val range3 = Range()
        range3.color = Color.parseColor("#ce0000")
        range3.from = 3.01
        range3.to = 4.0

        binding.apply {
            //add color ranges to gauge
            halfGauge.addRange(range)
            halfGauge.addRange(range2)
            halfGauge.addRange(range3)

            //set min max and current value
            halfGauge.minValue = 0.0
            halfGauge.maxValue = 4.0
        }
    }


    private fun bindDetailsStateObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.reportFlow.collect {
                    handState(it)
                }
            }
        }
    }

    private fun handState(state: DamageReportDetailsPageState){
        showOrHideProgressBar(state.isLoading)

        state.reportDetails?.let {res->
            bindReportDetails(res)
        }

        state.errorMessage?.let{
            showErrorMessage(it)
        }
    }

    private fun bindReportDetails(report:DamageReportUI){

        imagesPager2Adapter = ImagesPager2Adapter(report.imageUris.map { it.toString() })
        viewPager2.adapter = imagesPager2Adapter
        indicator.setViewPager(viewPager2)
        imagesPager2Adapter.registerAdapterDataObserver(indicator.adapterDataObserver)

        binding.apply {
            tvDescription.text = report.damageDescription
            halfGauge.value = report.damageLevelIndicator.toDouble()
            tvId.text = report.id.toString()
            tvDate.text = report.dateUploaded
        }
    }

    private fun showOrHideProgressBar(isLoading: Boolean) {
        binding.apply {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showErrorMessage(errorMessage: String) {
        binding.root.showSnackBar(errorMessage)
        viewModel.onEvent(DamageReportDetailsPageEvent.ResetErrorMessage)
    }

}