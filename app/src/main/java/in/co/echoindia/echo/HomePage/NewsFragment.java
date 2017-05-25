package in.co.echoindia.echo.HomePage;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

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

import javax.net.ssl.HttpsURLConnection;

import in.co.echoindia.echo.Model.NewsDetailsModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

public class NewsFragment extends Fragment {

    NewsAdapter mNewsAdapter;
    ArrayList<NewsDetailsModel> newsListArray = new ArrayList<>();
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    ListView newsListView;
    ArrayList<NewsDetailsModel> newsList=new ArrayList<NewsDetailsModel>();
    NewsDetailsModel mNewsDetailModel;
    SwipeRefreshLayout newsSwipeRefresh;
    private static final String LOG_TAG = "NewsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        newsListView=(ListView)v.findViewById(R.id.news_list);
        newsSwipeRefresh=(SwipeRefreshLayout)v.findViewById(R.id.news_swipe_refresh);
        sharedpreferences = AppUtil.getAppPreferences(getActivity());
        editor = sharedpreferences.edit();
        Type type = new TypeToken<ArrayList<NewsDetailsModel>>() {}.getType();
        newsListArray = new Gson().fromJson(sharedpreferences.getString(Constants.NEWS_LIST, ""), type);
        Log.e(LOG_TAG,"News Element Count "+newsListArray.size());
        mNewsAdapter = new NewsAdapter(getActivity(), newsListArray);
        newsListView.setAdapter(mNewsAdapter);
        mNewsAdapter.notifyDataSetChanged();
        newsSwipeRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.e(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
                        FetchNews mFetchNews=new FetchNews();
                        mFetchNews.execute();
                    }
                }
        );
        return v;
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
            if(o!=null) {
                setNewsData(o);
            }

        }
    }
    private void setNewsData(Object o)  {
        int max=sharedpreferences.getInt(Constants.LAST_NEWS_UPDATE,0);
        Log.e(LOG_TAG,"MAX ID VALUE :"+max);
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
                    newsListArray.add(mNewsDetailModel);
                }
                editor.putString(Constants.NEWS_LIST, new Gson().toJson(newsListArray));
                editor.putInt(Constants.LAST_NEWS_UPDATE,max);
                editor.commit();
                Toast.makeText(getActivity(), "Echo News Updated", Toast.LENGTH_SHORT).show();
            }
            else if(checkStatus.equals("1")){
                Toast.makeText(getActivity(), "Echo News is up-to-date", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(), "Server Connection Error", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,e.toString());
        }

        onRefreshComplete(newsListArray);

    }
    void onRefreshComplete(ArrayList<NewsDetailsModel> newsListRefresh){
        NewsAdapter mNewsAdapterRefreshed = new NewsAdapter(getActivity(), newsListRefresh);
        newsListView.setAdapter(mNewsAdapterRefreshed);
        mNewsAdapter.notifyDataSetChanged();
        newsSwipeRefresh.setRefreshing(false);
    }

}
