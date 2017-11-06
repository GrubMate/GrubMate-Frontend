package com.example.grubmate.grubmate;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.grubmate.grubmate.dataClass.Notification;
import com.example.grubmate.grubmate.fragments.FeedFragment;
import com.example.grubmate.grubmate.fragments.NotificationCenterFragment;
import com.example.grubmate.grubmate.utilities.GrubMatePreference;
import com.example.grubmate.grubmate.utilities.NetworkUtilities;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;
import com.google.gson.Gson;
import com.squareup.picasso.OkHttpDownloader;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NotificationService extends Service {
    private NotificationBinder mBinder = new NotificationBinder();
    private Gson gson;
    private static OkHttpClient client;
    private NotificationCompat.Builder builder;
    private NotificationManagerCompat notificationManager;
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
        builder = new NotificationCompat.Builder(this);
        notificationManager = NotificationManagerCompat.from(this);
        client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO: send a polling request for data
        Log.d("NotificationService", "onStartExecuted");
       new NotificationTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "Notification");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class NotificationTask extends AsyncTask<String, Integer, String> {
        private Semaphore semaphore;
        private String result;

        @Override
        protected String doInBackground(String... params) {
            semaphore = new Semaphore(0);
            result = null;
                Request request = new Request.Builder()
                        .url(GrubMatePreference.getNotificationURL(PersistantDataManager.getUserID()))
                        .build();
            // asynchrounously send notification request;
            client.newCall(request).enqueue(new NotificationCallBack());
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String postActionResponse) {
            Log.d("Notification", postActionResponse==null?"Null":postActionResponse);
            if(postActionResponse != null && !postActionResponse.contains("Error")) {
                Intent local = new Intent();
                local.setAction(NotificationCenterFragment.BROADCAST_ACTION);
                Log.d("notification", postActionResponse);
                if(isResponseValid(postActionResponse)) {
                    PersistantDataManager.addNotification(gson.fromJson(postActionResponse, Notification.class));
                    local.putExtra("notification", postActionResponse);
                    sendBroadcast(local);
                    Notification notification = gson.fromJson(postActionResponse, Notification.class);
                    sendNotification(notification.title, "You have a new notification");
                }
            }
            new NotificationTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "Notification");;
        }
        class NotificationCallBack implements Callback {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(this.toString(), "Network error");
                semaphore.release();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                result = response.body()==null?null:response.body().string();
                Log.d("Notification", result==null?"null":result);
                semaphore.release();
            }
        }
    }

    private void sendNotification(String title, String text) {
        notificationManager.notify(0x1234, builder.setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .build());
    }

    public boolean isResponseValid(String response) {
        if (response == null) {
            // response should not be false
            return false;
        } else if (response.length() == 0) {
            // response should not be empty
            return false;
        } else if (response.contains("error")) {
            // response should not contain error
            return false;
        }
        return true;
    }
}
