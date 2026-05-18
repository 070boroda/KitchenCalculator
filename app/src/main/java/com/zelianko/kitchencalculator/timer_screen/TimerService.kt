package com.zelianko.kitchencalculator.timer_screen

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.zelianko.kitchencalculator.MainActivity
import com.zelianko.kitchencalculator.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TimerState(
    val totalMillis: Long = 0L,
    val remainingMillis: Long = 0L,
    val isRunning: Boolean = false,
    val isFinished: Boolean = false,
)

object TimerController {
    private val _state = MutableStateFlow(TimerState())
    val state: StateFlow<TimerState> = _state.asStateFlow()

    internal fun update(transform: (TimerState) -> TimerState) {
        _state.update(transform)
    }

    fun start(context: Context, totalMillis: Long) {
        val intent = Intent(context, TimerService::class.java).apply {
            action = TimerService.ACTION_START
            putExtra(TimerService.EXTRA_TOTAL_MILLIS, totalMillis)
        }
        ContextCompat.startForegroundService(context, intent)
    }

    fun pause(context: Context) = sendCommand(context, TimerService.ACTION_PAUSE)
    fun resume(context: Context) = sendCommand(context, TimerService.ACTION_RESUME)
    fun stop(context: Context) = sendCommand(context, TimerService.ACTION_STOP)

    private fun sendCommand(context: Context, action: String) {
        val intent = Intent(context, TimerService::class.java).apply { this.action = action }
        context.startService(intent)
    }
}

