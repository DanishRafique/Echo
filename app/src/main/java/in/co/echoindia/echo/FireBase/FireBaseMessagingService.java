package in.co.echoindia.echo.FireBase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.co.echoindia.echo.Model.NotificationModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.User.SplashActivity;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

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
    ArrayList<NotificationModel> notificationModelArrayList=new ArrayList<>();

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



        Type type = new TypeToken<ArrayList<NotificationModel>>() {}.getType();
        notificationModelArrayList = new Gson().fromJson(sharedpreferences.getString(Constants.MY_NOTIFICATION, ""), type);

        if(notificationModelArrayList==null){
            notificationModelArrayList=new ArrayList<>();
        }

        NotificationModel newNotification=new NotificationModel();
        newNotification.setPostID(remoteMessage.getData().get("postid"));
        newNotification.setNotificationID(remoteMessage.getData().get("id"));
        newNotification.setNotificationBody(remoteMessage.getData().get("body"));
        newNotification.setNotificationImage(remoteMessage.getData().get("image"));
        newNotification.setNotificationMessage(remoteMessage.getData().get("message"));
        newNotification.setNotificationTitle(remoteMessage.getData().get("title"));
        Date currentDate = new Date();
        String dateToday = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String timeNow = sdf.format(new Date());
        newNotification.setNotificationTime(timeNow);
        newNotification.setNotificationDate(dateToday);
        notificationModelArrayList.add(newNotification);
        int numberOfNotication=sharedpreferences.getInt(Constants.NUMBER_OF_NOTIFICATION,0);
        editor.putString(Constants.MY_NOTIFICATION, new Gson().toJson(notificationModelArrayList));
        editor.putInt(Constants.NUMBER_OF_NOTIFICATION,numberOfNotication+1);
        editor.commit();

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

        Resources res = this.getResources();
        int height = (int) res.getDimension(android.R.dimen.notification_large_icon_height);
        int width = (int) res.getDimension(android.R.dimen.notification_large_icon_width);

        myBitmap = Bitmap.createScaledBitmap(myBitmap, width, height, false);

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
