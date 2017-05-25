package in.co.echoindia.echo.User;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

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

import in.co.echoindia.echo.Model.RepDetailModel;
import in.co.echoindia.echo.Model.UserDetailsModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText changePasswordOld,changePasswordNew, changePasswordNewConfirm;
    Button changePasswordButton;
    String oldPassword,newPassword,newPasswordConfirm,actualPassword;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    UserDetailsModel userDetailsModel;
    RepDetailModel repDetailModel;
    private static final String LOG_TAG = "ChangePasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = AppUtil.getAppPreferences(this);
        editor = sharedpreferences.edit();
        if(sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_TYPE,"").equals("USER")) {
            userDetailsModel = new Gson().fromJson(sharedpreferences.getString(Constants.SETTINGS_OBJ_USER, ""), UserDetailsModel.class);
            actualPassword=userDetailsModel.getPassword();
        }
        else if(sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_TYPE,"").equals("REP")) {
            repDetailModel = new Gson().fromJson(sharedpreferences.getString(Constants.SETTINGS_OBJ_USER, ""), RepDetailModel.class);
            actualPassword=repDetailModel.getPassword();
        }

        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        changePasswordOld=(EditText)findViewById(R.id.change_password_old);
        changePasswordNew=(EditText)findViewById(R.id.change_password_new);
        changePasswordNewConfirm=(EditText)findViewById(R.id.change_password_new_confirm);
        changePasswordButton=(Button)findViewById(R.id.change_password_button);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldPassword = changePasswordOld.getText().toString().trim();
                newPassword = changePasswordNew.getText().toString().trim();
                newPasswordConfirm = changePasswordNewConfirm.getText().toString().trim();
                if (oldPassword.equals("") || newPassword.equals("") || newPasswordConfirm.equals("")) {
                    Toast.makeText(ChangePasswordActivity.this, "Please fill all the details Update", Toast.LENGTH_SHORT).show();
                } else if (!actualPassword.equals(oldPassword)) {
                    Toast.makeText(ChangePasswordActivity.this, "Old Password does not match . Please Try Again .", Toast.LENGTH_SHORT).show();
                } else if(!newPassword.equals(newPasswordConfirm)) {
                    Toast.makeText(ChangePasswordActivity.this, "New Password does not match with the Confirmed Password", Toast.LENGTH_SHORT).show();
                } else {
                    ChangePassword changePassword=new ChangePassword();
                    changePassword.execute();

                }

            }
        });
    }
    class ChangePassword extends AsyncTask {

        String url_user_login = "http://echoindia.co.in/php/password.php";


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
                postDataParams.put("todo","changePassword");
                postDataParams.put("username",sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_USER_CODE,""));
                postDataParams.put("userType",sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_TYPE,""));
                postDataParams.put("oldPassword",oldPassword);
                postDataParams.put("newPassword",newPassword);
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
            super.onPostExecute(o);
            if(o!=null) {
                JSONObject jObject = null;
                try {
                    jObject = new JSONObject(o.toString());
                    String checkStatus = jObject.getString("status");
                    if (checkStatus.equals("1")) {
                        Toast.makeText(ChangePasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();

                        if (sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_TYPE, "").equals("USER")) {
                            userDetailsModel.setPassword(newPassword);
                            editor.putString(Constants.SETTINGS_OBJ_USER, new Gson().toJson(userDetailsModel));
                        } else if (sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_TYPE, "").equals("REP")) {
                            repDetailModel.setPassword(newPassword);
                            editor.putString(Constants.SETTINGS_OBJ_USER, new Gson().toJson(repDetailModel));
                        }
                        editor.commit();
                        finish();
                    } else if (checkStatus.equals("0")) {
                        Toast.makeText(ChangePasswordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(ChangePasswordActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }


        }
    }



}
