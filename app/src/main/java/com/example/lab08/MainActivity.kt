import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.lab08.R
import com.example.lab08.RandomCharacterService

class MainActivity : AppCompatActivity() {
    private lateinit var editText: EditText
    private lateinit var serviceIntent: Intent
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.getCharExtra("randomCharacter", '?')?.let {
                editText.setText(it.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_background)

        editText = findViewById(R.id.editText_random)
        serviceIntent = Intent(this, RandomCharacterService::class.java)

        findViewById<Button>(R.id.btn_start).setOnClickListener {
            startService(serviceIntent)
        }

        findViewById<Button>(R.id.btn_stop).setOnClickListener {
            stopService(serviceIntent)
            editText.text.clear()
        }
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter("RANDOM_CHAR_ACTION")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Для Android 13+ (API 33+)
            registerReceiver(
                receiver,
                intentFilter,
                RECEIVER_NOT_EXPORTED // Приемник доступен только внутри приложения
            )
        } else {
            // Для старых версий Android
            registerReceiver(receiver, intentFilter)
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }
}