package com.example.beekeeper.presenter.screen.authentication.change_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.beekeeper.R
import com.example.beekeeper.databinding.FragmentChangePasswordBinding
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment :
    BaseFragment<FragmentChangePasswordBinding>(FragmentChangePasswordBinding::inflate) {

         private val viewModel: ChangePasswordViewModel by viewModels()
    override fun setUp() {
        viewModel.changePassword()

    }

}