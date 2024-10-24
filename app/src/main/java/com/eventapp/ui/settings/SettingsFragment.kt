package com.eventapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.eventapp.databinding.FragmentSettingsBinding
import com.eventapp.ui.ViewModelFactory
import com.eventapp.worker.DailyReminderWorker
import java.util.concurrent.TimeUnit

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }
    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchDarkMode.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchDarkMode.isChecked = false
            }
        }


        viewModel.getReminderSettings().observe(viewLifecycleOwner) { isReminderActive ->
            binding.switchDailyReminder.isChecked = isReminderActive
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.switchDarkMode.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }
        binding.switchDailyReminder.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                workManager.pruneWork()
                val state = workManager.getWorkInfosByTag(WORKER_TAG).get()
                if (state.isEmpty()) {
                    startPeriodicTask()
                }
            } else {
                cancelPeriodicTask()
            }
        }

        workManager = WorkManager.getInstance(requireActivity())

        return root
    }

    private fun startPeriodicTask() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        periodicWorkRequest =
            PeriodicWorkRequest.Builder(DailyReminderWorker::class.java, 1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .addTag(WORKER_TAG)
                .build()
        workManager.enqueue(periodicWorkRequest)
        viewModel.saveReminderSetting(true)
    }

    private fun cancelPeriodicTask() {
        workManager.cancelAllWorkByTag(WORKER_TAG)
        viewModel.saveReminderSetting(false)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val WORKER_TAG = "DAILY_REMINDER"
    }
}