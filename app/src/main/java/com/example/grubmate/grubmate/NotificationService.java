package com.example.grubmate.grubmate;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;

public class NotificationService extends Service {
    private NotificationBinder mBinder = new NotificationBinder();
    class NotificationBinder extends Binder {
        public void startPolling() {
            Log.d("NotificationService", "polling started");
        }
    }

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("NotificationService", "onCreateExecuted");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO: send a polling request for data
        Log.d("NotificationService", "onStartExecuted");
        Intent local = new Intent();
        local.setAction(MainActivity.BROADCAST_ACTION);
        this.sendBroadcast(local);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("NotificationService", "onDestroyExecuted");
    }
}
