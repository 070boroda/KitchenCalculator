package com.zelianko.kitchencalculator.subactivity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.Ringtone
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.timer_screen.TimerController
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun Timer(
    totalTime: Long,
    handleColor: Color,
    inactiveBarColor: Color,
    activeBarColor: Color,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 14.dp,
    ringtone: Ringtone,
    context: Context,
    groupKey: Int? = null,
    cookingTypeKey: Int? = null,
    productId: Long? = null,
) {
    val state by TimerController.state.collectAsStateWithLifecycle()
    val appContext = LocalContext.current.applicationContext

    // Если сервис уже что-то отсчитывает (даже от другого продукта) — отображаем
    // его состояние, иначе показываем "готов к старту" с переданным totalTime.
    val serviceActive = state.totalMillis > 0L && !state.isFinished
    val displayTotal = if (serviceActive) state.totalMillis else totalTime
    val currentTime = if (serviceActive) state.remainingMillis else totalTime
    val isTimerRunning = serviceActive && state.isRunning
    val isFinished = state.isFinished && state.totalMillis == totalTime
    val progress = if (displayTotal > 0L) currentTime / displayTotal.toFloat() else 1f

    // Останавливаем системный рингтон, когда сервис уже отыграл свой звук —
    // экранный рингтон оставлен для совместимости со старым UX, если экран открыт.
    LaunchedEffect(isFinished) {
        if (isFinished) ringtone.play()
    }

    DisposableEffect(Unit) {
        onDispose { ringtone.stop() }
    }

    val postNotificationsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            TimerController.start(appContext, totalTime, groupKey, cookingTypeKey, productId)
        }
    }

    var size by remember { mutableStateOf(IntSize.Zero) }
    val hour = 3600L * 1000L
    val minut = 60L * 1000L

    // Брендовый градиент активной дуги: основной тон → его осветлённая версия.
    val activeBrush = remember(activeBarColor) {
        Brush.sweepGradient(
            listOf(
                activeBarColor,
                lerp(activeBarColor, Color.White, 0.45f),
                activeBarColor,
            )
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.onSizeChanged { size = it }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val arcSize = Size(size.width.toFloat(), size.height.toFloat())
            // Фоновая (неактивная) дорожка.
            drawArc(
                color = inactiveBarColor,
                startAngle = -215f,
                sweepAngle = 250f,
                useCenter = false,
                size = arcSize,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            // Активная дуга с градиентом.
            drawArc(
                brush = activeBrush,
                startAngle = -215f,
                sweepAngle = 250f * progress,
                useCenter = false,
                size = arcSize,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            val center = Offset(size.width / 2f, size.height / 2f)
            val beta = (250f * progress + 145f) * (PI / 180f).toFloat()
            val r = size.width / 2f
            val a = cos(beta) * r
            val b = sin(beta) * r
            val handleCenter = Offset(center.x + a, center.y + b)
            val handleRadius = strokeWidth.toPx() * 1.1f
            // Белая «обводка» бегунка + цветная сердцевина — выглядит объёмно.
            drawCircle(color = Color.White, radius = handleRadius, center = handleCenter)
            drawCircle(color = handleColor, radius = handleRadius * 0.6f, center = handleCenter)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(id = R.string.cooking_time),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = String.format(
                    "%02d:%02d:%02d",
                    (currentTime / hour),
                    (currentTime % hour) / minut,
                    (currentTime % minut) / 1000L
                ),
                fontSize = 46.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
      }

        Spacer(modifier = Modifier.height(20.dp))

        // Кнопки управления — под кругом, чтобы не перекрывать бегунок.
        // Равные по ширине через weight → адаптивны под любой экран.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                onClick = {
                    ringtone.stop()
                    when {
                        // Финиш — стартуем новый таймер с текущим выбранным временем.
                        isFinished -> startWithPermission(
                            appContext, totalTime, groupKey, cookingTypeKey, productId,
                            postNotificationsLauncher
                        )
                        // Сервис активен и идёт — ставим на паузу.
                        isTimerRunning -> TimerController.pause(appContext)
                        // Сервис активен и на паузе — возобновляем.
                        serviceActive -> TimerController.resume(appContext)
                        // Никакого активного таймера — запускаем с totalTime.
                        else -> startWithPermission(
                            appContext, totalTime, groupKey, cookingTypeKey, productId,
                            postNotificationsLauncher
                        )
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 52.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isTimerRunning) {
                        Color(0xFFE53935)
                    } else {
                        activeBarColor
                    },
                    contentColor = Color.White,
                )
            ) {
                Text(
                    text = when {
                        isTimerRunning -> stringResource(id = R.string.stop)
                        isFinished -> stringResource(id = R.string.restart)
                        serviceActive -> stringResource(id = R.string.resume)
                        else -> stringResource(id = R.string.start)
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    softWrap = false,
                )
            }

            // Кнопка полного сброса — видна, пока таймер активен (идёт или на паузе),
            // чтобы не ждать долгий отсчёт до конца.
            if (serviceActive) {
                OutlinedButton(
                    onClick = {
                        ringtone.stop()
                        TimerController.stop(appContext)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 52.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                ) {
                    Text(
                        text = stringResource(id = R.string.reset),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        softWrap = false,
                    )
                }
            }
        }
    }
}

private fun startWithPermission(
    context: Context,
    totalTime: Long,
    groupKey: Int?,
    cookingTypeKey: Int?,
    productId: Long?,
    launcher: androidx.activity.result.ActivityResultLauncher<String>,
) {
    val needsPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    if (needsPermission) {
        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        TimerController.start(context, totalTime, groupKey, cookingTypeKey, productId)
    }
}
