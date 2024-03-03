package com.example.beekeeper.presenter.screen.authentification.login

import androidx.navigation.fragment.findNavController
import com.example.beekeeper.R
import com.example.beekeeper.databinding.FragmentLoginScreenBinding
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginScreenBinding>(FragmentLoginScreenBinding::inflate) {

    override fun bind() {
        binding.btnLogin.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_navigation_home)
        }
        binding.btnRegister.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
        binding.btnReset.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }
    }

}