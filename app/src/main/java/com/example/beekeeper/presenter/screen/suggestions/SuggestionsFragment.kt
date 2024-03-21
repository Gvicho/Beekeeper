package com.example.beekeeper.presenter.screen.suggestions

import android.util.Log.d
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.BackoffPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.beekeeper.UploadDataWorkManager
import com.example.beekeeper.databinding.FragmentSuggestionsBinding
import com.example.beekeeper.domain.model.user.UserData
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SuggestionsFragment :
    BaseFragment<FragmentSuggestionsBinding>(FragmentSuggestionsBinding::inflate) {

    override fun setUp() {

        startWorkManager()

    }

    override fun setListeners() {

    }

    private fun startWorkManager() {
        val user = UserData(
            email = "kotekote@mail.ru",
            name = "kotef",
            lastName = "japarfidze",
            image = null,
            token = "konstantine1123"
        )
        val data =
            workDataOf(
                "email" to user.email,
                "name" to user.name,
                "lastName" to user.lastName,
                "image" to user.image,
                "token" to user.token
            )

        val worker = OneTimeWorkRequestBuilder<UploadDataWorkManager>()
            .setInputData(data)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(requireContext())
            .enqueueUniqueWork("writeUser", ExistingWorkPolicy.KEEP, worker)


    }

    override fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                WorkManager.getInstance(requireContext()).getWorkInfosForUniqueWorkFlow("writeUser")
                    .collect {
                        d("UploadUserStatus", it.toString())
                    }


            }
        }
    }
}
