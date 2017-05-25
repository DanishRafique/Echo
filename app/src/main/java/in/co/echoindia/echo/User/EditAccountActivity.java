package in.co.echoindia.echo.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;
import in.co.echoindia.echo.Model.RepDetailModel;
import in.co.echoindia.echo.Model.UserDetailsModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

public class EditAccountActivity extends AppCompatActivity {

    TextView changePasswordTextView;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    UserDetailsModel userDetailsModel;
    RepDetailModel repDetailModel;

    EditText firstName,lastName, emailAddress,phoneNumber,city , pinCode  , state;
    LinearLayout llUserInformation , llUserAddress,llUserPolitic;

    Button btnUpdateDetails;
    CircleImageView imgProfile;
    AutoCompleteTextView lokSabha,vidhanSabha , ward;
    AutoCompleteTextView repParty,repDesignation;
    EditText repLocation,repQualification,repHomePage,repTwitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        sharedpreferences = AppUtil.getAppPreferences(this);
        editor = sharedpreferences.edit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        changePasswordTextView=(TextView)findViewById(R.id.edit_account_change_password);

        imgProfile=(CircleImageView)findViewById(R.id.edit_detail_img_profile);

        firstName=(EditText)findViewById(R.id.edit_detail_first_name);
        lastName=(EditText)findViewById(R.id.edit_detail_last_name);
        emailAddress=(EditText)findViewById(R.id.edit_detail_email_id);
        phoneNumber=(EditText)findViewById(R.id.edit_detail_phone_number);

        repParty=(AutoCompleteTextView)findViewById(R.id.edit_detail_rep_party);
        repDesignation=(AutoCompleteTextView)findViewById(R.id.edit_detail_rep_designation);
        repLocation=(EditText)findViewById(R.id.edit_detail_rep_location);
        repQualification=(EditText)findViewById(R.id.edit_detail_rep_qualification);
        repHomePage=(EditText)findViewById(R.id.edit_detail_home_page);
        repTwitter=(EditText)findViewById(R.id.edit_detail_twitter);

        state=(EditText)findViewById(R.id.edit_detail_state);
        lokSabha=(AutoCompleteTextView)findViewById(R.id.edit_detail_lok_sabha);
        vidhanSabha=(AutoCompleteTextView)findViewById(R.id.edit_detail_vidhan_sabha);
        city=(EditText)findViewById(R.id.edit_detail_city);
        ward=(AutoCompleteTextView) findViewById(R.id.edit_detail_ward);
        pinCode=(EditText)findViewById(R.id.edit_detail_pin_code);

        llUserPolitic=(LinearLayout)findViewById(R.id.edit_detail_political_career);
        btnUpdateDetails=(Button)findViewById(R.id.edit_detail_update);

        btnUpdateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(EditAccountActivity.this, "Your Account Detail has been Updated", Toast.LENGTH_SHORT).show();
                if(sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_TYPE,"").equals("USER")){
                    getUserData();
                }
                else{
                    getRepData();
                }
                EditAccountActivity.this.finish();
            }
        });






        changePasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changePasswordIntent=new Intent(EditAccountActivity.this, ChangePasswordActivity.class);
                startActivity(changePasswordIntent);
            }
        });

        if(sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_TYPE,"").equals("USER")){
            userDetailsModel=new Gson().fromJson(sharedpreferences.getString(Constants.SETTINGS_OBJ_USER, ""), UserDetailsModel.class);
            setUserData(userDetailsModel);
        }
        else if(sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_TYPE,"").equals("REP")){
            repDetailModel=new Gson().fromJson(sharedpreferences.getString(Constants.SETTINGS_OBJ_USER, ""), RepDetailModel.class);
            setRepData(repDetailModel);
        }

    }

    void setUserData(UserDetailsModel userDetailsModel){

        llUserPolitic.setVisibility(View.GONE);

        Glide.with(this).load(userDetailsModel.getUserPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgProfile);

        firstName.setText(userDetailsModel.getFirstName());
        lastName.setText(userDetailsModel.getLastName());
        emailAddress.setText(userDetailsModel.getEmailId());
        phoneNumber.setText(userDetailsModel.getPhoneNo());

        state.setText(userDetailsModel.getState());
        lokSabha.setText(userDetailsModel.getLokSabha());
        vidhanSabha.setText(userDetailsModel.getVidhanSabha());
        city.setText(userDetailsModel.getCity());
        ward.setText(userDetailsModel.getWard());
        pinCode.setText(userDetailsModel.getPinCode());


    }

    void setRepData(RepDetailModel repDetailModel){

        Glide.with(this).load(repDetailModel.getUserPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgProfile);

        firstName.setText(repDetailModel.getFirstName());
        lastName.setText(repDetailModel.getLastName());
        emailAddress.setText(repDetailModel.getEmailId());
        phoneNumber.setText(repDetailModel.getPhoneNo());

        repParty.setText(repDetailModel.getRepParty());
        repDesignation.setText(repDetailModel.getRepDesignation());
        repQualification.setText(repDetailModel.getRepQualification());
        repLocation.setText(repDetailModel.getRepLocation());
        repHomePage.setText(repDetailModel.getRepHomePage());
        repTwitter.setText(repDetailModel.getRepTwitter());

        state.setText(repDetailModel.getState());
        lokSabha.setText(repDetailModel.getLokSabha());
        vidhanSabha.setText(repDetailModel.getVidhanSabha());
        city.setText(repDetailModel.getCity());
        ward.setText(repDetailModel.getWard());
        pinCode.setText(repDetailModel.getPinCode());

    }

    void getUserData(){
        UserDetailsModel userModel=new UserDetailsModel();
        userModel.setUserName(userDetailsModel.getUserName());
        userModel.setUserPhoto(userDetailsModel.getUserPhoto());
        userModel.setFirstName(firstName.getText().toString());
        userModel.setLastName(lastName.getText().toString());
        userModel.setEmailId(emailAddress.getText().toString());
        userModel.setPhoneNo(phoneNumber.getText().toString());
        userModel.setState(state.getText().toString());
        userModel.setLokSabha(lokSabha.getText().toString());
        userModel.setVidhanSabha(vidhanSabha.getText().toString());
        userModel.setCity(city.getText().toString());
        userModel.setWard(ward.getText().toString());
        userModel.setPinCode(pinCode.getText().toString());
        editor.putString(Constants.SETTINGS_OBJ_USER, new Gson().toJson(userModel));
        editor.commit();
    }

    void getRepData(){
        RepDetailModel userModel=new RepDetailModel();
        userModel.setRepName(repDetailModel.getRepName());
        userModel.setUserPhoto(repDetailModel.getUserPhoto());
        userModel.setFirstName(firstName.getText().toString());
        userModel.setLastName(lastName.getText().toString());
        userModel.setEmailId(emailAddress.getText().toString());
        userModel.setPhoneNo(phoneNumber.getText().toString());
        userModel.setState(state.getText().toString());
        userModel.setLokSabha(lokSabha.getText().toString());
        userModel.setVidhanSabha(vidhanSabha.getText().toString());
        userModel.setCity(city.getText().toString());
        userModel.setWard(ward.getText().toString());
        userModel.setPinCode(pinCode.getText().toString());


        userModel.setRepParty(repParty.getText().toString());
        userModel.setRepDesignation(repDesignation.getText().toString());
        userModel.setRepQualification(repQualification.getText().toString());
        userModel.setRepLocation(repLocation.getText().toString());
        userModel.setRepHomePage(repHomePage.getText().toString());
        userModel.setRepTwitter(repTwitter.getText().toString());








        editor.putString(Constants.SETTINGS_OBJ_USER, new Gson().toJson(userModel));
        editor.commit();
    }

}
