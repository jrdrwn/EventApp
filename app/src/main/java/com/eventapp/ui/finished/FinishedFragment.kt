package com.eventapp.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.eventapp.ListEventAdapter
import com.eventapp.databinding.FragmentFinishedBinding

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null

    private val binding get() = _binding!!

    private val viewModel: FinishedViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.listEvent.observe(viewLifecycleOwner) {

            binding.rvFinished.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = ListEventAdapter(it)
            binding.rvFinished.adapter = adapter
            binding.errorPage.visibility = View.GONE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {

            binding.errorPage.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
            binding.errorMessage.text = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (savedInstanceState == null) {
            viewModel.getEvents()
        }

        binding.btnTryAgain.setOnClickListener {
            viewModel.getEvents()
            binding.errorPage.visibility = View.GONE
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}