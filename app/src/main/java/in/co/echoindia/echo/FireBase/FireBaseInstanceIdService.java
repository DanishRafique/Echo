package in.co.echoindia.echo.FireBase;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

/**
 * Created by Danish Rafique on 27-04-2017.
 */

public class FireBaseInstanceIdService extends FirebaseInstanceIdService {
        private static final String LOG_TAG = "FireBaseInstanceId";

        SharedPreferences sharedpreferences;
        SharedPreferences.Editor editor;
        /**
         * Called if InstanceID token is updated. This may occur if the security of
         * the previous token had been compromised. Note that this is called when the InstanceID token
         * is initially generated so this is where you would retrieve the token.
         */
        // [START refresh_token]
        @Override
        public void onTokenRefresh() {
            // Get updated InstanceID token.
            Log.e(LOG_TAG,"Inside Instance");
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();

            Log.e(LOG_TAG,"onTokenRefresh");

            // If you want to send messages to this application instance or
            // manage this apps subscriptions on the server side, send the
            // Instance ID token to your app server.
            storeRegIdInPref(refreshedToken);
            /**super.onTokenRefresh();
             String refreshedToken = FirebaseInstanceId.getInstance().getToken();

             // Saving reg id to shared preferences
             storeRegIdInPref(refreshedToken);

             // sending reg id to your server
             sendRegistrationToServer(refreshedToken);

             // Notify UI that registration has completed, so the progress indicator can be hidden.
             Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
             registrationComplete.putExtra("token", refreshedToken);
             LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
             }**/
        }

        private void storeRegIdInPref(String token) {

            Log.e(LOG_TAG,token+" ");
            sharedpreferences = AppUtil.getAppPreferences(this);
            editor = sharedpreferences.edit();
            editor.putString(Constants.REG_ID, token);
            editor.commit();

        }
}
