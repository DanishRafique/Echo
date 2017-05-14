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

import in.co.echoindia.echo.Model.PollDetailsModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

public class PollFragment extends Fragment {

    PollAdapter mPollAdapter;
    ArrayList<PollDetailsModel> pollListArray = new ArrayList<>();
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    ListView pollListView;
    ArrayList<PollDetailsModel> pollList = new ArrayList<PollDetailsModel>();
    PollDetailsModel mPollDetailModel;
    SwipeRefreshLayout pollSwipeRefresh;
    private static final String LOG_TAG = "PollFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_poll, container, false);
        pollListView = (ListView) v.findViewById(R.id.poll_list);
        pollSwipeRefresh = (SwipeRefreshLayout) v.findViewById(R.id.poll_swipe_refresh);
        sharedpreferences = AppUtil.getAppPreferences(getActivity());
        editor = sharedpreferences.edit();
        Type type = new TypeToken<ArrayList<PollDetailsModel>>() {
        }.getType();
        pollListArray = new Gson().fromJson(sharedpreferences.getString(Constants.POLL_LIST, ""), type);
        mPollAdapter = new PollAdapter(getActivity(), pollListArray);
        pollListView.setAdapter(mPollAdapter);
        mPollAdapter.notifyDataSetChanged();
        pollSwipeRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.e(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
                        FetchPoll mFetchPoll=new FetchPoll();
                        mFetchPoll.execute();
                    }
                }
        );
        return v;
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
                postDataParams.put("maxID",0);
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
                pollListArray.clear();
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
                    mPollDetailModel.setPollOptionOneColor(pollObject.getInt("PollOptionOneColor"));
                    mPollDetailModel.setPollOptionTwoColor(pollObject.getInt("PollOptionTwoColor"));
                    mPollDetailModel.setPollVendorLogo(pollObject.getString("PollVendorLogo"));
                    mPollDetailModel.setPollStartDate(pollObject.getString("PollStartDate"));
                    mPollDetailModel.setPollQuestion(pollObject.getString("PollQuestion"));
                    mPollDetailModel.setPollEndDate(pollObject.getString("PollEndDate"));
                    if(Integer.valueOf(mPollDetailModel.getPollId())>max){
                        max=Integer.valueOf(mPollDetailModel.getPollId());
                    }
                    pollListArray.add(mPollDetailModel);
                }
                editor.putString(Constants.POLL_LIST, new Gson().toJson(pollListArray));
                editor.putInt(Constants.LAST_POLL_UPDATE,max);
                editor.commit();

            }
            else if(checkStatus.equals("0")){
                Toast.makeText(getActivity(), "Echo Poll is up-to-date", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(), "Server Connection Error", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,e.toString());
        }
        onRefreshComplete(pollListArray);

    }
    void onRefreshComplete(ArrayList<PollDetailsModel> pollListRefresh){
        PollAdapter mPollAdapterRefreshed = new PollAdapter(getActivity(), pollListRefresh);
        pollListView.setAdapter(mPollAdapterRefreshed);
        mPollAdapter.notifyDataSetChanged();
        pollSwipeRefresh.setRefreshing(false);
    }


}