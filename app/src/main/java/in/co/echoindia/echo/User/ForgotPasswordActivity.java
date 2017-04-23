package in.co.echoindia.echo.User;

import android.app.Dialog;
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

import java.util.concurrent.TimeUnit;

import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;

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
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOTPDialog();
            }
        });
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

}
