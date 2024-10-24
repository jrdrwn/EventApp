package com.eventapp.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.eventapp.R
import com.eventapp.data.remote.response.EventResponse
import com.eventapp.data.remote.retrofit.ApiConfig
import com.eventapp.utils.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DailyReminderWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    companion object {
        private val TAG = DailyReminderWorker::class.java.simpleName
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "dicoding channel"
    }

    private var resultStatus: Result? = null

    override fun doWork(): Result {
        return getUpcomingEvent()
    }

    private fun getUpcomingEvent(): Result {
        Log.d(TAG, "getUpcomingEvent: Start.....")
        Looper.prepare()
        val apiService = ApiConfig.getApiService()
        val client = apiService.getOneUpcomingEvent()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                if (response.isSuccessful) {
                    val listEvents = response.body()?.listEvents
                    val title = listEvents?.get(0)?.name
                    val time = listEvents?.get(0)?.beginTime
                    showNotification(title, time)
                    Log.d(TAG, "onSuccess: Selesai.....")
                    resultStatus = Result.success()
                } else {
                    showNotification("Event sedang tidak ada", response.message())
                    resultStatus = Result.failure()
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                showNotification("Gagal mendapatkan event terdekat", t.message.toString())
                Log.d(TAG, "onSuccess: Gagal.....")
                resultStatus = Result.failure()
            }
        })
        return resultStatus ?: Result.failure()
    }

    private fun showNotification(title: String?, description: String?) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_upcoming_24)
                .setSubText("Event yang akan datang")
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notification.setChannelId(CHANNEL_ID)
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }

}