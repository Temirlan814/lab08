package com.example.lab08
// RandomCharacterService.kt
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Random

class RandomCharacterService : Service() {
    private val scope = CoroutineScope(Dispatchers.Default + Job())
    private val localBroadcastManager by lazy { LocalBroadcastManager.getInstance(this) }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        scope.launch {
            while (true) {
                delay(1000)
                val digit = Random().nextInt(10)
                localBroadcastManager.sendBroadcast(
                    Intent("RANDOM_DIGIT_EVENT").apply { putExtra("digit", digit) }
                )
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }
}