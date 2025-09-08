package com.andro_sk.eventnotes.data.local.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.andro_sk.eventnotes.R
import com.andro_sk.eventnotes.domain.contracts.EventsRepository
import com.andro_sk.eventnotes.domain.models.EventModel
import com.andro_sk.eventnotes.domain.models.Response
import com.andro_sk.eventnotes.ui.extension.getParsedDateOrDefaultDate
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.Period
import kotlin.collections.filter

@HiltWorker
class EventWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    @Assisted private val eventsRepository: EventsRepository
): CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val result = eventsRepository.fetchEvents()
                when (result) {
                    is Response.Success -> {
                        val notifyEvents = result.data.filter {
                            val localDateNow = LocalDate.now()
                            val daysBetween = Period.between(localDateNow, it.date.getParsedDateOrDefaultDate()).days
                            daysBetween <= 3 && daysBetween > 0
                        }
                        createNotificationChannel(applicationContext)
                        notifyEvents.forEach {
                            createEventNotification(it)
                            println("EventWorker: ${it.eventTittle}")
                        }
                    }
                    is Response.Error -> {
                        Result.failure()
                    }
                }
                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }

    private fun createEventNotification(eventModel: EventModel){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val notification = NotificationCompat.Builder(
                    applicationContext, "my_worker_notification_channel_id"
                )
                    .setSmallIcon(R.drawable.gardevoir_cicle) // Cambia esto por tu icono
                    .setContentTitle("Se acerca este evento!!")
                    .setContentText(eventModel.eventTittle)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .build()

                val notificationManager = NotificationManagerCompat.from(applicationContext)
                notificationManager.notify(eventModel.hashCode(), notification)
            }
        }
    }

    private fun createNotificationChannel(context: Context) {
        val channelId = "my_worker_notification_channel_id"
        val channelName = "My Worker Notifications"
        val channelDescription = "Notifications for my awesome worker."
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}