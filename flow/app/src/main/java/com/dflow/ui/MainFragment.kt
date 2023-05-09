package com.dflow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import coil.transform.RoundedCornersTransformation
import com.dflow.databinding.FragmentMainBinding
import com.dflow.utils.Navigator
import com.dflow.utils.UiResult
import com.dflow.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainFragment : BindingFragment<FragmentMainBinding>() {

    companion object {

        val TAG = this::class.java.name.toString()

        @JvmStatic
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by activityViewModels()

    override fun inflate(inflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean): FragmentMainBinding = FragmentMainBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when(it){
                        is UiResult.Success -> {
                            val url = it.data.message
                            binding.img.load(url) {
                                transformations(RoundedCornersTransformation(16f))
                            }
                        }
                        is UiResult.Error -> {
                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
                        }
                        is UiResult.Loading -> {
                            Toast.makeText(requireContext(), "LOADING", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiStateShared.collect {
                    when(it){
                        is UiResult.Success -> {
                            val url = it.data.message
                            binding.img.load(url) {
                                transformations(RoundedCornersTransformation(16f))
                            }
                        }
                        is UiResult.Error -> {
                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
                        }
                        is UiResult.Loading -> {
                            Toast.makeText(requireContext(), "LOADING", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.res.observe(viewLifecycleOwner) {
            val url = it?.message
            binding.img.load(url) {
                transformations(RoundedCornersTransformation(16f))
            }
        }


        binding.random.setOnClickListener { viewModel.findMultipleRandomItemWithSharedFlow() }
        binding.randomMultiple.setOnClickListener { viewModel.findMultipleRandomItemWithStateFlow() }
        binding.liveData.setOnClickListener { viewModel.findMultipleRandomItem() }
        binding.next.setOnClickListener { Navigator.showFragment(requireActivity().supportFragmentManager, DetailFragment.newInstance(), DetailFragment.TAG, true) }
    }
}