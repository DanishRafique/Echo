package in.co.echoindia.echo.User;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import in.co.echoindia.echo.Model.MyItem;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.Clustering.ClusterManager;

public class DevelopmentActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ClusterManager<MyItem> mClusterManager;

    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_development);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_development);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the deviced, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        boolean success = googleMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_json)));
        LatLng kolkata = new LatLng(51.5584087,-0.0915169);
        CameraUpdate zoom= CameraUpdateFactory.zoomTo(13);
        //mMap.addMarker(new MarkerOptions().position(kolkata).title("Marker in Kolkata"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(kolkata));
        mMap.animateCamera(zoom);

        mClusterManager = new ClusterManager<MyItem>(this, mMap);

        mMap.setOnCameraIdleListener(mClusterManager);
        try {
            readItems();
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show();
        }
    }

    private void readItems() throws JSONException {
        InputStream inputStream = getResources().openRawResource(R.raw.radar_search);
        List<MyItem> items = new MyItemReader().read(inputStream);
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            for (MyItem item : items) {
                LatLng position = item.getPosition();
                double lat = position.latitude + offset;
                double lng = position.longitude + offset;
                MyItem offsetItem = new MyItem(lat, lng);
                mClusterManager.addItem(offsetItem);
            }
        }
    }

    public class MyItemReader {

        /*
         * This matches only once in whole input,
         * so Scanner.next returns whole InputStream as a String.
         * http://stackoverflow.com/a/5445161/2183804
         */
        private static final String REGEX_INPUT_BOUNDARY_BEGINNING = "\\A";

        public List<MyItem> read(InputStream inputStream) throws JSONException {
            List<MyItem> items = new ArrayList<MyItem>();
            String json = new Scanner(inputStream).useDelimiter(REGEX_INPUT_BOUNDARY_BEGINNING).next();
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                String title = null;
                String snippet = null;
                JSONObject object = array.getJSONObject(i);
                double lat = object.getDouble("lat");
                double lng = object.getDouble("lng");
                if (!object.isNull("title")) {
                    title = object.getString("title");
                }
                if (!object.isNull("snippet")) {
                    snippet = object.getString("snippet");
                }
                items.add(new MyItem(lat, lng, title, snippet));
            }
            return items;
        }


    }
    }

