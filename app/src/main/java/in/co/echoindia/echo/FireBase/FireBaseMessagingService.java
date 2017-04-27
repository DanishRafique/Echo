package in.co.echoindia.echo.FireBase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

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

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        sharedpreferences = AppUtil.getAppPreferences(this);
        editor = sharedpreferences.edit();
        Log.e("FireBase Called", "FireBase Called");
        Log.e("FireBase", remoteMessage.getData().toString());
        notificationId = remoteMessage.getData().get("notificationID");
        if (notificationId.equals("1")) {
            message = remoteMessage.getData().get("body");
            title = remoteMessage.getData().get("title");


        }
        else if(notificationId.equals("3")){
            message = remoteMessage.getData().get("body");
            title = remoteMessage.getData().get("title");


        }
        else if(notificationId.equals("4")){
            message = remoteMessage.getData().get("body");
            title = remoteMessage.getData().get("title");


        }
        //Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.logo_blue_icon);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
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
