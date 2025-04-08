package app.tabletracker.feature_companion.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class ClientService : Service() {
    enum class Action {
        Connect,
        Disconnect,
        SendData,
        ReceiveData
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}