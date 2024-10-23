package com.eventapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.eventapp.databinding.FragmentHomeBinding
import com.eventapp.ui.ViewModelFactory
import com.eventapp.ui.adapter.ListEventAdapter
import com.eventapp.ui.finished.FinishedViewModel
import com.eventapp.ui.upcoming.UpcomingViewModel
import com.eventapp.utils.Result

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val upcomingViewModel: UpcomingViewModel by viewModels {
        ViewModelFactory.getInstance(
            requireActivity()
        )
    }
    private val finishedViewModel: FinishedViewModel by viewModels {
        ViewModelFactory.getInstance(
            requireActivity()
        )
    }

    private val limit = 5

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            finishedViewModel.getFinishedEvent(limit)
            upcomingViewModel.getUpcomingEvent(limit)
        }

        getFinishedEvent()
        getUpcomingEvent()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnTryAgain.setOnClickListener {
            upcomingViewModel.getUpcomingEvent(limit)
            binding.upcomingErrorPage.visibility = View.GONE
        }
        binding.finishedBtnTryAgain.setOnClickListener {
            finishedViewModel.getFinishedEvent(limit)
            binding.finishedErrorPage.visibility = View.GONE
        }

        return root
    }

    private fun getFinishedEvent() {
        finishedViewModel.listEvents.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.finishedProgressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.finishedProgressBar.visibility = View.GONE
                        val listEventData = result.data
                        binding.rvFinished.layoutManager = LinearLayoutManager(
                            requireActivity(),
                        )
                        val adapter = ListEventAdapter(listEventData)
                        binding.rvFinished.adapter = adapter
                        binding.finishedErrorPage.visibility = View.GONE
                    }

                    is Result.Error -> {
                        binding.finishedProgressBar.visibility = View.GONE
                        binding.finishedErrorPage.visibility =
                            if (result.error.isNotEmpty()) View.VISIBLE else View.GONE
                        binding.errorMessage.text = result.error
                    }
                }

            }
        }


    }

    private fun getUpcomingEvent() {
        upcomingViewModel.listEvents.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.upcomingProgressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.upcomingProgressBar.visibility = View.GONE
                        val listEventData = result.data
                        binding.rvUpcoming.layoutManager = LinearLayoutManager(
                            requireActivity(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        val adapter = ListEventAdapter(listEventData, horizontal = true)
                        binding.rvUpcoming.adapter = adapter
                        binding.upcomingErrorPage.visibility = View.GONE
                    }

                    is Result.Error -> {
                        binding.upcomingProgressBar.visibility = View.GONE
                        binding.upcomingErrorPage.visibility =
                            if (result.error.isNotEmpty()) View.VISIBLE else View.GONE
                        binding.errorMessage.text = result.error
                    }
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}