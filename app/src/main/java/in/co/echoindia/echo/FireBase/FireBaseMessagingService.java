package in.co.echoindia.echo.FireBase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import in.co.echoindia.echo.R;
import in.co.echoindia.echo.User.SplashActivity;
import in.co.echoindia.echo.Utils.AppUtil;

/**
 * Created by Danish Rafique on 27-04-2017.
 */

public class FireBaseMessagingService extends FirebaseMessagingService {
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    Intent intent;
    String notificationId;
    String title="",message="";
    private static final String LOG_TAG = "FireBaseMessaging";
    InputStream in;
    Bitmap myBitmap;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        /**
         * 1. News Update
         * 2. Poll Update
         * 3. Representative Update
         * 4. Post Share
         * 5. Post Comment
         */

        sharedpreferences = AppUtil.getAppPreferences(this);
        editor = sharedpreferences.edit();
        Log.e(LOG_TAG, "FireBase Called");
        Log.e(LOG_TAG, remoteMessage.getData().toString());
        notificationId = remoteMessage.getData().get("id");
        if (notificationId.equals("4")) {
            message = remoteMessage.getData().get("body");
            title = remoteMessage.getData().get("title");
            URL url = null;
            try {
                url = new URL(remoteMessage.getData().get("image"));
                Log.e(LOG_TAG,"Image Link"+remoteMessage.getData().get("image"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            in = connection.getInputStream();
            myBitmap = BitmapFactory.decodeStream(in);
            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.logo_blue_icon);
        intent=new Intent(this, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.icon)
                .setLargeIcon(myBitmap)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
      /*  if (notificationId.equals("1")) {
            notificationBuilder.addAction(R.drawable.accept, "BID NOW", pendingIntent);
        }*/
        startForeground(1, notificationBuilder.build());
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

}
