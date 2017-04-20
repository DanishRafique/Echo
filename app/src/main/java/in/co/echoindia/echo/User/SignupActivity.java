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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
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
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;
import in.co.echoindia.echo.Model.LokSabhaModel;
import in.co.echoindia.echo.Model.MunicipalCorporationModel;
import in.co.echoindia.echo.Model.PoliticalPartyModel;
import in.co.echoindia.echo.Model.VidhanSabhaModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;
import in.co.echoindia.echo.Utils.MarshMallowPermission;

public class SignupActivity extends AppCompatActivity {

    public static final int RESULT_LOAD_IMAGE = 1;
    private static final Random rand = new Random();
    private static final String LOG_TAG = "SignUpActivity";
    private static final int CAMERA_REQUEST = 1888;
    EditText firstName,lastName, userName , emailAddress,phoneNumber,password, confirmPassword,city , pinCode , district , state;
    LinearLayout llUserInformation , llUserAddress,llUserPolitic;
    Button btnSignUp;
    TextView tvSignUpContinue;
    int randInt;
    Dialog checkOTPDialog;
    CountDownTimer otpTimer;
    TextView otpSubmit, resendOTP , txtOTPNumber;
    EditText edtOtp;
    TextView txtTimer;
    CircleImageView imgProfile;
    String encodedImage;
    byte[] byteArray;
    ImageView imageView;
    Dialog chooseImage;
    TextView chooseImageCamera , chooseImageGallery;
    Button btnUpload;
    AutoCompleteTextView lokSabha,vidhanSabha , ward;
    VidhanSabhaModel mVidhanSabhaModel;
    LokSabhaModel mLokSabhaModel;
    MunicipalCorporationModel mMunicipalCorporationModel;
    ArrayList<VidhanSabhaModel> mVidhanSabhaDetail=new ArrayList<>();
    ArrayList<LokSabhaModel> mLokSabhaDetail=new ArrayList<>();
    ArrayList<String> lokSabhaList = new ArrayList<>();
    ArrayList<String> vidhanSabhaList = new ArrayList<>();
    ArrayList<String> wardList = new ArrayList<>();
    ArrayList<String> politicalPartyList=new ArrayList<>();
    ArrayList<String> designationList=new ArrayList<>();
    ArrayAdapter<String> mLokSabhaArrayAdapter;
    ArrayAdapter<String> mVidhanSabhaArrayAdapter;
    ArrayAdapter<String> mWardArrayAdapter;
    ArrayAdapter<String> mDesignationArrayAdapter;
    ArrayAdapter<String> mPoliticalPartyArrayAdapter;
    ArrayList<MunicipalCorporationModel> mMunicipalCorporationModelDetail=new ArrayList<>();
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    AutoCompleteTextView repParty,repDesignation;
    EditText repLocation,repQualification,repHomePage,repTwitter;
    CheckBox checkboxPolitics;
    TextView tvPoliticContinue;
    boolean isPolitician=false;
    private ProgressDialog pDialog;
    private Uri imageToUploadUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sharedpreferences = AppUtil.getAppPreferences(this);
        editor = sharedpreferences.edit();
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
        lokSabha=(AutoCompleteTextView) findViewById(R.id.auto_lok_sabha);
        vidhanSabha=(AutoCompleteTextView) findViewById(R.id.auto_vidhan_sabha);
        city=(EditText)findViewById(R.id.edt_city);
        ward=(AutoCompleteTextView) findViewById(R.id.auto_ward);
        pinCode=(EditText)findViewById(R.id.edt_pin_code);
        state=(EditText)findViewById(R.id.edt_state);
        btnSignUp=(Button)findViewById(R.id.btn_sign_up);
        imgProfile=(CircleImageView)findViewById(R.id.img_profile);
        checkboxPolitics=(CheckBox)findViewById(R.id.checkbox_politic);
        repParty=(AutoCompleteTextView)findViewById(R.id.auto_rep_party);
        repDesignation=(AutoCompleteTextView)findViewById(R.id.auto_rep_designation);
        repLocation=(EditText)findViewById(R.id.edt_rep_location);
        repQualification=(EditText)findViewById(R.id.edt_rep_qualification);
        repHomePage=(EditText)findViewById(R.id.edt_home_page);
        repTwitter=(EditText)findViewById(R.id.edt_twitter);
        llUserPolitic=(LinearLayout)findViewById(R.id.ll_user_politic);
        tvPoliticContinue=(TextView)findViewById(R.id.tv_politic_continue);
        city.setText(sharedpreferences.getString(Constants.CURRENT_CITY,""));
        state.setText(sharedpreferences.getString(Constants.CURRENT_STATE,""));
        pinCode.setText(sharedpreferences.getString(Constants.CURRENT_PIN_CODE,""));
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChooseImageDialog();
            }
        });
        llUserInformation=(LinearLayout)findViewById(R.id.ll_user_information);
        llUserAddress=(LinearLayout)findViewById(R.id.ll_user_address);
        tvSignUpContinue=(TextView)findViewById(R.id.tv_sign_up_continue);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeClick=new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(homeClick);
                finish();
            }});
        tvSignUpContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               workOnNextButtonClick();
            }
        });
        tvPoliticContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llUserPolitic.setVisibility(View.GONE);
                llUserAddress.setVisibility(View.VISIBLE);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!lokSabha.getText().toString().equals("")&&!lokSabhaList.contains(lokSabha.getText().toString())){
                    Toast.makeText(SignupActivity.this, "Please Choose the Lok Sabha Constituency from the list.", Toast.LENGTH_SHORT).show();
                }
                else if(!vidhanSabha.getText().toString().equals("")&&!vidhanSabhaList.contains(vidhanSabha.getText().toString())){
                    Toast.makeText(SignupActivity.this, "Please Choose the Vidhan Sabha Constituency from the list.", Toast.LENGTH_SHORT).show();
                }
                else if(!ward.getText().toString().equals("")&&!wardList.contains(ward.getText().toString().trim())){
                    Toast.makeText(SignupActivity.this, "Please Choose Ward Number from the list.", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(isPolitician){
                        SignUpRep mSignUpRep=new SignUpRep();
                        mSignUpRep.execute();
                    }
                    else {
                        SignUpUser mSignUpUser = new SignUpUser();
                        mSignUpUser.execute();
                    }
                }
            }
        });

        politicalPartyList.clear();

        Type type = new TypeToken<ArrayList<PoliticalPartyModel>>() {}.getType();
        ArrayList<PoliticalPartyModel> politicalList= new Gson().fromJson(sharedpreferences.getString(Constants.POLITICAL_PARTY_LIST, ""), type);
        for(int i=0;i<politicalList.size();i++){
            politicalPartyList.add(politicalList.get(i).getPartyNameShort());
        }
        mPoliticalPartyArrayAdapter = new ArrayAdapter(SignupActivity.this, R.layout.item_spinner_textview, politicalPartyList);
        repParty.setAdapter(mPoliticalPartyArrayAdapter);
        repParty.setThreshold(1);
        FetchVidhanSabha mFetchVidhanSabha=new FetchVidhanSabha();
        mFetchVidhanSabha.execute();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent homeClick=new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(homeClick);
        finish();
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

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
       if(checked){
          isPolitician=true;
       }
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
        txtOTPNumber=(TextView)checkOTPDialog.findViewById(R.id.txt_otp_number);
        txtOTPNumber.setText("THE OTP HAS BEEN SEND TO "+phoneNumber.getText().toString().trim());
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
            if(checkboxPolitics.isChecked()){
                llUserPolitic.setVisibility(View.VISIBLE);
            }
            else{
                llUserAddress.setVisibility(View.VISIBLE);
            }
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
                Log.e(LOG_TAG,"CatchActivityResult"+e.getMessage().toString());

            }
            if (originBitmap != null) {
                this.imageView.setImageBitmap(originBitmap);
                Log.e(LOG_TAG,"ImageLoaded"+"Done Loading Image");
                try {
                    Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
                    byteArray = byteArrayOutputStream.toByteArray();
                    Log.e("byteArray", byteArray.toString());
                    //btnUpload.setVisibility(View.VISIBLE);
                    encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    //Log.e(LOG_TAG,"EncodedImage"+encodedImage);
                } catch (Exception e) {
                    Log.e(LOG_TAG,"OnActivityResultException : "+e.toString());
                }
            }
        } else {
            Log.e(LOG_TAG,"ErrorOccurredActivityResult");

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

    private void setVidhanData(Object o)  {
        Log.e(LOG_TAG,"VIDHAN SABHA JSON : "+o.toString());
        try {
            vidhanSabhaList.clear();
            JSONObject jObject=new JSONObject(o.toString());
            String checkStatus=jObject.getString("status");
            if(checkStatus.equals("1")&& o != null){
                JSONArray vidhanArray=jObject.getJSONArray("data");
                Log.e(LOG_TAG,"Vidhan Sabha Element Count "+vidhanArray.length());
                for(int i =0 ; i<vidhanArray.length();i++){
                    JSONObject vidhanObject=vidhanArray.getJSONObject(i);

                    mVidhanSabhaModel=new VidhanSabhaModel();
                    mVidhanSabhaModel.setConstituencyId(vidhanObject.getString("ConstituencyId"));
                    mVidhanSabhaModel.setConstituencyName(vidhanObject.getString("ConstituencyName"));
                    mVidhanSabhaModel.setConstituencyDistrict(vidhanObject.getString("ConstituencyDistrict"));
                    mVidhanSabhaModel.setConstituencyState(vidhanObject.getString("ConstituencyState"));
                    mVidhanSabhaDetail.add(mVidhanSabhaModel);
                    vidhanSabhaList.add(mVidhanSabhaModel.getConstituencyName());
                }
                mVidhanSabhaArrayAdapter = new ArrayAdapter(SignupActivity.this, R.layout.item_spinner_textview, vidhanSabhaList);
                vidhanSabha.setAdapter(mVidhanSabhaArrayAdapter);
                vidhanSabha.setThreshold(1);
                editor.putString(Constants.VIDHAN_SABHA_LIST, new Gson().toJson(mVidhanSabhaDetail));
                editor.commit();
            }
            else {
                Toast.makeText(this, "Server Connection Error", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,e.toString());
        }
        FetchLokSabha mFetchLokSabha=new FetchLokSabha();
        mFetchLokSabha.execute();

    }

    private void setMunicipalCorporationWardData(Object o)  {
        Log.e(LOG_TAG,"WARD JSON : "+o.toString());
        try {
            wardList.clear();
            JSONObject jObject=new JSONObject(o.toString());
            String checkStatus=jObject.getString("status");
            if(checkStatus.equals("1")&& o != null){
                JSONArray wardArray=jObject.getJSONArray("data");
                Log.e(LOG_TAG,"Ward Element Count "+wardArray.length());
                for(int i =0 ; i<wardArray.length();i++){
                    JSONObject wardObject=wardArray.getJSONObject(i);
                    mMunicipalCorporationModel=new MunicipalCorporationModel();
                    mMunicipalCorporationModel.setWardId(wardObject.getString("WardId"));
                    mMunicipalCorporationModel.setWardState(wardObject.getString("WardState"));
                    mMunicipalCorporationModel.setWardCity(wardObject.getString("WardCity"));
                    mMunicipalCorporationModel.setWardCityRegion(wardObject.getString("WardCityRegion"));
                    mMunicipalCorporationModel.setWardStartNumber(wardObject.getInt("WardStartNumber"));
                    mMunicipalCorporationModel.setWardEndNumber(wardObject.getInt("WardEndNumber"));
                    mMunicipalCorporationModelDetail.add(mMunicipalCorporationModel);

                }
                for(int i=mMunicipalCorporationModel.getWardStartNumber();i<=mMunicipalCorporationModel.getWardEndNumber();i++){
                    wardList.add(String.valueOf(i));
                }
                mWardArrayAdapter = new ArrayAdapter(SignupActivity.this, R.layout.item_spinner_textview, wardList);
                ward.setAdapter(mWardArrayAdapter);
                ward.setThreshold(1);
                editor.putString(Constants.MUNICIPAL_CORPORATION_LIST, new Gson().toJson(mMunicipalCorporationModelDetail));
                editor.commit();
            }
            else {
                Toast.makeText(this, "Server Connection Error", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,e.toString());
        }
        FetchDesignation mFetchDesignation=new FetchDesignation();
        mFetchDesignation.execute();

    }

    private void setLokSabhaData(Object o)  {
        Log.e(LOG_TAG,"LOK SABHA JSON : "+o.toString());
        try {
            lokSabhaList.clear();
            JSONObject jObject=new JSONObject(o.toString());
            String checkStatus=jObject.getString("status");
            if(checkStatus.equals("1")&& o != null){
                JSONArray lokArray=jObject.getJSONArray("data");
                Log.e(LOG_TAG,"Lok Sabha Element Count "+lokArray.length());
                for(int i =0 ; i<lokArray.length();i++){
                    JSONObject lokObject=lokArray.getJSONObject(i);
                    mLokSabhaModel=new LokSabhaModel();
                    mLokSabhaModel.setConstituencyId(lokObject.getString("ConstituencyId"));
                    mLokSabhaModel.setConstituencyName(lokObject.getString("ConstituencyName"));
                    mLokSabhaModel.setConstituencyReserved(lokObject.getString("ConstituencyReserved"));
                    mLokSabhaModel.setConstituencyState(lokObject.getString("ConstituencyState"));
                    mLokSabhaDetail.add(mLokSabhaModel);
                    lokSabhaList.add(mLokSabhaModel.getConstituencyName());
                }
                mLokSabhaArrayAdapter = new ArrayAdapter(SignupActivity.this, R.layout.item_spinner_textview, lokSabhaList);
                lokSabha.setAdapter(mLokSabhaArrayAdapter);
                lokSabha.setThreshold(1);
                editor.putString(Constants.LOK_SABHA_LIST, new Gson().toJson(mLokSabhaDetail));
                editor.commit();
            }
            else {
                Toast.makeText(this, "Server Connection Error", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,e.toString());
        }
        FetchMunicipalCorporationWard mFetchMunicipalCorporationWard=new FetchMunicipalCorporationWard();
        mFetchMunicipalCorporationWard.execute();

    }



    private void setDesignationData(Object o)  {
        Log.e(LOG_TAG,"DESIGNATION JSON : "+o.toString());
        try {
            designationList.clear();
            JSONObject jObject=new JSONObject(o.toString());
            String checkStatus=jObject.getString("status");
            if(checkStatus.equals("1")&& o != null){
                JSONArray designationArray=jObject.getJSONArray("designation");
                Log.e(LOG_TAG,"DESIGNATION Element Count "+designationArray.length());
                for(int i =0 ; i<designationArray.length();i++){
                    JSONObject designationObject=designationArray.getJSONObject(i);
                    designationList.add(designationObject.getString("DesignationName"));
                }
                mDesignationArrayAdapter = new ArrayAdapter(SignupActivity.this, R.layout.item_spinner_textview, designationList);
                repDesignation.setAdapter(mDesignationArrayAdapter);
                repDesignation.setThreshold(1);
            }
            else {
                Toast.makeText(this, "Server Connection Error", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,e.toString());
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

    class SignUpUser extends AsyncTask {

        String url_user_sign_up = "http://echoindia.co.in/php/signup.php";
        String userNameStr=userName.getText().toString().trim();
        String passwordStr=password.getText().toString().trim();
        String firstNameStr=firstName.getText().toString().trim();
        String lastNameStr = lastName.getText().toString().trim();
        String wardStr=ward.getText().toString().trim();
        String cityStr=city.getText().toString().trim();
        String lokSabhaStr=lokSabha.getText().toString().trim();
        String vidhanSabhaStr=vidhanSabha.getText().toString().trim();
        String pincodeStr=pinCode.getText().toString().trim();
        String stateStr=state.getText().toString().trim();
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
                URL url = new URL(url_user_sign_up);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username",userNameStr);
                postDataParams.put("password",passwordStr);
                postDataParams.put("firstName",firstNameStr);
                postDataParams.put("lastName",lastNameStr);
                postDataParams.put("email",emailStr);
                postDataParams.put("phone",phoneNumberStr);
                postDataParams.put("loksabha",lokSabhaStr);
                postDataParams.put("vidhansabha",vidhanSabhaStr);
                postDataParams.put("ward",wardStr);
                postDataParams.put("city",cityStr);
                postDataParams.put("pincode",pincodeStr);
                postDataParams.put("state",stateStr);
                postDataParams.put("userPhoto",encodedImage);

                Log.e(LOG_TAG,"URL"+url_user_sign_up);
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
                Log.e(LOG_TAG,"Registration JSON: "+o.toString());
                jObject = new JSONObject(o.toString());
                String checkStatus=jObject.getString("status");
                if(checkStatus.equals("1")){
                    Toast.makeText(SignupActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                    Intent intentDocumentUpload=new Intent(SignupActivity.this,DocumentUploadActivity.class);
                    startActivity(intentDocumentUpload);
                    SignupActivity.this.finish();
                }
                else if(checkStatus.equals("0")){
                    Toast.makeText(SignupActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    class SignUpRep extends AsyncTask {

        String url_rep_sign_up = "http://echoindia.co.in/php/repSignup.php";
        String userNameStr=userName.getText().toString().trim();
        String passwordStr=password.getText().toString().trim();
        String firstNameStr=firstName.getText().toString().trim();
        String lastNameStr = lastName.getText().toString().trim();
        String wardStr=ward.getText().toString().trim();
        String cityStr=city.getText().toString().trim();
        String lokSabhaStr=lokSabha.getText().toString().trim();
        String vidhanSabhaStr=vidhanSabha.getText().toString().trim();
        String pincodeStr=pinCode.getText().toString().trim();
        String stateStr=state.getText().toString().trim();
        String phoneNumberStr=phoneNumber.getText().toString().trim();
        String emailStr=emailAddress.getText().toString().trim();


        String repPartyStr=repParty.getText().toString();
        String repDesignationStr=repDesignation.getText().toString();
        String repLocationStr=repLocation.getText().toString();
        String repQualificationStr=repQualification.getText().toString();
        String repHomePageStr=repHomePage.getText().toString();
        String repTwitterStr=repTwitter.getText().toString();





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
                URL url = new URL(url_rep_sign_up);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username",userNameStr);
                postDataParams.put("password",passwordStr);
                postDataParams.put("firstName",firstNameStr);
                postDataParams.put("lastName",lastNameStr);
                postDataParams.put("email",emailStr);
                postDataParams.put("phone",phoneNumberStr);
                postDataParams.put("loksabha",lokSabhaStr);
                postDataParams.put("vidhansabha",vidhanSabhaStr);
                postDataParams.put("ward",wardStr);
                postDataParams.put("city",cityStr);
                postDataParams.put("pincode",pincodeStr);
                postDataParams.put("state",stateStr);
                postDataParams.put("userPhoto",encodedImage);
                postDataParams.put("repParty",repPartyStr);
                postDataParams.put("repDesignation",repDesignationStr);
                postDataParams.put("repLocation",repLocationStr);
                postDataParams.put("repQualification",repQualificationStr);
                postDataParams.put("repHomePage",repHomePageStr);
                postDataParams.put("repTwitter",repTwitterStr);

                Log.e(LOG_TAG,"URL"+url_rep_sign_up);
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
                Log.e(LOG_TAG,"Registration JSON: "+o.toString());
                jObject = new JSONObject(o.toString());
                String checkStatus=jObject.getString("status");
                if(checkStatus.equals("1")){
                    Toast.makeText(SignupActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                    Intent intentDocumentUpload=new Intent(SignupActivity.this,DocumentUploadActivity.class);
                    startActivity(intentDocumentUpload);
                    SignupActivity.this.finish();
                }
                else if(checkStatus.equals("0")){
                    Toast.makeText(SignupActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class FetchVidhanSabha extends AsyncTask {

        String url_vidhan_sabha = "http://echoindia.co.in/php/vidhansabha.php";


        @Override
        protected Object doInBackground(Object[] params) {

            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(url_vidhan_sabha);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("state",sharedpreferences.getString(Constants.CURRENT_STATE,""));
                Log.e(LOG_TAG,"URL"+url_vidhan_sabha);
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
            setVidhanData(o);
        }
    }

    class FetchMunicipalCorporationWard extends AsyncTask {

        String url_ward = "http://echoindia.co.in/php/ward.php";


        @Override
        protected Object doInBackground(Object[] params) {

            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(url_ward);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("city",sharedpreferences.getString(Constants.CURRENT_CITY,""));
                Log.e(LOG_TAG,"URL"+url_ward);
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
            setMunicipalCorporationWardData(o);
        }
    }

    class FetchLokSabha extends AsyncTask {

        String url_lok_sabha = "http://echoindia.co.in/php/loksabha.php";


        @Override
        protected Object doInBackground(Object[] params) {

            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(url_lok_sabha);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("state",sharedpreferences.getString(Constants.CURRENT_STATE,""));
                Log.e(LOG_TAG,"URL"+url_lok_sabha);
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
            setLokSabhaData(o);
        }
    }



    class FetchDesignation extends AsyncTask {

        String url_designation = "http://echoindia.co.in/php/getDesignation.php";


        @Override
        protected Object doInBackground(Object[] params) {

            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(url_designation);

                Log.e(LOG_TAG,"URL"+url_designation);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8"));
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
            setDesignationData(o);
        }
    }


}
