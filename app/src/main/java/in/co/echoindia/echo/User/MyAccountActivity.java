package in.co.echoindia.echo.User;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;
import in.co.echoindia.echo.Model.UserDetailsModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

public class MyAccountActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    UserDetailsModel userDetailsModel;

    TextView tvName,tvUserName,tvEmail,tvMobile,tvAddress, tvCity , tvWard , tvPinCode ,tvDistrict , tvState;
    TextView tvLokSabha,tvVidhanSabha;

    CircleImageView userPhoto;
    LinearLayout profileBackground;

    LinearLayout llLokSabha,llVidhanSabha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_my_account);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedpreferences = AppUtil.getAppPreferences(this);
        editor = sharedpreferences.edit();
        userDetailsModel = new Gson().fromJson(sharedpreferences.getString(Constants.SETTINGS_OBJ_USER, ""), UserDetailsModel.class);

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
        userPhoto=(CircleImageView)findViewById(R.id.user_photo);
        profileBackground=(LinearLayout)findViewById(R.id.profile_background);
        llLokSabha=(LinearLayout)findViewById(R.id.ll_lok_sabha);
        llVidhanSabha=(LinearLayout)findViewById(R.id.ll_vidhan_sabha);

        tvName.setText(userDetailsModel.getFirstName()+" "+userDetailsModel.getLastName());
        tvUserName.setText(userDetailsModel.getUserName());
        tvEmail.setText(userDetailsModel.getEmailId());
        tvMobile.setText(userDetailsModel.getPhoneNo());
        if(!userDetailsModel.getLokSabha().equals("")) {
            tvLokSabha.setText(userDetailsModel.getLokSabha());
        }
        else{
            llLokSabha.setVisibility(View.GONE);
        }
        tvCity.setText(userDetailsModel.getCity());
        tvWard.setText(userDetailsModel.getWard());
        tvPinCode.setText(userDetailsModel.getPinCode());
        if(!userDetailsModel.getVidhanSabha().equals("")) {
            tvVidhanSabha.setText(userDetailsModel.getVidhanSabha());
        }
        else{
            llVidhanSabha.setVisibility(View.GONE);
        }
        tvState.setText(userDetailsModel.getState());

        Glide.with(this).load("http://www.filmibeat.com/img/220x90x275/popcorn/profile_photos/hrithik-roshan-20150105095244-131.jpg").diskCacheStrategy(DiskCacheStrategy.ALL).into(userPhoto);

        Glide.with(this).load("http://www.filmibeat.com/img/220x90x275/popcorn/profile_photos/hrithik-roshan-20150105095244-131.jpg").asBitmap().into(new SimpleTarget<Bitmap>(500, 200) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    profileBackground.setBackground(drawable);
                }
            }
        });



    }
}
