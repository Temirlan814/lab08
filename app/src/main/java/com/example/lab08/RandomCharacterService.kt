import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

class RandomCharacterService : Service() {
    private val scope = CoroutineScope(Dispatchers.Default + Job())
    private var isRunning = false
    private val chars = ('A'..'Z').toList()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isRunning = true
        scope.launch {
            while (isRunning) {
                delay(1000)
                val char = chars.random()
                sendBroadcast(char)
                Log.d("Service", "Generated: $char")
            }
        }
        return START_STICKY
    }

    private fun sendBroadcast(char: Char) {
        Intent().apply {
            action = "RANDOM_CHAR_ACTION"
            putExtra("randomCharacter", char)
            sendBroadcast(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        scope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}