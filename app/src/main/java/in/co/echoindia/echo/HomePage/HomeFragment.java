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
import in.co.echoindia.echo.Model.PostDetailModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;


public class HomeFragment extends Fragment {
    private static final String LOG_TAG = "HomeFragment";
    HomeAdapter mHomeAdapter;
    ArrayList<PostDetailModel> homeListArray = new ArrayList<>();

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    ListView homeListView;
    ArrayList<PostDetailModel> homeList=new ArrayList<PostDetailModel>();
    PostDetailModel mPostDetailModel;
    SwipeRefreshLayout homeSwipeRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        homeListView=(ListView)v.findViewById(R.id.home_list);

        homeSwipeRefresh=(SwipeRefreshLayout)v.findViewById(R.id.home_swipe_refresh);
        sharedpreferences = AppUtil.getAppPreferences(getActivity());
        editor = sharedpreferences.edit();
        Type type = new TypeToken<ArrayList<PostDetailModel>>() {}.getType();
        homeListArray = new Gson().fromJson(sharedpreferences.getString(Constants.HOME_LIST, ""), type);
        Log.e(LOG_TAG,"home Element Count "+homeListArray.size());
        mHomeAdapter = new HomeAdapter(getActivity(), homeListArray);
        homeListView.setAdapter(mHomeAdapter);
        mHomeAdapter.notifyDataSetChanged();
        homeSwipeRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.e(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
                        FetchHome mFetchHome=new FetchHome();
                        mFetchHome.execute();
                    }
                }
        );
        return v;
    }


    private void setHomeData(Object o)  {
        int max=sharedpreferences.getInt(Constants.LAST_USER_UPDATE,0);
        try {
            JSONObject jObject=new JSONObject(o.toString());
            Log.e(LOG_TAG,"Home Details :"+o.toString());
            String checkStatus=jObject.getString("status");
            if(checkStatus.equals("1")&&o != null){
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
                    homeListArray.add(mPostDetailModel);
                }
                editor.putString(Constants.HOME_LIST, new Gson().toJson(homeListArray));
                editor.putInt(Constants.LAST_USER_UPDATE,max);
                editor.commit();
                Toast.makeText(getActivity(), "Your Feed Updated", Toast.LENGTH_SHORT).show();
            }
            else if(checkStatus.equals("0")){
                Toast.makeText(getActivity(), "Your Feed Updated", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(), "Server Connection Error", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,e.toString());
        }
        onRefreshComplete(homeListArray);


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

    void onRefreshComplete(ArrayList<PostDetailModel> homeListRefresh){
        HomeAdapter mHomeAdapterRefreshed = new HomeAdapter(getActivity(), homeListRefresh);
        homeListView.setAdapter(mHomeAdapterRefreshed);
        mHomeAdapterRefreshed.notifyDataSetChanged();
        homeSwipeRefresh.setRefreshing(false);
    }


}
