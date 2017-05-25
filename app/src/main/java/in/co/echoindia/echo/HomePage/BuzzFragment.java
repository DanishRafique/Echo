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
import java.util.Collections;
import java.util.Comparator;

import javax.net.ssl.HttpsURLConnection;

import in.co.echoindia.echo.Model.PostDetailModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;


public class BuzzFragment extends Fragment {
    private static final String LOG_TAG = "BuzzFragment";
    BuzzAdapter mBuzzAdapter;
    ArrayList<PostDetailModel> buzzListArray = new ArrayList<>();
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    ListView buzzListView;
    ArrayList<PostDetailModel> buzzList=new ArrayList<PostDetailModel>();
    PostDetailModel mPostDetailModel;
    SwipeRefreshLayout buzzSwipeRefresh;
    Type type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buzz, container, false);
        buzzListView=(ListView)v.findViewById(R.id.buzz_list);
        buzzSwipeRefresh=(SwipeRefreshLayout)v.findViewById(R.id.buzz_swipe_refresh);
        sharedpreferences = AppUtil.getAppPreferences(getActivity());
        editor = sharedpreferences.edit();
        type = new TypeToken<ArrayList<PostDetailModel>>() {}.getType();
        buzzListArray = new Gson().fromJson(sharedpreferences.getString(Constants.BUZZ_LIST, ""), type);
        Log.e(LOG_TAG,"Buzz Element Count "+buzzListArray.size());
        Collections.sort(buzzListArray,new PostComparator());
        mBuzzAdapter = new BuzzAdapter(getActivity(), buzzListArray);
        buzzListView.setAdapter(mBuzzAdapter);
        mBuzzAdapter.notifyDataSetChanged();
        buzzSwipeRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        buzzListArray = new Gson().fromJson(sharedpreferences.getString(Constants.BUZZ_LIST, ""), type);
                        Log.e(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
                        FetchBuzz mFetchBuzz=new FetchBuzz();
                        mFetchBuzz.execute();
                        mBuzzAdapter.notifyDataSetChanged();
                    }
                }
        );
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBuzzAdapter.notifyDataSetChanged();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mBuzzAdapter!=null) {
            Type type = new TypeToken<ArrayList<PostDetailModel>>() {}.getType();
            buzzListArray = new Gson().fromJson(sharedpreferences.getString(Constants.BUZZ_LIST, ""), type);
            Log.e(LOG_TAG,"Buzz Element Count "+buzzListArray.size());
            Collections.sort(buzzListArray,new PostComparator());
            mBuzzAdapter = new BuzzAdapter(getActivity(), buzzListArray);
            buzzListView.setAdapter(mBuzzAdapter);
            mBuzzAdapter.notifyDataSetChanged();
            Log.e(LOG_TAG,"Set User Visibility Called Buzz");
        }
    }
    public class PostComparator implements Comparator<PostDetailModel> {

        @Override
        public int compare(PostDetailModel lhs, PostDetailModel rhs) {

            return Integer.parseInt(lhs.getPostId())<Integer.parseInt(rhs.getPostId()) ? 1:(Integer.parseInt(lhs.getPostId())==Integer.parseInt(rhs.getPostId())?0:-1);
        }
    }
    private void setBuzzData(Object o)  {
        int max=sharedpreferences.getInt(Constants.LAST_BUZZ_UPDATE,0);
        try {
            JSONObject jObject=new JSONObject(o.toString());
            Log.e(LOG_TAG,"Buzz Details :"+o.toString());
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
                    mPostDetailModel.setPostUserPhoto(buzzObject.getString("UserPhoto"));
                    mPostDetailModel.setPostRepParty(buzzObject.getString("RepParty"));
                    mPostDetailModel.setPostRepDesignation(buzzObject.getString("RepDesignation"));
                    mPostDetailModel.setPostRepDetail(buzzObject.getString("RepDetail"));
                    mPostDetailModel.setIsShared(buzzObject.getString("IsShared"));
                    mPostDetailModel.setSharedCount(buzzObject.getString("ShareCount"));
                    mPostDetailModel.setSharedFrom(buzzObject.getString("SharedFrom"));
                    mPostDetailModel.setPostLocation(buzzObject.getString("PostLocation"));
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
                    buzzListArray.add(mPostDetailModel);
                }
                editor.putString(Constants.BUZZ_LIST, new Gson().toJson(buzzListArray));
                editor.putInt(Constants.LAST_BUZZ_UPDATE,max);
                editor.commit();
                Toast.makeText(getActivity(), "Echo Buzz Updated", Toast.LENGTH_SHORT).show();
            }
            else if(checkStatus.equals("0")){
                Toast.makeText(getActivity(), "Echo Buzz Updated", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(), "Server Connection Error", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,e.toString());
        }
        onRefreshComplete(buzzListArray);

    }

    void onRefreshComplete(ArrayList<PostDetailModel> buzzListRefresh){
        Collections.sort(buzzListRefresh,new PostComparator());
        BuzzAdapter mBuzzAdapterRefreshed = new BuzzAdapter(getActivity(), buzzListRefresh);
        buzzListView.setAdapter(mBuzzAdapterRefreshed);
        mBuzzAdapterRefreshed.notifyDataSetChanged();
        buzzSwipeRefresh.setRefreshing(false);
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
            if(o!=null) {
                Log.e(LOG_TAG, "BUZZ : " + o.toString());
                setBuzzData(o);
            }
        }
    }
}
