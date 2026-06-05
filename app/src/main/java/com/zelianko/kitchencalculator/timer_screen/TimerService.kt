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
import androidx.glance.appwidget.updateAll
import com.zelianko.kitchencalculator.MainActivity
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.widget.TimerWidget
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
    /**
     * Контекст выбора, для которого запущен таймер. Нужен, чтобы при возврате
     * на экран восстановить те же chips и заблокировать выбор другого продукта,
     * пока таймер не остановлен. Переживает уход с экрана (object живёт в процессе,
     * а foreground-сервис держит процесс живым).
     */
    val groupKey: Int? = null,
    val cookingTypeKey: Int? = null,
    val productId: Long? = null,
)

object TimerController {
    private val _state = MutableStateFlow(TimerState())
    val state: StateFlow<TimerState> = _state.asStateFlow()

    internal fun update(transform: (TimerState) -> TimerState) {
        _state.update(transform)
    }

    fun start(
        context: Context,
        totalMillis: Long,
        groupKey: Int? = null,
        cookingTypeKey: Int? = null,
        productId: Long? = null,
    ) {
        val intent = Intent(context, TimerService::class.java).apply {
            action = TimerService.ACTION_START
            putExtra(TimerService.EXTRA_TOTAL_MILLIS, totalMillis)
            putExtra(TimerService.EXTRA_GROUP_KEY, groupKey ?: Int.MIN_VALUE)
            putExtra(TimerService.EXTRA_COOKING_TYPE_KEY, cookingTypeKey ?: Int.MIN_VALUE)
            putExtra(TimerService.EXTRA_PRODUCT_ID, productId ?: Long.MIN_VALUE)
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
                val groupKey = intent.getIntExtra(EXTRA_GROUP_KEY, Int.MIN_VALUE)
                    .takeIf { it != Int.MIN_VALUE }
                val typeKey = intent.getIntExtra(EXTRA_COOKING_TYPE_KEY, Int.MIN_VALUE)
                    .takeIf { it != Int.MIN_VALUE }
                val productId = intent.getLongExtra(EXTRA_PRODUCT_ID, Long.MIN_VALUE)
                    .takeIf { it != Long.MIN_VALUE }
                startTimer(total, groupKey, typeKey, productId)
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

    private fun startTimer(
        totalMillis: Long,
        groupKey: Int? = null,
        cookingTypeKey: Int? = null,
        productId: Long? = null,
    ) {
        TimerController.update {
            TimerState(
                totalMillis = totalMillis,
                remainingMillis = totalMillis,
                isRunning = true,
                isFinished = false,
                groupKey = groupKey,
                cookingTypeKey = cookingTypeKey,
                productId = productId,
            )
        }
        startInForeground(totalMillis)
        refreshWidget()
        runTicker()
    }

    private fun pauseTimer() {
        tickJob?.cancel()
        TimerController.update { it.copy(isRunning = false) }
        updateNotification(running = false)
        refreshWidget()
    }

    private fun resumeTimer() {
        if (TimerController.state.value.remainingMillis <= 0L) return
        TimerController.update { it.copy(isRunning = true, isFinished = false) }
        updateNotification(running = true)
        refreshWidget()
        runTicker()
    }

    private fun stopTimer() {
        tickJob?.cancel()
        TimerController.update { TimerState() }
        refreshWidget()
    }

    private fun runTicker() {
        tickJob?.cancel()
        tickJob = scope.launch {
            var lastTick = SystemClock.elapsedRealtime()
            var lastSecondShown = TimerController.state.value.remainingMillis / 1000L
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
                // Widget — обновляем не чаще раза в секунду, чтобы не дёргать систему.
                val curSecond = TimerController.state.value.remainingMillis / 1000L
                if (curSecond != lastSecondShown) {
                    lastSecondShown = curSecond
                    refreshWidget()
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
        refreshWidget()
        stopSelfAndForeground()
    }

    private fun refreshWidget() {
        scope.launch {
            runCatching {
                // updateAll вызывает provideGlance для всех экземпляров виджета.
                // Если их не размещали — это no-op.
                TimerWidget().updateAll(this@TimerService)
            }
        }
    }

    private fun startInForeground(totalMillis: Long) {
        ensureChannels()
        val notification = buildOngoingNotification(totalMillis, running = true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            // SPECIAL_USE — без таймаута (в отличие от SHORT_SERVICE, который
            // система убивает через ~3 минуты с ANR). Таймер готовки может идти
            // десятки минут, поэтому нужен длительный тип.
            startForeground(
                NOTIFICATION_ID_ONGOING,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
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

        // Чистим устаревший канал (если пользователь обновился со старой сборки,
        // где у нас не было звука alarm на канале).
        if (nm.getNotificationChannel(OLD_CHANNEL_ID_FINISHED) != null) {
            nm.deleteNotificationChannel(OLD_CHANNEL_ID_FINISHED)
        }

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
            // На Android 8+ звук и вибро задаются на канале, а не на notification.
            // setSound/setDefaults в NotificationCompat.Builder для этого канала
            // игнорируются после его создания — поэтому конфигурируем здесь.
            val audioAttrs = android.media.AudioAttributes.Builder()
                .setUsage(android.media.AudioAttributes.USAGE_ALARM)
                .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            nm.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID_FINISHED,
                    getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_HIGH,
                ).apply {
                    setSound(
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
                        audioAttrs,
                    )
                    enableVibration(true)
                    vibrationPattern = longArrayOf(0, 400, 200, 400, 200, 400)
                    setBypassDnd(false)
                    lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
                }
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
        const val EXTRA_GROUP_KEY = "extra_group_key"
        const val EXTRA_COOKING_TYPE_KEY = "extra_cooking_type_key"
        const val EXTRA_PRODUCT_ID = "extra_product_id"

        private const val CHANNEL_ID_ONGOING = "timer_ongoing"
        // v2 — у v1 был дефолтный звук канала вместо alarm, а изменить параметры
        // существующего канала система не даёт. Бамп id = новый канал с правильной
        // конфигурацией.
        private const val CHANNEL_ID_FINISHED = "timer_finished_v2"
        private const val OLD_CHANNEL_ID_FINISHED = "timer_finished"
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
