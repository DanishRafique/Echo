package in.co.echoindia.echo.User;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;

import static it.sephiroth.android.library.imagezoom.ImageViewTouchBase.LOG_TAG;

public class ForgotPasswordActivity extends AppCompatActivity {

    Dialog checkOTPDialog;
    CountDownTimer otpTimer;
    TextView otpSubmit, resendOTP , txtOTPNumber;
    EditText edtOtp;
    TextView txtTimer;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    LinearLayout searchll,changell;
    Button searchBtn;
    EditText forgotPasswordText;
    EditText forgotPasswordNew , forgotPasswordNewConfirm ;
    Button changePasswordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedpreferences = AppUtil.getAppPreferences(this);
        editor = sharedpreferences.edit();
        searchll=(LinearLayout)findViewById(R.id.forgot_password_search_ll);
        changell=(LinearLayout)findViewById(R.id.forgot_change_password_ll);
        searchBtn=(Button)findViewById(R.id.forgot_password_search_button);
        changePasswordBtn=(Button)findViewById(R.id.change_password_button);
        forgotPasswordText=(EditText)findViewById(R.id.forgot_password_text);
        forgotPasswordNew=(EditText)findViewById(R.id.forgot_password_new);
        forgotPasswordNewConfirm=(EditText)findViewById(R.id.forgot_password_new_confirm);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetUserName mGetUserName=new GetUserName(forgotPasswordText.getText().toString());
                mGetUserName.execute();
            }
        });
        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword=forgotPasswordNew.getText().toString();
                String newPasswordConfirm=forgotPasswordNewConfirm.getText().toString();
                if(newPassword.equals(newPasswordConfirm)) {
                    ChangePassword mChangePassword = new ChangePassword(newPassword);
                    mChangePassword.execute();
                }
            }
        });

    }

    void openOTPDialog() {
        checkOTPDialog = new Dialog(this);
        checkOTPDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        checkOTPDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        checkOTPDialog.setContentView(R.layout.dialog_otp_check);
        otpSubmit = (TextView) checkOTPDialog.findViewById(R.id.otp_submit);
        edtOtp = (EditText) checkOTPDialog.findViewById(R.id.edt_otp);
        txtTimer = (TextView) checkOTPDialog.findViewById(R.id.timer);
        resendOTP = (TextView) checkOTPDialog.findViewById(R.id.resend_otp);
        txtOTPNumber=(TextView)checkOTPDialog.findViewById(R.id.txt_otp_number);
        //txtOTPNumber.setText("THE OTP HAS BEEN SEND TO "+phoneNumber.getText().toString().trim());
        setOtpTimer();
        checkOTPDialog.show();

        otpSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otpTimer.cancel();
                checkOTP();
                checkOTPDialog.dismiss();
            }
        });
        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendOTP.setVisibility(View.GONE);
                setOtpTimer();
            }
        });
    }

    void setOtpTimer() {
        if (otpTimer != null) {
            otpTimer.cancel();
        }
        otpTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                String timercount = "" + String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                txtTimer.setText(timercount + " remaining");
            }

            public void onFinish() {
                resendOTP.setVisibility(View.VISIBLE);
                txtTimer.setText("You can try sending the OTP again");
            }
        }.start();

    }

    void checkOTP() {
        String otpGiven = sharedpreferences.getString("OTP", "").trim();
        String enteredText = edtOtp.getText().toString().trim();
        if (enteredText.equals(otpGiven)) {
            Toast.makeText(ForgotPasswordActivity.this, "OTP Matched Successfully", Toast.LENGTH_SHORT).show();
            searchll.setVisibility(View.GONE);
            changell.setVisibility(View.VISIBLE);
            checkOTPDialog.dismiss();

        } else {
            Toast.makeText(ForgotPasswordActivity.this, "OTP did not match , Please try again.", Toast.LENGTH_SHORT).show();
        }
    }



    class GetUserName extends AsyncTask {

        String url_user_login = "http://echoindia.co.in/php/password.php";
        String data;

        public GetUserName(String data){
            this.data=data;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object[] params) {
            editor.putString("OTP","0000");
            editor.commit();
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(url_user_login);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("todo","getUsername");
                postDataParams.put("user",data);
                postDataParams.put("otp",sharedpreferences.getString("OTP",""));
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
            JSONObject jObject= null;
            try {
                jObject = new JSONObject(o.toString());
                String checkStatus=jObject.getString("status");
                if(checkStatus.equals("1")){
                    editor.putString("userType",jObject.getString("userType"));
                    editor.putString("username",jObject.getString("username"));
                    editor.commit();
                    openOTPDialog();
                }
                else if(checkStatus.equals("2")){
                    Toast.makeText(ForgotPasswordActivity.this, "User Doesn't Exist", Toast.LENGTH_SHORT).show();
                }
                else if(checkStatus.equals("0")){
                    Toast.makeText(ForgotPasswordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    class ChangePassword extends AsyncTask {

        String url_user_login = "http://echoindia.co.in/php/password.php";
        String data;

        public ChangePassword(String data){
            this.data=data;
        }

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
                postDataParams.put("todo","forgotPassword");
                postDataParams.put("username",sharedpreferences.getString("username",""));
                postDataParams.put("userType",sharedpreferences.getString("userType",""));
                postDataParams.put("password",data);
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
            JSONObject jObject= null;
            try {
                jObject = new JSONObject(o.toString());
                String checkStatus=jObject.getString("status");
                if(checkStatus.equals("1")){
                    Toast.makeText(ForgotPasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if(checkStatus.equals("0")){
                    Toast.makeText(ForgotPasswordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
