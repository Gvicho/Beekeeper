package com.example.beekeeper.presenter.screen.home

import com.example.beekeeper.databinding.FragmentHomeBinding
import com.example.beekeeper.presenter.adapter.FarmsRecyclerAdapter
import com.example.beekeeper.presenter.adapter.ItemClickCallBack
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.extension.showSnackBar
import com.example.beekeeper.presenter.model.home.FarmUi
import com.example.beekeeper.presenter.model.home.LocationUi
import com.example.beekeeper.presenter.model.home.OwnerUi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    ItemClickCallBack {

    private lateinit var farmsRecyclerAdapter: FarmsRecyclerAdapter
    override fun bind() {
        bindFarmsRecycler()
    }

    private fun bindFarmsRecycler(){
        farmsRecyclerAdapter = FarmsRecyclerAdapter(this)
        binding.farmsRecycler.adapter = farmsRecyclerAdapter
        farmsRecyclerAdapter.submitList(mockFarmList)
    }

    private val mockFarmList = listOf(
        FarmUi(
            id = 0,
            beeHiveNumber = 20,
            farmName = "Choporti Farm",
            location = LocationUi(
                latitude = 41.979269096928455,
                longitude = 44.76308904438155,
                locationName = "Georgia, Choporti, Choporti Reservation N23"
            ),
            owner = OwnerUi(
                id = 123,
                name = "Jemali Kakauridze",
                numberOfFarms = 4
            ),
            images = listOf(
                "https://plus.unsplash.com/premium_photo-1681506409755-889461169989?q=80&w=1000&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                "https://images.unsplash.com/photo-1572731561221-96d988d74dc9?q=80&w=2071&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                "https://images.unsplash.com/photo-1473973266408-ed4e27abdd47?q=80&w=2072&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            )
        ),
        FarmUi(
            id = 1,
            beeHiveNumber = 52,
            farmName = "Tsotskhnara Farm",
            location = LocationUi(
                latitude = 42.048895912574146,
                longitude = 43.582125712799254,
                locationName = "Georgia, Tsotskhnara, Unnamed street"
            ),
            owner = OwnerUi(
                id = 123,
                name = "Jemali Kakauridze",
                numberOfFarms = 4
            ),
            images = listOf(
                "https://images.unsplash.com/photo-1695130059316-a094d8280a4f?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                "https://agenda.ge/files/news/014/beekeping-base.jpg"
            )
        ),
        FarmUi(
            id = 2,
            beeHiveNumber = 23,
            farmName = "Mukhuri Farm",
            location = LocationUi(
                latitude = 42.63557809303544,
                longitude = 42.17077491687702,
                locationName = "Georgia, Samegrelo, Mukhuri"
            ),
            owner = OwnerUi(
                id = 11,
                name = "Pavle Otkhozoria",
                numberOfFarms = 1
            ),
            images = listOf(
                "https://s7d1.scene7.com/is/image/wbcollab/Georgia-Honey-home-1350x675:1400x600?qlt=100&resMode=sharp2",
                "https://honeyofgeorgia.com/assets/images/regions/e55ecba6698851935ebd53a9b2f8ae13.jpg",
                "https://images.unsplash.com/photo-1538451204217-42a17267e3ba?crop=entropy&cs=srgb&fm=jpg&ixid=MnwyMzA4NjJ8MHwxfHNlYXJjaHwxfHxtdWtodXJpLXNhbWVncmVsby16ZW1vLXN2YW5ldGl8ZW58MHwwfHx8MTYyMTkxNjUwNA&ixlib=rb-1.2.1&q=85&w=1170&dpr=1"
            )
        )
    )

    override fun onItemClick(id: Int) {
        binding.root.showSnackBar("Open Details Page")
    }

    override fun onLocationButtonClick(location: LocationUi) {
        binding.root.showSnackBar("Open Location on Map")
    }

}