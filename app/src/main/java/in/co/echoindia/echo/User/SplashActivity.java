package in.co.echoindia.echo.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import in.co.echoindia.echo.HomePage.HomePageActivity;
import in.co.echoindia.echo.Model.NewsDetailsModel;
import in.co.echoindia.echo.Model.PoliticalPartyModel;
import in.co.echoindia.echo.Model.PollDetailsModel;
import in.co.echoindia.echo.Model.PostDetailModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;
import in.co.echoindia.echo.Utils.GPSTracker;
import in.co.echoindia.echo.Utils.PrefManager;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {

    private static final String LOG_TAG = "SplashActivity";
    private static final int REQUEST_PERMISSIONS = 100;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    NewsDetailsModel mNewsDetailModel;
    PollDetailsModel mPollDetailModel;
    PostDetailModel mPostDetailModel;
    ArrayList<NewsDetailsModel> newsList=new ArrayList<NewsDetailsModel>();
    ArrayList<PollDetailsModel> pollList=new ArrayList<PollDetailsModel>();
    ArrayList<NewsDetailsModel> newsListArray = new ArrayList<>();
    ArrayList<PostDetailModel> homeList = new ArrayList<>();
    ArrayList<PostDetailModel> buzzList = new ArrayList<>();
    ArrayList<PoliticalPartyModel> politicalPartyList=new ArrayList<>();
    PoliticalPartyModel politicalPartyModel;
    Double latitude = 0.0, longitude = 0.0;
    Geocoder geocoder;
    String currentLocationAddress = "";
    boolean boolean_permission;
    List<Address> addresses;
    GPSTracker gps;
    private PrefManager prefManager;
    private View mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        mContentView = findViewById(R.id.fullscreen_content);
        sharedpreferences = AppUtil.getAppPreferences(this);
        editor = sharedpreferences.edit();


        Intent intent = new Intent(getApplicationContext(), GPSTracker.class);
        this.startService(intent);


        geocoder = new Geocoder(this, Locale.getDefault());

        fn_permission();
        gps = new GPSTracker(SplashActivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            LatLng mLatLng = new LatLng(latitude, longitude);
            editor.putString(Constants.MY_LATITUDE,String.valueOf(latitude));
            editor.putString(Constants.MY_LONGITUDE,String.valueOf(longitude));
            editor.commit();
            String errorMessage = "";
            geocoder = new Geocoder(SplashActivity.this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException ioException) {
                errorMessage = "Service Not Available " + ioException.toString();
                Log.e("My Test", errorMessage, ioException);
            } catch (IllegalArgumentException illegalArgumentException) {
                errorMessage = "Invalid Latitude or Longitude Used";
                Log.e("My test ", errorMessage + ". " + "Latitude = " + latitude + ", Longitude = " + longitude, illegalArgumentException);
            }

            if (addresses != null && addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                currentLocationAddress = address + "," + city + "," + state + "," + country + "," + postalCode;
                editor.putString(Constants.CURRENT_CITY,city);
                editor.putString(Constants.CURRENT_STATE,state);
                editor.putString(Constants.CURRENT_PIN_CODE,postalCode);
                editor.commit();
            }
            Log.e(LOG_TAG,"My Test currentLocationAddress " + currentLocationAddress);
        }else{
            gps.showSettingsAlert();
        }


        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        FetchNews mFetchNews =new FetchNews();
        mFetchNews.execute();

    }
    @Override
    protected void onResume() {
        super.onResume();


    }

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION))) {


            } else {
                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION

                        },
                        REQUEST_PERMISSIONS);


            }
        } else {

            boolean_permission = true;
        }

    }

    void gotoNextActivity(){
        Boolean firstTime=sharedpreferences.getBoolean(Constants.IS_FIRST_TIME_LAUNCH,false);
        Log.e(LOG_TAG," "+firstTime);
        if(sharedpreferences.getBoolean(Constants.SETTINGS_IS_LOGGED,false)) {
            Intent intentHomePage = new Intent(SplashActivity.this, HomePageActivity.class);
            Toast.makeText(SplashActivity.this, "Welcome "+sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_USER_CODE,""), Toast.LENGTH_SHORT).show();
            startActivity(intentHomePage);

        }
        else if(firstTime!=null && firstTime==true) {
            Intent intentLogin = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intentLogin);
        }
        else{
            Intent intentWalkThrough = new Intent(SplashActivity.this, WalkthroughActivity.class);
            startActivity(intentWalkThrough);
        }
        SplashActivity.this.finish();
    }

    private void setNewsData(Object o)  {
        int max=sharedpreferences.getInt(Constants.LAST_NEWS_UPDATE,0);
        Log.e(LOG_TAG,"MAX ID VALUE :"+max);
        Log.e(LOG_TAG,"NEWS JSON : "+o.toString());
        try {
            JSONObject jObject=new JSONObject(o.toString());
            String checkStatus=jObject.getString("status");
            if(checkStatus.equals("0")&& o != null){
                JSONArray newsArray=jObject.getJSONArray("news");
                Log.e(LOG_TAG,"News Element Count "+newsArray.length());
                for(int i =0 ; i<newsArray.length();i++){
                    JSONObject newsObject=newsArray.getJSONObject(i);
                    Log.d("newsPrint", newsObject.toString());
                    mNewsDetailModel=new NewsDetailsModel();
                    mNewsDetailModel.setNewsID(newsObject.getString("ID"));
                    mNewsDetailModel.setNewsTitle(newsObject.getString("NewsTitle"));
                    mNewsDetailModel.setNewsDescription(newsObject.getString("NewsDescription"));
                    mNewsDetailModel.setNewsVendor(newsObject.getString("NewsVendorName"));
                    mNewsDetailModel.setNewsVendorLogo(newsObject.getString("NewsVendorLogo"));
                    mNewsDetailModel.setNewsTimeline(newsObject.getString("NewsTime"));
                    mNewsDetailModel.setNewsVendorLink(newsObject.getString("NewsVendorLink"));
                    mNewsDetailModel.setNewsImage(newsObject.getString("NewsImage"));
                    mNewsDetailModel.setNewsUpVote(newsObject.getInt("NewsUpVote"));
                    mNewsDetailModel.setNewsDownVote(newsObject.getInt("NewsDownVote"));
                    mNewsDetailModel.setNewsState(newsObject.getString("NewsState"));
                    mNewsDetailModel.setNewsUpVoteValue(false);
                    mNewsDetailModel.setNewsDownVoteValue(false);
                    if(Integer.valueOf(mNewsDetailModel.getNewsID())>max){
                        max=Integer.valueOf(mNewsDetailModel.getNewsID());
                    }
                    newsList.add(mNewsDetailModel);
                }
                editor.putString(Constants.NEWS_LIST, new Gson().toJson(newsList));
                editor.putInt(Constants.LAST_NEWS_UPDATE,max);
                editor.commit();
                editor = sharedpreferences.edit();
                Type type = new TypeToken<ArrayList<NewsDetailsModel>>() {}.getType();
                newsListArray = new Gson().fromJson(sharedpreferences.getString(Constants.NEWS_LIST, ""), type);
                Log.e(LOG_TAG,"News Element Count "+newsListArray.size());

            }
            else if(checkStatus.equals("1")){
                //Toast.makeText(this, "Error Loading News List", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Server Connection Error", Toast.LENGTH_SHORT).show();
            }
            FetchPoll mFetchPoll=new FetchPoll();
            mFetchPoll.execute();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,e.toString());
        }

    }

    private void setPollData(Object o)  {
        int max=sharedpreferences.getInt(Constants.LAST_POLL_UPDATE,0);
        try {
            JSONObject jObject=new JSONObject(o.toString());
            String checkStatus=jObject.getString("status");
            if(checkStatus.equals("1")&&o != null){
                JSONArray newsArray=jObject.getJSONArray("polls");
                for(int i =0 ; i<newsArray.length();i++){
                    JSONObject pollObject=newsArray.getJSONObject(i);
                    mPollDetailModel=new PollDetailsModel();
                    mPollDetailModel.setPollId(pollObject.getString("PollId"));
                    mPollDetailModel.setPollTitle(pollObject.getString("PollTitle"));
                    mPollDetailModel.setPollImage(pollObject.getString("PollImage"));
                    mPollDetailModel.setPollDescription(pollObject.getString("PollDescription"));
                    mPollDetailModel.setPollOptionOneText(pollObject.getString("PollOptionOneText"));
                    mPollDetailModel.setPollOptionOneVote(pollObject.getInt("PollOptionOneVote"));
                    mPollDetailModel.setPollOptionTwoText(pollObject.getString("PollOptionTwoText"));
                    mPollDetailModel.setPollOptionTwoVote(pollObject.getInt("PollOptionTwoVote"));
                    mPollDetailModel.setPollVendor(pollObject.getString("PollVendor"));
                    mPollDetailModel.setPollVendorLogo(pollObject.getString("PollVendorLogo"));
                    mPollDetailModel.setPollStartDate(pollObject.getString("PollStartDate"));
                    mPollDetailModel.setPollEndDate(pollObject.getString("PollEndDate"));
                    mPollDetailModel.setPollOptionOneColor(pollObject.getInt("PollOptionOneColor"));
                    mPollDetailModel.setPollOptionTwoColor(pollObject.getInt("PollOptionTwoColor"));
                    mPollDetailModel.setPollQuestion(pollObject.getString("PollQuestion"));
                    if(Integer.valueOf(mPollDetailModel.getPollId())>max){
                        max=Integer.valueOf(mPollDetailModel.getPollId());
                    }
                    pollList.add(mPollDetailModel);
                }
                editor.putString(Constants.POLL_LIST, new Gson().toJson(pollList));
                editor.putInt(Constants.LAST_POLL_UPDATE,max);
                editor.commit();

            }
            else if(checkStatus.equals("0")){
                //Toast.makeText(this, "Error Loading News List", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Server Connection Error", Toast.LENGTH_SHORT).show();
            }
            FetchBuzz mFetchBuzz=new FetchBuzz();
            mFetchBuzz.execute();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,e.toString());
        }

    }

    private void setBuzzData(Object o)  {
        int max=sharedpreferences.getInt(Constants.LAST_BUZZ_UPDATE,0);

        try {
            JSONObject jObject=new JSONObject(o.toString());
            Log.e(LOG_TAG,"Buzz Details :"+o.toString());
            String checkStatus=jObject.getString("status");
            if(checkStatus.equals("1")&&o != null){
                Type type = new TypeToken<ArrayList<PostDetailModel>>() {}.getType();
                Boolean firstTime=sharedpreferences.getBoolean(Constants.IS_FIRST_TIME_LAUNCH,false);
                if(firstTime==null) {
                    buzzList = new Gson().fromJson(sharedpreferences.getString(Constants.BUZZ_LIST, ""), type);
                }
                JSONArray newsArray=jObject.getJSONArray("posts");
                for(int i =0 ; i<newsArray.length();i++){
                    JSONObject buzzObject=newsArray.getJSONObject(i);
                    mPostDetailModel=new PostDetailModel();
                    mPostDetailModel.setPostId(buzzObject.getString("PostId"));
                    mPostDetailModel.setPostUserName(buzzObject.getString("PostUserName"));
                    mPostDetailModel.setPostFirstName(buzzObject.getString("FirstName"));
                    mPostDetailModel.setPostLastName(buzzObject.getString("LastName"));
                    mPostDetailModel.setPostText(buzzObject.getString("PostText"));
                    mPostDetailModel.setPostTime(buzzObject.getString("PostTime"));
                    mPostDetailModel.setPostDate(buzzObject.getString("PostDate"));
                    mPostDetailModel.setPostUpVote(buzzObject.getInt("PostUpVote"));
                    mPostDetailModel.setPostDownVote(buzzObject.getInt("PostDownVote"));
                    mPostDetailModel.setPostType(buzzObject.getString("PostType"));
                    mPostDetailModel.setIsShared(buzzObject.getString("IsShared"));
                    mPostDetailModel.setSharedCount(buzzObject.getString("ShareCount"));
                    mPostDetailModel.setSharedFrom(buzzObject.getString("SharedFrom"));
                    mPostDetailModel.setPostUserPhoto(buzzObject.getString("UserPhoto"));
                    mPostDetailModel.setPostRepParty(buzzObject.getString("RepParty"));
                    mPostDetailModel.setPostRepDesignation(buzzObject.getString("RepDesignation"));
                    mPostDetailModel.setPostRepDetail(buzzObject.getString("RepDetail"));
                    mPostDetailModel.setPostUpVoteValue(false);
                    mPostDetailModel.setPostDownVoteValue(false);
                    JSONArray postImageArray=buzzObject.getJSONArray("images");
                    ArrayList<String>postImageArrayList = new ArrayList<>();
                    for(int j =0 ; j<postImageArray.length();j++) {
                        postImageArrayList.add(postImageArray.getString(j));
                    }
                    if(postImageArray.length()>0) {
                        mPostDetailModel.setPostImages(postImageArrayList);
                    }
                    else{
                        mPostDetailModel.setPostImages(null);
                    }
                    if(Integer.valueOf(mPostDetailModel.getPostId())>max){
                        max=Integer.valueOf(mPostDetailModel.getPostId());
                    }
                    buzzList.add(mPostDetailModel);
                }
                editor.putString(Constants.BUZZ_LIST, new Gson().toJson(buzzList));
                editor.putInt(Constants.LAST_BUZZ_UPDATE,max);
                editor.commit();
            }
            else if(checkStatus.equals("0")){
                //Toast.makeText(this, "Error Loading News List", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Server Connection Error", Toast.LENGTH_SHORT).show();
            }
            FetchPoliticalParty mFetchPoliticalParty=new FetchPoliticalParty();
            mFetchPoliticalParty.execute();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,e.toString());
        }

    }

    private void setHomeData(Object o)  {
        int max=sharedpreferences.getInt(Constants.LAST_USER_UPDATE,0);
        try {
            JSONObject jObject=new JSONObject(o.toString());
            Log.e(LOG_TAG,"Home Details :"+o.toString());
            String checkStatus=jObject.getString("status");
            if(checkStatus.equals("1")&&o != null){
                Type type = new TypeToken<ArrayList<PostDetailModel>>() {}.getType();
                Boolean firstTime=sharedpreferences.getBoolean(Constants.IS_FIRST_TIME_LAUNCH,false);
                if(firstTime==null) {
                    homeList = new Gson().fromJson(sharedpreferences.getString(Constants.HOME_LIST, ""), type);
                }
                JSONArray newsArray=jObject.getJSONArray("posts");
                for(int i =0 ; i<newsArray.length();i++){
                    JSONObject buzzObject=newsArray.getJSONObject(i);
                    mPostDetailModel=new PostDetailModel();
                    mPostDetailModel.setPostId(buzzObject.getString("PostId"));
                    mPostDetailModel.setPostUserName(buzzObject.getString("PostUserName"));
                    mPostDetailModel.setPostFirstName(buzzObject.getString("FirstName"));
                    mPostDetailModel.setPostLastName(buzzObject.getString("LastName"));
                    mPostDetailModel.setPostText(buzzObject.getString("PostText"));
                    mPostDetailModel.setPostTime(buzzObject.getString("PostTime"));
                    mPostDetailModel.setPostDate(buzzObject.getString("PostDate"));
                    mPostDetailModel.setPostUpVote(buzzObject.getInt("PostUpVote"));
                    mPostDetailModel.setPostDownVote(buzzObject.getInt("PostDownVote"));
                    mPostDetailModel.setPostType(buzzObject.getString("PostType"));
                    mPostDetailModel.setIsShared(buzzObject.getString("IsShared"));
                    mPostDetailModel.setSharedFrom(buzzObject.getString("SharedFrom"));
                    mPostDetailModel.setSharedCount(buzzObject.getString("ShareCount"));
                    mPostDetailModel.setPostUserPhoto(buzzObject.getString("UserPhoto"));
                    mPostDetailModel.setPostUpVoteValue(false);
                    mPostDetailModel.setPostDownVoteValue(false);
                    JSONArray postImageArray=buzzObject.getJSONArray("images");
                    ArrayList<String>postImageArrayList = new ArrayList<>();
                    for(int j =0 ; j<postImageArray.length();j++) {
                        postImageArrayList.add(postImageArray.getString(j));
                    }
                    if(postImageArray.length()>0) {
                        mPostDetailModel.setPostImages(postImageArrayList);
                    }
                    else{
                        mPostDetailModel.setPostImages(null);
                    }
                    if(Integer.valueOf(mPostDetailModel.getPostId())>max){
                        max=Integer.valueOf(mPostDetailModel.getPostId());
                    }
                    homeList.add(mPostDetailModel);
                }
                editor.putString(Constants.HOME_LIST, new Gson().toJson(homeList));
                editor.putInt(Constants.LAST_USER_UPDATE,max);
                editor.commit();
            }
            else if(checkStatus.equals("0")){
                //Toast.makeText(this, "Error Loading News List", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Server Connection Error", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,e.toString());
        }
        gotoNextActivity();

    }

    private void setPoliticalPartyData(Object o)  {
        Log.e(LOG_TAG,"POLITICAL PARTY JSON : "+o.toString());
        try {
            politicalPartyList.clear();
            JSONObject jObject=new JSONObject(o.toString());
            String checkStatus=jObject.getString("status");
            if(checkStatus.equals("1")&& o != null){
                JSONArray politicalPartyArray=jObject.getJSONArray("party");
                Log.e(LOG_TAG,"POLITICAL PARTY Element Count "+politicalPartyArray.length());
                for(int i =0 ; i<politicalPartyArray.length();i++){
                    JSONObject politicalPartyObject=politicalPartyArray.getJSONObject(i);
                    politicalPartyModel=new PoliticalPartyModel();
                    politicalPartyModel.setPartyId(politicalPartyObject.getString("PartyId"));
                    politicalPartyModel.setPartyName(politicalPartyObject.getString("PartyName"));
                    politicalPartyModel.setPartyNameShort(politicalPartyObject.getString("PartyNameShort"));
                    politicalPartyModel.setPartyLogo(politicalPartyObject.getString("PartyLogo"));
                    politicalPartyList.add(politicalPartyModel);
                }
                editor.putString(Constants.POLITICAL_PARTY_LIST, new Gson().toJson(politicalPartyList));
                editor.commit();
            }
            else {
                Toast.makeText(this, "Server Connection Error", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,e.toString());
        }
        FetchHome mFetchHome=new FetchHome();
        mFetchHome.execute();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    class FetchNews extends AsyncTask {

        String url_news_update = "http://echoindia.co.in/php/news.php";
        int maxID=sharedpreferences.getInt(Constants.LAST_NEWS_UPDATE,0);

        @Override
        protected Object doInBackground(Object[] params) {

            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(url_news_update);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("maxID",maxID);
                Log.e(LOG_TAG,"URL"+url_news_update);
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
            setNewsData(o);
        }
    }

    class FetchPoll extends AsyncTask {

        String url_poll_update = "http://echoindia.co.in/php/getPolls.php";
        int maxID=sharedpreferences.getInt(Constants.LAST_POLL_UPDATE,0);

        @Override
        protected Object doInBackground(Object[] params) {

            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(url_poll_update);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("maxID",maxID);
                Log.e(LOG_TAG,"URL"+url_poll_update);
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
            Log.e(LOG_TAG,"POLL : "+o.toString());
            setPollData(o);
        }
    }

    class FetchBuzz extends AsyncTask {

        String url_buzz_update = "http://echoindia.co.in/php/posts.php";
        int maxID=sharedpreferences.getInt(Constants.LAST_BUZZ_UPDATE,0);

        @Override
        protected Object doInBackground(Object[] params) {

            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(url_buzz_update);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("maxID",maxID);
                Log.e(LOG_TAG,"URL"+url_buzz_update);
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
            Log.e(LOG_TAG,"BUZZ : "+o.toString());
            setBuzzData(o);
        }
    }


    class FetchHome extends AsyncTask {

        String url_home_update = "http://echoindia.co.in/php/userpost.php";
        int maxID=sharedpreferences.getInt(Constants.LAST_USER_UPDATE,0);

        @Override
        protected Object doInBackground(Object[] params) {

            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(url_home_update);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("maxID",maxID);
                Log.e(LOG_TAG,"URL"+url_home_update);
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
            Log.e(LOG_TAG,"HOME : "+o.toString());
            setHomeData(o);
        }
    }

    class FetchPoliticalParty extends AsyncTask {

        String url_political_party = "http://echoindia.co.in/php/getParty.php";


        @Override
        protected Object doInBackground(Object[] params) {

            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(url_political_party);

                Log.e(LOG_TAG,"URL"+url_political_party);
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
            setPoliticalPartyData(o);
        }
    }
}
