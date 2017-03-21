package in.co.echoindia.echo.User;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.MarshMallowPermission;

public class SignupActivity extends AppCompatActivity {

    EditText firstName,lastName, userName , emailAddress,phoneNumber,password, confirmPassword, address , city , ward , pinCode , district , state;
    LinearLayout llUserInformation , llUserAddress;
    Button btnSignUp;
    TextView tvSignUpContinue;
    int randInt;
    Dialog checkOTPDialog;
    CountDownTimer otpTimer;
    private static final Random rand = new Random();
    private static final String LOG_TAG = "SignUpActivity";
    private ProgressDialog pDialog;

    TextView otpSubmit, resendOTP;
    EditText edtOtp;
    TextView txtTimer;
    ImageView imgProfile;

    String encodedImage;
    byte[] byteArray;
    ImageView imageView;
    Dialog chooseImage;
    TextView chooseImageCamera , chooseImageGallery;
    Button btnUpload;
    public static final int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private Uri imageToUploadUri;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pDialog = new ProgressDialog(this);
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
        imgProfile=(ImageView)findViewById(R.id.img_profile);
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChooseImageDialog();
            }
        });

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

    void makeRoundCorners(ImageView imgView , int drawable){
        Bitmap mBitmap = ((BitmapDrawable) getResources().getDrawable(drawable)).getBitmap();
        Bitmap imageRounded = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), mBitmap.getConfig());
        Canvas canvas = new Canvas(imageRounded);
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight())), 25, 25, mPaint);// Round Image Corner 100 100 100 100
        imgView.setImageBitmap(imageRounded);
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
            ExistenceTest existenceTest=new ExistenceTest();
            existenceTest.execute();
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

    void openChooseImageDialog(){
        chooseImage = new Dialog(this);
        chooseImage.requestWindowFeature(Window.FEATURE_NO_TITLE);
        chooseImage.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        chooseImage.setContentView(R.layout.dialog_choose_image);

        chooseImageCamera = (TextView) chooseImage.findViewById(R.id.choose_image_camera);
        chooseImageGallery = (TextView) chooseImage.findViewById(R.id.choose_image_gallery);
        chooseImage.show();
        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(SignupActivity.this);
        if (!marshMallowPermission.checkPermissionForExternalStorage()) {
            marshMallowPermission.requestPermissionForExternalStorage();
        }
        chooseImageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseImageFromCamera();
                chooseImage.dismiss();
            }
        });
        chooseImageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseImageFromGallery();
                chooseImage.dismiss();
            }
        });
    }

    private void ChooseImageFromCamera() {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                && !Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING)) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(Environment.getExternalStorageDirectory(), "POST_IMAGE.jpg");
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            imageToUploadUri = Uri.fromFile(f);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);

        } else {
            Toast.makeText(SignupActivity.this, "No activity found to perform this task", Toast.LENGTH_SHORT).show();
        }
    }

    private void ChooseImageFromGallery() {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                && !Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING)) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, RESULT_LOAD_IMAGE);

        } else {
            Toast.makeText(SignupActivity.this, "No activity found to perform this task", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageView=imgProfile;

        if (requestCode==CAMERA_REQUEST || requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Bitmap originBitmap = null;
            Uri selectedImage=null;
            if(requestCode == RESULT_LOAD_IMAGE )
                selectedImage=data.getData();
            if(imageToUploadUri != null)
                selectedImage=imageToUploadUri;
            InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
                originBitmap = BitmapFactory.decodeStream(imageStream);
            } catch (FileNotFoundException e) {
                Log.e("CatchActivityResult",e.getMessage().toString());

            }
            if (originBitmap != null) {
                this.imageView.setImageBitmap(originBitmap);
                Log.e("ImageLoaded", "Done Loading Image");
                try {
                    Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
                    byteArray = byteArrayOutputStream.toByteArray();
                    Log.e("byteArray", byteArray.toString());
                    btnUpload.setVisibility(View.VISIBLE);
                    encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                } catch (Exception e) {
                    Log.e("OnActivityResultEx", e.toString());
                }
            }
        } else {
            Log.e("ErrorOccurredActRes", "ErrorOnActRes");

        }

    }

    class ExistenceTest extends AsyncTask {

        String url_user_login = "http://echoindia.co.in/php/CheckExist.php";
        //http://echoindia.co.in/php/UserLogin.php?username=user&password=123&deviceType=Android&deviceId=1234
        String userNameStr=userName.getText().toString().trim();
        String phoneNumberStr=phoneNumber.getText().toString().trim();
        String emailStr=emailAddress.getText().toString().trim();


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
                postDataParams.put("email",emailStr);
                postDataParams.put("phone",phoneNumberStr);

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
            JSONObject jObject= null;
            try {
                jObject = new JSONObject(o.toString());
                String checkStatus=jObject.getString("status");
                if(checkStatus.equals("1")){
                    openOTPDialog();
                }
                else if(checkStatus.equals("0")){
                    Toast.makeText(SignupActivity.this, jObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(checkOTPDialog!=null)
            checkOTPDialog.dismiss();
        if(chooseImage!=null)
            chooseImage.dismiss();
        if(pDialog!=null)
            pDialog.dismiss();
    }
}
