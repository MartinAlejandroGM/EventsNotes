package com.andro_sk.eventnotes.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.andro_sk.eventnotes.data.local.worker.EventWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class WorkerViewModel @Inject constructor(
    private val workManager: WorkManager
) : ViewModel() {

    fun startMyWorker() {
        val workRequest = OneTimeWorkRequest.Builder(EventWorker::class.java)
            .build()

        val periodicWorkRequest = PeriodicWorkRequest.Builder(EventWorker::class.java, 1, TimeUnit.MINUTES).build()
        workManager.enqueue(listOf(
            workRequest,
            periodicWorkRequest
        ))
    }
}