class TimerService : Service() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var tickJob: Job? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val total = intent.getLongExtra(EXTRA_TOTAL_MILLIS, 0L)
                startTimer(total)
            }
            ACTION_PAUSE -> pauseTimer()
            ACTION_RESUME -> resumeTimer()
            ACTION_STOP -> {
                stopTimer()
                stopSelfAndForeground()
            }
        }
        return START_STICKY
    }

    private fun startTimer(totalMillis: Long) {
        TimerController.update {
            TimerState(
                totalMillis = totalMillis,
                remainingMillis = totalMillis,
                isRunning = true,
                isFinished = false,
            )
        }
        startInForeground(totalMillis)
        runTicker()
    }

    private fun pauseTimer() {
        tickJob?.cancel()
        TimerController.update { it.copy(isRunning = false) }
        updateNotification(running = false)
    }

    private fun resumeTimer() {
        if (TimerController.state.value.remainingMillis <= 0L) return
        TimerController.update { it.copy(isRunning = true, isFinished = false) }
        updateNotification(running = true)
        runTicker()
    }

    private fun stopTimer() {
        tickJob?.cancel()
        TimerController.update { TimerState() }
    }

    private fun runTicker() {
        tickJob?.cancel()
        tickJob = scope.launch {
            var lastTick = SystemClock.elapsedRealtime()
            while (TimerController.state.value.remainingMillis > 0L &&
                TimerController.state.value.isRunning
            ) {
                delay(TICK_INTERVAL_MS)
                val now = SystemClock.elapsedRealtime()
                val elapsed = now - lastTick
                lastTick = now
                TimerController.update {
                    it.copy(remainingMillis = (it.remainingMillis - elapsed).coerceAtLeast(0L))
                }
            }
            if (TimerController.state.value.remainingMillis <= 0L &&
                TimerController.state.value.totalMillis > 0L
            ) {
                onFinished()
            }
        }
    }

    private fun onFinished() {
        TimerController.update {
            it.copy(isRunning = false, isFinished = true, remainingMillis = 0L)
        }
        postFinishedNotification()
        stopSelfAndForeground()
    }

    private fun startInForeground(totalMillis: Long) {
        ensureChannels()
        val notification = buildOngoingNotification(totalMillis, running = true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                NOTIFICATION_ID_ONGOING,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SHORT_SERVICE
            )
        } else {
            startForeground(NOTIFICATION_ID_ONGOING, notification)
        }
    }

    private fun updateNotification(running: Boolean) {
        val total = TimerController.state.value.totalMillis
        val notification = buildOngoingNotification(total, running)
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(NOTIFICATION_ID_ONGOING, notification)
    }

    private fun buildOngoingNotification(totalMillis: Long, running: Boolean): android.app.Notification {
        val remaining = TimerController.state.value.remainingMillis
        val finishAt = System.currentTimeMillis() + remaining

        val openTimerIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(MainActivity.EXTRA_OPEN_TIMER, true)
        }
        val contentIntent = PendingIntent.getActivity(
            this, REQ_OPEN_TIMER,
            openTimerIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID_ONGOING)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.cooking_time))
            .setContentIntent(contentIntent)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        if (running && remaining > 0L) {
            builder.setWhen(finishAt)
                .setUsesChronometer(true)
                .setChronometerCountDown(true)
                .setShowWhen(true)
        } else {
            builder.setUsesChronometer(false)
                .setShowWhen(false)
                .setContentText(formatTime(remaining))
        }

        val pauseResumeAction = if (running) {
            NotificationCompat.Action(
                0,
                getString(R.string.stop),
                pendingService(ACTION_PAUSE, REQ_PAUSE),
            )
        } else {
            NotificationCompat.Action(
                0,
                getString(R.string.start),
                pendingService(ACTION_RESUME, REQ_RESUME),
            )
        }
        builder.addAction(pauseResumeAction)
        builder.addAction(
            NotificationCompat.Action(
                0,
                getString(R.string.cancel),
                pendingService(ACTION_STOP, REQ_STOP),
            )
        )

        return builder.build()
    }

    private fun postFinishedNotification() {
        ensureChannels()
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val openTimerIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(MainActivity.EXTRA_OPEN_TIMER, true)
        }
        val contentIntent = PendingIntent.getActivity(
            this, REQ_OPEN,
            openTimerIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID_FINISHED)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.cooking_time))
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
            .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
            .build()

        nm.notify(NOTIFICATION_ID_FINISHED, notification)
    }

    private fun pendingService(action: String, reqCode: Int): PendingIntent {
        val intent = Intent(this, TimerService::class.java).apply { this.action = action }
        return PendingIntent.getService(
            this, reqCode, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun ensureChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (nm.getNotificationChannel(CHANNEL_ID_ONGOING) == null) {
            nm.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID_ONGOING,
                    getString(R.string.cooking_time),
                    NotificationManager.IMPORTANCE_LOW,
                ).apply { setSound(null, null) }
            )
        }
        if (nm.getNotificationChannel(CHANNEL_ID_FINISHED) == null) {
            nm.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID_FINISHED,
                    getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_HIGH,
                )
            )
        }
    }

    private fun stopSelfAndForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    companion object {
        const val ACTION_START = "com.zelianko.kitchencalculator.timer.START"
        const val ACTION_PAUSE = "com.zelianko.kitchencalculator.timer.PAUSE"
        const val ACTION_RESUME = "com.zelianko.kitchencalculator.timer.RESUME"
        const val ACTION_STOP = "com.zelianko.kitchencalculator.timer.STOP"
        const val EXTRA_TOTAL_MILLIS = "extra_total_millis"

        private const val CHANNEL_ID_ONGOING = "timer_ongoing"
        private const val CHANNEL_ID_FINISHED = "timer_finished"
        private const val NOTIFICATION_ID_ONGOING = 1001
        private const val NOTIFICATION_ID_FINISHED = 1002

        private const val REQ_PAUSE = 11
        private const val REQ_RESUME = 12
        private const val REQ_STOP = 13
        private const val REQ_OPEN = 14
        private const val REQ_OPEN_TIMER = 15

        private const val TICK_INTERVAL_MS = 200L

        private fun formatTime(millis: Long): String {
            val totalSec = millis / 1000L
            val h = totalSec / 3600L
            val m = (totalSec % 3600L) / 60L
            val s = totalSec % 60L
            return String.format("%02d:%02d:%02d", h, m, s)
        }
    }
}
