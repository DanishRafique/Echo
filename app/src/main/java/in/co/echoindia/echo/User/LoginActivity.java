package in.co.echoindia.echo.User;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import in.co.echoindia.echo.Model.UserDetailsModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;

public class LoginActivity extends AppCompatActivity{

    private ProgressDialog pDialog;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    EditText userName,password;
    LinearLayout login;

    UserDetailsModel mUserDetailsModel;

    private static final String LOG_TAG = "LoginActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedpreferences = AppUtil.getAppPreferences(this);
        editor = sharedpreferences.edit();
        pDialog = new ProgressDialog(this);

        userName = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (LinearLayout)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login login = new Login();
                login.execute("");
            }
        });
    }
    class Login extends AsyncTask {

        String url_user_login = "http://echoindia.co.in/php/UserLogin.php";
        //http://echoindia.co.in/php/UserLogin.php?username=user&password=123&deviceType=Android&deviceId=1234
        String userNameStr=userName.getText().toString().trim();
        String passwordStr=password.getText().toString().trim();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {

            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(url_user_login);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username",userNameStr);
                postDataParams.put("password",passwordStr);
                postDataParams.put("deviceType","ANDROID");
                postDataParams.put("deviceId","1234");
                Log.e(LOG_TAG,"URL"+url_user_login);
                Log.e(LOG_TAG,"PostParam"+postDataParams.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8"));
                writer.write(AppUtil.getPostDataString(postDataParams));
                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader( conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");

                    String line = "";
                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    Log.e(LOG_TAG,sb.toString());
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception ex) {
                Log.e(LOG_TAG,"Exception inside");
                return null;
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            Log.e(LOG_TAG,"onPostExecute");
            Log.e(LOG_TAG,"JSONObject Received "+o.toString());
            try {
                JSONObject jObject = new JSONObject(o.toString());
                Log.e(LOG_TAG,jObject.toString());

            } catch (JSONException e) {
                Log.e(LOG_TAG,e.toString());
                e.printStackTrace();
            }
        }
    }
}