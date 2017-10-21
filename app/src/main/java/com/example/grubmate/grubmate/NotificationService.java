package com.example.grubmate.grubmate;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;
import android.widget.Toast;

import com.example.grubmate.grubmate.dataClass.Notification;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.google.gson.Gson;

import java.io.IOException;

public class NotificationService extends Service {
    private NotificationBinder mBinder = new NotificationBinder();
    private Gson gson;
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
        gson = new Gson();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO: send a polling request for data
        Log.d("NotificationService", "onStartExecuted");
//        new NotificationTask().execute(GrubMatePreference.getNotificationURL(PersistantDataManager.getUserID()));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class NotificationTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            if (params.length == 0||params[0].length()==0) {
                return null;
            }

            try {
                return  NetworkUtilities.getLong(GrubMatePreference.getNotificationURL(PersistantDataManager.getUserID()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String postActionResponse) {

//          new NotificationTask().execute(GrubMatePreference.getNotificationURL(PersistantDataManager.getUserID()));
        }
    }
}
