package in.co.echoindia.echo.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;
import in.co.echoindia.echo.Model.RepDetailModel;
import in.co.echoindia.echo.Model.UserDetailsModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;
import in.co.echoindia.echo.Utils.GifSizeFilter;
import in.co.echoindia.echo.Utils.MarshMallowPermission;

public class PostActivity extends AppCompatActivity {
    EditText echoPostEdit;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    int REQUEST_CODE_CHOOSE=2;
    private static final String LOG_TAG = "PostActivity";
    TextView echoPostLocation;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    List<Uri> mSelected;
    TextView echoPostPhoto;
    TextView echoPostPhotoSelection,echoPostLocationSelection;
    LinearLayout echoPostImagell;
    byte[] byteArray;
    ImageView echoPostImage1,echoPostImage2,echoPostImage3,echoPostImage4,echoPostImage5;
    String encodedImage;
    ArrayList<String> encodedImageList=new ArrayList<String>();
    ImageView imageViewSet;
    Button btnEchoPost;
    String location;

    CircleImageView echoPostUserImage;
    TextView echoPostName,echoPostUserName;

    UserDetailsModel mUserDetail;
    RepDetailModel mRepDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        echoPostEdit=(EditText)findViewById(R.id.echo_post_edit);
        sharedpreferences = AppUtil.getAppPreferences(this);
        editor = sharedpreferences.edit();
        final double latitude=Double.parseDouble(sharedpreferences.getString(Constants.MY_LATITUDE,""));
        final double longitude=Double.parseDouble(sharedpreferences.getString(Constants.MY_LONGITUDE,""));

        echoPostUserImage=(CircleImageView)findViewById(R.id.echo_post_user_image);
        echoPostName=(TextView)findViewById(R.id.echo_post_name);
        echoPostUserName=(TextView)findViewById(R.id.echo_post_username);

