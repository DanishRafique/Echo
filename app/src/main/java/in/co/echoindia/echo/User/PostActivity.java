package in.co.echoindia.echo.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import java.util.List;

import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;
import in.co.echoindia.echo.Utils.GifSizeFilter;

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.e(LOG_TAG, "Place: " + place.getName());
                Log.e(LOG_TAG," Address: "+place.getAddress());
                echoPostLocation.setText(place.getName());
                echoPostLocation.setTextColor(Color.parseColor("#095f86"));
                echoPostLocation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_blue, 0, 0, 0);
                //echoPostLocationSelection.setVisibility(View.VISIBLE);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e(LOG_TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }

        }
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            Log.e(LOG_TAG, "mSelected: " + mSelected);
            echoPostPhoto.setText(mSelected.size()+" Photos");
            echoPostPhoto.setTextColor(Color.parseColor("#095f86"));
            echoPostPhoto.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_camera_blue, 0, 0, 0);
            //echoPostPhotoSelection.setVisibility(View.VISIBLE);
        }
    }
}
