package in.co.echoindia.echo.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import in.co.echoindia.echo.HomePage.HomePageActivity;
import in.co.echoindia.echo.Model.NewsDetailsModel;
import in.co.echoindia.echo.Model.PollDetailsModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    private static final String LOG_TAG = "SplashActivity";
    SharedPreferences.Editor editor;
    private View mContentView;
    NewsDetailsModel mNewsDetailModel;
    PollDetailsModel mPollDetailModel;
    ArrayList<NewsDetailsModel> newsList=new ArrayList<NewsDetailsModel>();
    ArrayList<PollDetailsModel> pollList=new ArrayList<PollDetailsModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        mContentView = findViewById(R.id.fullscreen_content);
        sharedpreferences = AppUtil.getAppPreferences(this);
        editor = sharedpreferences.edit();


        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        FetchNews mFetchNews=new FetchNews();
        mFetchNews.execute();
    }

    void gotoNextActivity(){
        if(sharedpreferences.getBoolean(Constants.SETTINGS_IS_LOGGED,false)) {
            Intent intentHomePage = new Intent(SplashActivity.this, HomePageActivity.class);
            Toast.makeText(SplashActivity.this, "Welcome "+sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_USER_CODE,""), Toast.LENGTH_SHORT).show();
            startActivity(intentHomePage);

        }
        else{
            Intent intentWalkThrough = new Intent(SplashActivity.this, WalkthroughActivity.class);
            startActivity(intentWalkThrough);
        }
        SplashActivity.this.finish();
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
    private void setNewsData(Object o)  {
        int max=sharedpreferences.getInt(Constants.LAST_NEWS_UPDATE,0);
        Log.e(LOG_TAG,"MAX ID VALUE :"+max);
        Log.e(LOG_TAG,"NEWS JSON : "+o.toString());
        try {
            JSONObject jObject=new JSONObject(o.toString());
            String checkStatus=jObject.getString("status");
            if(checkStatus.equals("0")&&o != null){
                JSONArray newsArray=jObject.getJSONArray("news");
                for(int i =0 ; i<newsArray.length();i++){
                    JSONObject newsObject=newsArray.getJSONObject(i);
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
                    if(Integer.valueOf(mNewsDetailModel.getNewsID())>max){
                        max=Integer.valueOf(mNewsDetailModel.getNewsID());
                    }
                    newsList.add(mNewsDetailModel);
                }
                editor.putString(Constants.NEWS_LIST, new Gson().toJson(newsList));
                editor.putInt(Constants.LAST_NEWS_UPDATE,max);
                editor.commit();

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
                    mPollDetailModel.setPollEndDate(pollObject.getString("PollEndData"));
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
            gotoNextActivity();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,e.toString());
        }

    }
}
