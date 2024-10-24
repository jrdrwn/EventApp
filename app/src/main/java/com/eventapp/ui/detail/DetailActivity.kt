package com.eventapp.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.eventapp.R
import com.eventapp.data.local.entity.FavoriteEvent
import com.eventapp.databinding.ActivityDetailBinding
import com.eventapp.ui.ViewModelFactory
import com.eventapp.utils.Result
import com.eventapp.utils.loadImage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: DetailActivityViewModel by viewModels {
            factory
        }
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Detail Event"
        }


        val eventId = intent.getIntExtra(EXTRA_EVENT_ID, -1)

        if (savedInstanceState == null) {
            viewModel.getDetailEvent(eventId)
        }

        viewModel.event.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val eventData = result.data
                        supportActionBar?.title = eventData.name

                        binding.apply {

                            viewModel.getFavoriteEventById(eventData.id)
                                .observe(this@DetailActivity) { favoriteEvent ->
                                    Log.d("DetailActivity", "favoriteEvent: $favoriteEvent")
                                    val favoriteEventData = FavoriteEvent(
                                        eventData.id,
                                        eventData.name,
                                        eventData.mediaCover,
                                        eventData.imageLogo,
                                        eventData.summary
                                    )
                                    if (favoriteEvent != null) {
                                        binding.detailFabFavorite.setImageResource(R.drawable.baseline_favorite_24)
                                        detailFabFavorite.setOnClickListener {
                                            viewModel.delete(favoriteEventData)
                                        }
                                    } else {
                                        binding.detailFabFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                                        detailFabFavorite.setOnClickListener {
                                            viewModel.insert(favoriteEventData)
                                        }
                                    }
                                }
                            detailBg.loadImage(eventData.mediaCover)
                            binding.detailName.text = eventData.name
                            binding.detailOwnerName.text =
                                getString(R.string.penyelenggara, eventData.ownerName)
                            binding.detailTime.text =
                                getString(
                                    R.string.waktu,
                                    convertToHumanReadable(eventData.beginTime)
                                )
                            binding.detailQuota.text =
                                getString(
                                    R.string.sisa_kuota,
                                    String.format(
                                        Locale.getDefault(),
                                        "%d",
                                        eventData.quota - eventData.registrants
                                    )
                                )

                            binding.detailDesc.text = HtmlCompat.fromHtml(
                                eventData.description,
                                HtmlCompat.FROM_HTML_MODE_LEGACY
                            )
                            binding.detailFabFavorite.visibility = View.VISIBLE
                            binding.detailRegister.visibility = View.VISIBLE
                            binding.errorPage.visibility = View.GONE
                        }
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorPage.visibility =
                            if (result.error.isNotEmpty()) View.VISIBLE else View.GONE
                        binding.errorMessage.text = result.error
                    }
                }
            }

        }


        binding.detailRegister.setOnClickListener {
            val url = "https://www.dicoding.com/events/${eventId}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addCategory(Intent.CATEGORY_BROWSABLE)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
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

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }
}