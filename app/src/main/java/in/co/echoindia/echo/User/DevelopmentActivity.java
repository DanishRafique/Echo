package in.co.echoindia.echo.User;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import in.co.echoindia.echo.Model.DevelopmentDetailsModel;
import in.co.echoindia.echo.Model.DevelopmentInProgressModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Clustering.ClusterManager;
import in.co.echoindia.echo.Utils.Constants;

import static in.co.echoindia.echo.R.id.map;

public class DevelopmentActivity extends AppCompatActivity  implements
        GoogleMap.OnInfoWindowClickListener,
        OnMapReadyCallback {
    private ClusterManager<DevelopmentDetailsModel> mClusterManager;
    private GoogleMap mMap;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private static final String LOG_TAG = "DevelopmentActivity";
    TextView mapUpdate,mapSnippet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_development);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_development);
        sharedpreferences = AppUtil.getAppPreferences(this);
        editor = sharedpreferences.edit();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        setSupportActionBar(toolbar);
        mapUpdate=(TextView)findViewById(R.id.map_title);
        mapSnippet=(TextView)findViewById(R.id.map_snippet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        boolean success = googleMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_json_echo)));
        LatLng currentLocation = new LatLng(Double.parseDouble(sharedpreferences.getString(Constants.MY_LATITUDE,"")),Double.parseDouble(sharedpreferences.getString(Constants.MY_LONGITUDE,"")));
        CameraUpdate zoom= CameraUpdateFactory.zoomTo(13);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        mMap.animateCamera(zoom);
        mClusterManager = new ClusterManager<DevelopmentDetailsModel>(this, mMap);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(this);


        mMap.setOnMarkerClickListener((new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.getTitle()!=null) {
                    mapUpdate.setText(marker.getTitle().trim());
                }
                else{
                    mapUpdate.setText("");
                }
                if(marker.getSnippet()!=null){
                    mapSnippet.setText(marker.getSnippet().trim());
                }
                else{
                    mapSnippet.setText("");
                }
                return false;
            }

        }));
        mMap.setOnMapClickListener((new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mapUpdate.setText("Click on the Marker to know the detail");
                mapSnippet.setText("");
            }
        }));
        try {
            readItems();
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show();
        }
    }


    private void readItems() throws JSONException {
        Type type = new TypeToken<ArrayList<DevelopmentInProgressModel>>() {}.getType();
        ArrayList<DevelopmentInProgressModel> development= new Gson().fromJson(sharedpreferences.getString(Constants.DEVELOPMENT_IN_PROGRESS, ""), type);
            for (int i=0;i<development.size();i++) {
                double lat = Double.parseDouble(development.get(i).getDevelopmentLat());
                double lng =  Double.parseDouble(development.get(i).getDevelopmentLong());
                DevelopmentDetailsModel offsetItem = new DevelopmentDetailsModel(lat, lng ,development.get(i).getDevelopmentTitle(),development.get(i).getDevelopmentSnippet() );
                mClusterManager.addItem(offsetItem);
        }
    }



    @Override
    public void onInfoWindowClick(Marker marker) {
        /*Toast.makeText(this, marker.getTitle() + marker.getSnippet(), Toast.LENGTH_SHORT).show();
        Log.e(LOG_TAG,marker.getTitle() + marker.getSnippet());*/
        if(marker.getTitle()!=null) {
            mapUpdate.setText(marker.getTitle().trim());
        }
        else{
            mapUpdate.setText("");
        }
        if(marker.getSnippet()!=null){
            mapSnippet.setText(marker.getSnippet().trim());
        }
        else{
            mapSnippet.setText("");
        }
    }
}

