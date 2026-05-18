package com.zelianko.kitchencalculator.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.zelianko.kitchencalculator.MainActivity
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.timer_screen.TimerController
import com.zelianko.kitchencalculator.timer_screen.TimerState

/**
 * App Widget с активным таймером. Контент берётся из [TimerController] —
 * сам Glance composable не подписывается на flow, обновляется по запросу
 * из TimerService (или системой при первом размещении).
 *
 * Состояние читается в [provideGlance] (suspend-контекст), а в @Composable
 * передаётся как параметр — иначе Compose ругается на StateFlow.value в
 * композиции.
 */
class TimerWidget : GlanceAppWidget() {

    override val sizeMode: SizeMode = SizeMode.Single

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val state = TimerController.state.value
        provideContent {
            GlanceTheme {
                Content(context, state)
            }
        }
    }

    @Composable
    private fun Content(context: Context, state: TimerState) {
        val active = state.totalMillis > 0L && !state.isFinished
        val openTimerIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(MainActivity.EXTRA_OPEN_TIMER, true)
        }

        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.primary)
                .cornerRadius(16.dp)
                .padding(12.dp)
                .clickable(actionStartActivity(openTimerIntent)),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = context.getString(R.string.cooking_time),
                    style = TextStyle(
                        color = GlanceTheme.colors.onPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                    ),
                )
                Text(
                    text = if (active) formatRemaining(state.remainingMillis)
                    else context.getString(R.string.widget_no_timer),
                    style = TextStyle(
                        color = GlanceTheme.colors.onPrimary,
                        fontSize = if (active) 28.sp else 14.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                )
                if (active && !state.isRunning) {
                    Text(
                        text = context.getString(R.string.pause),
                        style = TextStyle(
                            color = GlanceTheme.colors.onPrimary,
                            fontSize = 11.sp,
                        ),
                    )
                }
            }
        }
    }

    private fun formatRemaining(millis: Long): String {
        val totalSec = (millis / 1000L).coerceAtLeast(0L)
        val h = totalSec / 3600L
        val m = (totalSec % 3600L) / 60L
        val s = totalSec % 60L
        return if (h > 0) "%02d:%02d:%02d".format(h, m, s)
        else "%02d:%02d".format(m, s)
    }
}
