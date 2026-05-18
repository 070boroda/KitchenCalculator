package com.zelianko.kitchencalculator


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.zelianko.kitchencalculator.datastore.AppPreferences
import com.zelianko.kitchencalculator.lottie.LottieLoader
import com.zelianko.kitchencalculator.modelview.ProductViewModel
import com.zelianko.kitchencalculator.navigation.RecipeNavGraph
import com.zelianko.kitchencalculator.timer_screen.TimerController
import com.zelianko.kitchencalculator.ui.theme.KitchenCalculatorTheme
import dagger.hilt.android.AndroidEntryPoint
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /** Решение о Lottie: null пока читаем DataStore, true/false — показывать или нет. */
    private var showLottieDecision by mutableStateOf<Boolean?>(null)

    /** Из intent: открыть TimerScreen сразу после старта (тап по уведомлению). */
    private var pendingOpenTimer by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        // Держим системный splash пока не решим, нужно ли показывать Lottie.
        splash.setKeepOnScreenCondition { showLottieDecision == null }
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        pendingOpenTimer = intent.shouldOpenTimer() || isTimerRunning()

        val productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]

        if (savedInstanceState == null) {
            AppMetrica.reportAppOpen(this)
        }

        // Lottie показываем только если её ещё не показывали сегодня.
        lifecycleScope.launch {
            val alreadyShown = AppPreferences.wasLottieShownToday(this@MainActivity)
            showLottieDecision = !alreadyShown
        }

        setContent {
            KitchenCalculatorTheme {
                val initialShowLoader = showLottieDecision ?: return@KitchenCalculatorTheme
                var showLoader by remember { mutableStateOf(initialShowLoader) }

                // Помечаем дату показа, когда Lottie завершится (или сразу, если её скипнули).
                LaunchedEffect(showLoader) {
                    if (!showLoader) {
                        AppPreferences.markLottieShownToday(this@MainActivity)
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    if (showLoader) {
                        LottieLoader(
                            isPlaying = true,
                            changeIsPlaying = { showLoader = false },
                        )
                    } else {
                        RecipeNavGraph(
                            productViewModel = productViewModel,
                            openTimerOnStart = pendingOpenTimer,
                            onTimerOpened = { pendingOpenTimer = false },
                        )
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        AppMetrica.reportAppOpen(intent)
        if (intent.shouldOpenTimer() || isTimerRunning()) {
            pendingOpenTimer = true
        }
    }

    private fun Intent.shouldOpenTimer(): Boolean =
        getBooleanExtra(EXTRA_OPEN_TIMER, false)

    /** Активен ли таймер прямо сейчас — есть totalMillis и не финиш. */
    private fun isTimerRunning(): Boolean {
        val state = TimerController.state.value
        return state.totalMillis > 0L && !state.isFinished
    }

    companion object {
        const val EXTRA_OPEN_TIMER = "extra_open_timer"
    }
}
