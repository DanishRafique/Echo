package in.co.echoindia.echo.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
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

import in.co.echoindia.echo.HomePage.HomePageActivity;
import in.co.echoindia.echo.Model.UserDetailsModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

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
            pDialog.show();
            pDialog.setMessage("Please Wait");
            pDialog.setCancelable(true);
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
            pDialog.dismiss();
            setUserData(o);


        }
    }

    private void setUserData(Object o)  {
        try {
            JSONObject jObject=new JSONObject(o.toString());
            String checkStatus=jObject.getString("status");
            if(checkStatus.equals("0")&&o != null){
                mUserDetailsModel=new UserDetailsModel();
                JSONObject responseObject=jObject.getJSONObject("response");
                JSONArray jArray=responseObject.getJSONArray("UserDetail");
                JSONObject userObj=jArray.getJSONObject(0);
                mUserDetailsModel.setUserName(userObj.getString("UserCode"));
                mUserDetailsModel.setPassword(password.getText().toString().trim());
                mUserDetailsModel.setFirstName(userObj.getString("FirstName"));
                mUserDetailsModel.setLastName(userObj.getString("LastName"));
                mUserDetailsModel.setEmailId(userObj.getString("EmailId"));
                mUserDetailsModel.setPhoneNo(userObj.getString("PhoneNo"));
                mUserDetailsModel.setAddress(userObj.getString("Address"));
                mUserDetailsModel.setWard(userObj.getString("City"));
                mUserDetailsModel.setPinCode(userObj.getString("PinCode"));
                mUserDetailsModel.setDistrict(userObj.getString("District"));
                mUserDetailsModel.setState(userObj.getString("State"));
                mUserDetailsModel.setUserPhoto(userObj.getString("UserPhoto"));
                mUserDetailsModel.setVoterIdPhoto(userObj.getString("VoterId"));
                mUserDetailsModel.setAadhaarPhoto(userObj.getString("AadharCard"));
                mUserDetailsModel.setIssueMaker(userObj.getString("IssueMaker"));
                mUserDetailsModel.setIsVerified(userObj.getString("isVerified"));
                editor.putString(Constants.SETTINGS_IS_LOGGED_TYPE,"USER");
                editor.putBoolean(Constants.SETTINGS_IS_LOGGED,true);
                editor.putString(Constants.SETTINGS_IS_LOGGED_USER_CODE, userObj.getString("UserCode"));
                editor.putString(Constants.SETTINGS_OBJ_USER, new Gson().toJson(mUserDetailsModel));
                editor.commit();
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(LoginActivity.this, HomePageActivity.class);
                startActivity(i);
                LoginActivity.this.finish();
            }
            else if(checkStatus.equals("1")){
                Toast.makeText(this, "Please Check Your Username and Password", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Login Failure", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,e.toString());
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(pDialog!=null)
            pDialog.dismiss();
    }
}