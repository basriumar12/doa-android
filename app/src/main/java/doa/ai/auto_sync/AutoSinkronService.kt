package doa.ai.auto_sync

import android.app.Service
import android.content.Intent
import android.os.IBinder

class AutoSinkronService : Service() {
    internal var stopped: Boolean = false

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        //        AutoSinkron.getInstance(getApplicationContext()).subscribe();
        stopped = false
        return Service.START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}