        if(sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_TYPE,"").equals("USER")){
            mUserDetail= new Gson().fromJson(sharedpreferences.getString(Constants.SETTINGS_OBJ_USER, ""), UserDetailsModel.class);
            Glide.with(this).load(mUserDetail.getUserPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).into(echoPostUserImage);
            echoPostUserName.setText(mUserDetail.getUserName());
            echoPostName.setText(mUserDetail.getFirstName()+" "+mUserDetail.getLastName());
        }
        else if(sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_TYPE,"").equals("REP")){
            mRepDetail= new Gson().fromJson(sharedpreferences.getString(Constants.SETTINGS_OBJ_USER, ""), RepDetailModel.class);
            Glide.with(this).load(mRepDetail.getUserPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).into(echoPostUserImage);
            echoPostUserName.setText(mRepDetail.getRepName());
            echoPostName.setText(mRepDetail.getFirstName()+" "+mRepDetail.getLastName());
        }



        echoPostLocation=(TextView)findViewById(R.id.echo_post_location);
        echoPostLocationSelection=(TextView)findViewById(R.id.echo_post_location_selection);
        echoPostLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .setBoundsBias(new LatLngBounds(new LatLng(latitude,longitude),new LatLng(latitude,longitude)))
                                    .build(PostActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
        echoPostPhoto=(TextView)findViewById(R.id.echo_post_photo);

        echoPostPhotoSelection=(TextView)findViewById(R.id.echo_post_photo_selection);



        echoPostPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MarshMallowPermission marshMallowPermission = new MarshMallowPermission(PostActivity.this);
                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                    marshMallowPermission.requestPermissionForExternalStorage();
                }
                Matisse.from(PostActivity.this)
                        .choose(MimeType.allOf())
                        .countable(true)
                        .maxSelectable(5)
                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });

        echoPostImagell=(LinearLayout) findViewById(R.id.echo_post_image_ll);
        echoPostImage1=(ImageView)findViewById(R.id.echo_post_image_1);
        echoPostImage2=(ImageView)findViewById(R.id.echo_post_image_2);
        echoPostImage3=(ImageView)findViewById(R.id.echo_post_image_3);
        echoPostImage4=(ImageView)findViewById(R.id.echo_post_image_4);
        echoPostImage5=(ImageView)findViewById(R.id.echo_post_image_5);




        btnEchoPost=(Button)findViewById(R.id.btn_echo_post);

        btnEchoPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertPost mInsertPost=new InsertPost(echoPostEdit.getText().toString());
                mInsertPost.execute();
            }
        });

        if(echoPostEdit==null && encodedImageList.size()==0){
            btnEchoPost.setEnabled(false);
        }
        else{
            btnEchoPost.setEnabled(true);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.e(LOG_TAG, "Place: " + place.getName());
                Log.e(LOG_TAG," Address: "+place.getAddress());
                echoPostLocation.setText(place.getName());
                location=place.getName()+" ,"+sharedpreferences.getString(Constants.CURRENT_CITY,"");
                echoPostLocation.setTextColor(Color.parseColor("#095f86"));
                echoPostLocation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_blue, 0, 0, 0);
                //echoPostLocationSelection.setVisibility(View.VISIBLE);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                location=null;
                // TODO: Handle the error.
                Log.e(LOG_TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                location=null;
            }

        }
        else if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            encodedImageList.clear();
            echoPostImage1.setVisibility(View.GONE);
            echoPostImage2.setVisibility(View.GONE);
            echoPostImage3.setVisibility(View.GONE);
            echoPostImage4.setVisibility(View.GONE);
            echoPostImage5.setVisibility(View.GONE);
            Bitmap originBitmap = null;
            mSelected = Matisse.obtainResult(data);
            Log.e(LOG_TAG, "mSelected: " + mSelected);
            if(mSelected.size()==1){
                echoPostPhoto.setText(mSelected.size() + " Photo");
            }
            else {
                echoPostPhoto.setText(mSelected.size() + " Photos");
            }
            echoPostPhoto.setTextColor(Color.parseColor("#095f86"));
            echoPostPhoto.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_camera_blue, 0, 0, 0);
            //echoPostPhotoSelection.setVisibility(View.VISIBLE);

            for(int i=0;i<mSelected.size();i++) {
                InputStream imageStream;
                if(i==0)
                {
                    imageViewSet=echoPostImage1;
                }
                else if(i==1) {
                    imageViewSet=echoPostImage2;
                }
                else if(i==2) {
                    imageViewSet=echoPostImage3;
                }
                else if(i==3) {
                    imageViewSet=echoPostImage4;
                }
                else if(i==4) {
                    imageViewSet=echoPostImage5;
                }

                try {
                    imageStream = getContentResolver().openInputStream(mSelected.get(i));
                    originBitmap = BitmapFactory.decodeStream(imageStream);
                } catch (FileNotFoundException e) {
                    Log.e("CatchActivityResult", e.getMessage().toString());

                }
                if (originBitmap != null) {
                    this.imageViewSet.setImageBitmap(originBitmap);
                    Log.e("ImageLoaded", "Done Loading Image");
                    try {
                        echoPostImagell.setVisibility(View.VISIBLE);
                        imageViewSet.setVisibility(View.VISIBLE);
                        Bitmap image = ((BitmapDrawable) imageViewSet.getDrawable()).getBitmap();
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        byteArray = byteArrayOutputStream.toByteArray();
                        Log.e("byteArray", byteArray.toString());
                        encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        encodedImageList.add(encodedImage);
                    } catch (Exception e) {
                        Log.e("OnActivityResultEx", e.toString());
                    }
                }
            }
        }
        else{
            encodedImageList.clear();
            mSelected.clear();
            echoPostImagell.setVisibility(View.GONE);
            echoPostPhoto.setText("Add Photo");
            echoPostPhoto.setTextColor(Color.parseColor("#000000"));
            echoPostPhoto.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_camera, 0, 0, 0);
        }

        for(int i=0;i<mSelected.size();i++){

            Log.e(LOG_TAG,"Image "+i+" : "+mSelected.get(i)+" "+encodedImageList.get(i));
        }
    }



    class InsertPost extends AsyncTask {

        String postText;

        String url_insert_post = "http://echoindia.co.in/php/insertPost.php";
        public InsertPost(String postText){
            this.postText=postText;
        }


        @Override
        protected Object doInBackground(Object[] params) {

            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(url_insert_post);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username",sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_USER_CODE,""));
                postDataParams.put("postText",postText);
                Date currentDate = new Date();
                String dateToday = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String timeNow = sdf.format(new Date());
                postDataParams.put("time",timeNow);
                postDataParams.put("date",dateToday);
                if(sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_TYPE,"").equals("REP")) {
                    postDataParams.put("postType","BUZZ");
                }
                else if(sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_TYPE,"").equals("USER")) {
                    postDataParams.put("postType","USER");
                }
                for(int i=0;i<encodedImageList.size();i++){
                    if(i==0){
                        postDataParams.put("img1",encodedImageList.get(i));
                    }
                    else if(i==1){
                        postDataParams.put("img2",encodedImageList.get(i));
                    }
                    else if(i==2){
                        postDataParams.put("img3",encodedImageList.get(i));
                    }
                    else if(i==3){
                        postDataParams.put("img4",encodedImageList.get(i));
                    }
                    else if(i==4){
                        postDataParams.put("img5",encodedImageList.get(i));
                    }
                }
                if(location!=null){
                    postDataParams.put("location",location);
                }


                Log.e(LOG_TAG,"URL"+url_insert_post);
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
            Log.e(LOG_TAG,"Insert Post : "+o.toString());
            finish();

        }
    }

}
