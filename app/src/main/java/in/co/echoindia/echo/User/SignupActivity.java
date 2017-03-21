package in.co.echoindia.echo.User;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;

public class SignupActivity extends AppCompatActivity {

    EditText firstName,lastName, userName , emailAddress,phoneNumber,password, confirmPassword, address , city , ward , pinCode , district , state;
    LinearLayout llUserInformation , llUserAddress;
    Button btnSignUp;
    TextView tvSignUpContinue;
    int randInt;
    Dialog checkOTPDialog;
    CountDownTimer otpTimer;
    private static final Random rand = new Random();

    TextView otpSubmit, resendOTP;
    EditText edtOtp;
    TextView txtTimer;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firstName=(EditText)findViewById(R.id.edt_first_name);
        lastName=(EditText)findViewById(R.id.edt_last_name);
        userName=(EditText)findViewById(R.id.edt_user_name);
        emailAddress=(EditText)findViewById(R.id.edt_email_id);
        phoneNumber=(EditText)findViewById(R.id.edt_phone_number);
        password=(EditText)findViewById(R.id.edt_password);
        confirmPassword=(EditText)findViewById(R.id.edt_confirm_passowrd);
        address=(EditText)findViewById(R.id.edt_address);
        city=(EditText)findViewById(R.id.edt_city);
        ward=(EditText)findViewById(R.id.edt_ward);
        pinCode=(EditText)findViewById(R.id.edt_pin_code);
        district=(EditText)findViewById(R.id.edt_district);
        state=(EditText)findViewById(R.id.edt_state);
        btnSignUp=(Button)findViewById(R.id.btn_sign_up);

        llUserInformation=(LinearLayout)findViewById(R.id.ll_user_information);
        llUserAddress=(LinearLayout)findViewById(R.id.ll_user_address);
        tvSignUpContinue=(TextView)findViewById(R.id.tv_sign_up_continue);
        sharedpreferences = AppUtil.getAppPreferences(this);
        editor = sharedpreferences.edit();
        tvSignUpContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               workOnNextButtonClick();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDocumentUpload=new Intent(SignupActivity.this,DocumentUploadActivity.class);
                startActivity(intentDocumentUpload);
                SignupActivity.this.finish();
            }
        });
    }

    private void workOnNextButtonClick() {

        String fname = firstName.getText().toString().trim();
        String lname = lastName.getText().toString().trim();
        String emailid = emailAddress.getText().toString().trim();
        String mob = phoneNumber.getText().toString().trim();
        String corporatename = userName.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String cpass = confirmPassword.getText().toString().trim();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (fname.equals("") || lname.equals("") || emailid.equals("") || mob.equals("") || corporatename.equals("") || pass.equals("") || cpass.equals("")) {
            Toast.makeText(SignupActivity.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
        } else if (!emailid.matches(emailPattern)) {
            Toast.makeText(SignupActivity.this, "Invalid Email Id", Toast.LENGTH_SHORT).show();
        } else if (!pass.equals(cpass)) {
            Toast.makeText(SignupActivity.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
        } else if (pass.length() < 8) {
            Toast.makeText(SignupActivity.this, "Password should be minimum 8 character", Toast.LENGTH_SHORT).show();
        } else {
            openOTPDialog();

        }

    }


    void openOTPDialog() {
        editor.putString("OTP","0000");
        editor.commit();
        checkOTPDialog = new Dialog(this);
        checkOTPDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        checkOTPDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        checkOTPDialog.setContentView(R.layout.dialog_otp_check);

        otpSubmit = (TextView) checkOTPDialog.findViewById(R.id.otp_submit);
        edtOtp = (EditText) checkOTPDialog.findViewById(R.id.edt_otp);
        txtTimer = (TextView) checkOTPDialog.findViewById(R.id.timer);
        resendOTP = (TextView) checkOTPDialog.findViewById(R.id.resend_otp);
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
            Toast.makeText(SignupActivity.this, "OTP Matched Successfully", Toast.LENGTH_SHORT).show();
            llUserInformation.setVisibility(View.GONE);
            llUserAddress.setVisibility(View.VISIBLE);
            checkOTPDialog.dismiss();

        } else {
            Toast.makeText(SignupActivity.this, "OTP did not match , Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(checkOTPDialog!=null)
            checkOTPDialog.dismiss();
    }
}
