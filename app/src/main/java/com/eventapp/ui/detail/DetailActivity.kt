package com.eventapp.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.eventapp.R
import com.eventapp.databinding.ActivityDetailBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }

    private lateinit var binding: ActivityDetailBinding

    private val viewModel: DetailActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Detail Event"
        }


        val eventId = intent.getIntExtra(EXTRA_EVENT_ID, -1)

        if (savedInstanceState == null) {
            viewModel.getDetailEvent(eventId)
        }


        viewModel.event.observe(this) {
            supportActionBar?.title = it.name

            Glide.with(this)
                .load(it.mediaCover)
                .into(binding.detailBg)


            binding.detailName.text = it.name
            binding.detailOwnerName.text = getString(R.string.penyelenggara, it.ownerName)
            binding.detailTime.text =
                getString(R.string.waktu, convertToHumanReadable(it.beginTime))
            binding.detailQuota.text =
                getString(
                    R.string.sisa_kuota,
                    String.format(Locale.getDefault(), "%d", it.quota - it.registrants)
                )

            binding.detailDesc.text = HtmlCompat.fromHtml(
                it.description,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            binding.detailRegister.visibility = View.VISIBLE
            binding.errorPage.visibility = View.GONE


        }

        viewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        binding.detailRegister.setOnClickListener {
            val url = "https://www.dicoding.com/events/${eventId}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addCategory(Intent.CATEGORY_BROWSABLE)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }


        viewModel.errorMessage.observe(this) {
            binding.errorPage.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
            binding.errorMessage.text = it
        }

        binding.btnTryAgain.setOnClickListener {
            viewModel.getDetailEvent(eventId)
            binding.errorPage.visibility = View.GONE
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()

        return super.onSupportNavigateUp()
    }

    private fun convertToHumanReadable(dateTimeString: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime = LocalDateTime.parse(dateTimeString, formatter)
        val humanReadableFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a")
        return dateTime.format(humanReadableFormatter)
    }
}