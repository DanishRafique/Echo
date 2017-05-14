package in.co.echoindia.echo.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;
import in.co.echoindia.echo.Model.RepDetailModel;
import in.co.echoindia.echo.Model.UserDetailsModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

public class MyAccountActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    UserDetailsModel userDetailsModel;
    RepDetailModel repDetailModel;

    TextView tvName,tvUserName,tvEmail,tvMobile,tvAddress, tvCity , tvWard , tvPinCode ,tvDistrict , tvState;
    TextView tvLokSabha,tvVidhanSabha;

    TextView tvParty,tvDesignation,tvLocation,tvQualification,tvWebsite,tvTwitter;

    CircleImageView userPhoto;
   // LinearLayout profileBackground;

    LinearLayout llLokSabha,llVidhanSabha;

    CardView myAccountPolitical;
    TextView editDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_my_account);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedpreferences = AppUtil.getAppPreferences(this);
        editor = sharedpreferences.edit();

        editDetails=(TextView)findViewById(R.id.my_account_edit);


        tvName=(TextView)findViewById(R.id.tv_name);
        tvUserName=(TextView)findViewById(R.id.tv_user_name);
        tvEmail=(TextView)findViewById(R.id.tv_email);
        tvMobile=(TextView)findViewById(R.id.tv_mobile);
        tvCity=(TextView)findViewById(R.id.tv_city);
        tvWard=(TextView)findViewById(R.id.tv_ward);
        tvPinCode=(TextView)findViewById(R.id.tv_pin_code);
        tvLokSabha=(TextView)findViewById(R.id.tv_lok_sabha);
        tvVidhanSabha=(TextView)findViewById(R.id.tv_vidhan_sabha);
        tvState=(TextView)findViewById(R.id.tv_state);
        userPhoto=(CircleImageView)findViewById(R.id.my_account_img_profile);
        //profileBackground=(LinearLayout)findViewById(R.id.profile_background);
        llLokSabha=(LinearLayout)findViewById(R.id.ll_lok_sabha);
        llVidhanSabha=(LinearLayout)findViewById(R.id.ll_vidhan_sabha);
        myAccountPolitical=(CardView)findViewById(R.id.my_account_political);

        tvParty=(TextView)findViewById(R.id.rep_party);
        tvDesignation=(TextView)findViewById(R.id.rep_designation);
        tvLocation=(TextView)findViewById(R.id.tv_rep_location);
        tvQualification=(TextView)findViewById(R.id.tv_rep_qualification);
        tvWebsite=(TextView)findViewById(R.id.tv_rep_home_page);
        tvTwitter=(TextView)findViewById(R.id.tv_rep_twitter);

        if(sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_TYPE,"").equals("USER")) {
            myAccountPolitical.setVisibility(View.GONE);
            userDetailsModel = new Gson().fromJson(sharedpreferences.getString(Constants.SETTINGS_OBJ_USER, ""), UserDetailsModel.class);
            tvName.setText(userDetailsModel.getFirstName() + " " + userDetailsModel.getLastName());
            tvUserName.setText(userDetailsModel.getUserName());
            tvEmail.setText(userDetailsModel.getEmailId());
            tvMobile.setText(userDetailsModel.getPhoneNo());
            if (!userDetailsModel.getLokSabha().equals("")) {
                tvLokSabha.setText(userDetailsModel.getLokSabha());
            } else {
                llLokSabha.setVisibility(View.GONE);
            }
            tvCity.setText(userDetailsModel.getCity());
            tvWard.setText(userDetailsModel.getWard());
            tvPinCode.setText(userDetailsModel.getPinCode());
            if (!userDetailsModel.getVidhanSabha().equals("")) {
                tvVidhanSabha.setText(userDetailsModel.getVidhanSabha());
            } else {
                llVidhanSabha.setVisibility(View.GONE);
            }
            tvState.setText(userDetailsModel.getState());
           Glide.with(this).load(userDetailsModel.getUserPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).into(userPhoto);
           /* Glide.with(this).load(userDetailsModel.getUserPhoto()).asBitmap().into(new SimpleTarget<Bitmap>(500, 200) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    Drawable drawable = new BitmapDrawable(resource);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        profileBackground.setBackground(drawable);
                    }
                }
            });*/
        }
        else if(sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_TYPE,"").equals("REP")) {

            repDetailModel = new Gson().fromJson(sharedpreferences.getString(Constants.SETTINGS_OBJ_USER, ""), RepDetailModel.class);
            tvName.setText(repDetailModel.getFirstName() + " " + repDetailModel.getLastName());
            tvUserName.setText(repDetailModel.getRepName());
            tvEmail.setText(repDetailModel.getEmailId());
            tvMobile.setText(repDetailModel.getPhoneNo());
            if (!repDetailModel.getLokSabha().equals("")) {
                tvLokSabha.setText(repDetailModel.getLokSabha());
            } else {
                llLokSabha.setVisibility(View.GONE);
            }
            tvCity.setText(repDetailModel.getCity());
            tvWard.setText(repDetailModel.getWard());
            tvPinCode.setText(repDetailModel.getPinCode());
            if (!repDetailModel.getVidhanSabha().equals("")) {
                tvVidhanSabha.setText(repDetailModel.getVidhanSabha());
            } else {
                llVidhanSabha.setVisibility(View.GONE);
            }
            tvState.setText(repDetailModel.getState());
            Glide.with(this).load(repDetailModel.getUserPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).into(userPhoto);
           /* Glide.with(this).load(repDetailModel.getUserPhoto()).asBitmap().into(new SimpleTarget<Bitmap>(500, 200) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    Drawable drawable = new BitmapDrawable(resource);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        profileBackground.setBackground(drawable);
                    }
                }
            });*/

            tvParty.setText(repDetailModel.getRepParty());
            tvDesignation.setText(repDetailModel.getRepDesignation());
            tvLocation.setText(repDetailModel.getRepLocation());
            tvQualification.setText(repDetailModel.getRepQualification());
            tvWebsite.setText(repDetailModel.getRepHomePage());
            tvTwitter.setText(repDetailModel.getRepTwitter());
        }

        editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent=new Intent(MyAccountActivity.this, EditAccountActivity.class);
                startActivity(editIntent);
            }
        });

    }
}
