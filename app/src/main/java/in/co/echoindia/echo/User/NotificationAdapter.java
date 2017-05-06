package in.co.echoindia.echo.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;
import in.co.echoindia.echo.Model.NotificationModel;
import in.co.echoindia.echo.Model.PostDetailModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

/**
 * Created by Danish Rafique on 05-05-2017.
 */

public class NotificationAdapter extends BaseAdapter {

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    ArrayList<NotificationModel> notificationModels = new ArrayList<>();
    private Context activity;
    private LayoutInflater inflater;

    CircleImageView notificationUserImage;
    TextView notificationText;
    TextView buzzTime;
    PostDetailModel mPostDetailModel;
    private static final String LOG_TAG = "NotificationActivity";


    public NotificationAdapter(Context activity, ArrayList<NotificationModel> notificationModels) {
        this.activity = activity;
        this.notificationModels = notificationModels;
    }

    @Override
    public int getCount() {
        if (notificationModels != null)
            return notificationModels.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        sharedpreferences = AppUtil.getAppPreferences(activity);
        editor = sharedpreferences.edit();

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_notification_child, null);
        }
        final NotificationModel notificationObj = notificationModels.get(position);
        notificationUserImage=(CircleImageView)convertView.findViewById(R.id.notification_image);
        notificationText=(TextView)convertView.findViewById(R.id.notification_text);
        buzzTime=(TextView)convertView.findViewById(R.id.notification_time);
        final LinearLayout notificationLL=(LinearLayout)convertView.findViewById(R.id.notification_ll);

        if(notificationUserImage!=null) {
            Glide.with(activity).load(notificationObj.getNotificationImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(notificationUserImage);
        }
        notificationText.setText(notificationObj.getNotificationBody());


        final String startDate=notificationObj.getNotificationDate();
        Date currentDate = new Date();
        final String dateToday = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
        int dateDay=Integer.parseInt(dateToday.substring(8,10));
        int dateMonth=Integer.parseInt(dateToday.substring(5,7));
        int dateYear=Integer.parseInt(dateToday.substring(0,4));
        int startDateDay=Integer.parseInt(startDate.substring(8,10));
        int startDateMonth=Integer.parseInt(startDate.substring(5,7));
        int startDateYear=Integer.parseInt(startDate.substring(0,4));
        if(dateMonth>startDateMonth){
            if((dateMonth-startDateMonth)==1) {
                buzzTime.setText(dateMonth - startDateMonth + " month ago");
            }
            else
            {
                buzzTime.setText(dateMonth - startDateMonth + " months ago");
            }

        }
        else if(startDateYear<dateYear){
            buzzTime.setText(dateMonth+(12-startDateMonth) + " months ago");
        }
        else if(startDateYear==dateYear&&startDateMonth==dateMonth&&startDateDay<dateDay){
            buzzTime.setText((dateDay-startDateDay) + " days ago");
        }
        else if(startDateYear==dateYear&&startDateMonth==dateMonth&&startDateDay==dateDay){
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String timeNow = sdf.format(new Date());
            final String postTime=notificationObj.getNotificationTime();
            int timeNowHour=Integer.parseInt(timeNow.substring(0,2));
            int timeNowMin=Integer.parseInt(timeNow.substring(3,5));
            int timeNowSec=Integer.parseInt(timeNow.substring(6));
            int postTimeHour=Integer.parseInt(postTime.substring(0,2));
            int postTimeMin=Integer.parseInt(postTime.substring(3,5));
            int postTimeSec=Integer.parseInt(postTime.substring(6));
            if(timeNowHour>postTimeHour){
                if((timeNowHour-postTimeHour)==1) {
                    buzzTime.setText(timeNowHour - postTimeHour + " hr ago");
                }
                else
                {
                    buzzTime.setText(timeNowHour - postTimeHour + " hrs ago");
                }
            }
            else if(timeNowMin>postTimeMin){
                if((timeNowMin-postTimeMin)==1) {
                    buzzTime.setText(timeNowMin - postTimeMin + " min ago");
                }
                else
                {
                    buzzTime.setText(timeNowMin - postTimeMin + " mins ago");
                }
            }
            else if(timeNowSec>postTimeSec){
                if((timeNowSec-postTimeSec)==1) {
                    buzzTime.setText(timeNowSec - postTimeSec + " sec ago");
                }
                else
                {
                    buzzTime.setText(timeNowSec - postTimeSec + " secs ago");
                }
            }
        }
        final String postId=notificationObj.getPostID();
        final String postType=notificationObj.getNotificationPostType();


        notificationLL.setBackgroundColor(Color.WHITE);
        notificationText.setTextColor(Color.BLACK);
        buzzTime.setTextColor(Color.BLACK);


        notificationLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                GetPost getPost=new GetPost(postId,postType);
                getPost.execute();
            }
        });

        int numberOfColoredPost=sharedpreferences.getInt(Constants.NUMBER_OF_COLORED_POST,0);
        if(numberOfColoredPost>position){
            notificationLL.setBackgroundColor(Color.parseColor("#247396"));
            notificationText.setTextColor(Color.WHITE);
            buzzTime.setTextColor(Color.WHITE);
        }
       return convertView;
    }

    class GetPost extends AsyncTask {

        String url_get_post = "http://echoindia.co.in/php/singlePost.php";

        String postType;
        String postId;

        public GetPost(String postId,String postType){
            this.postType=postType;
            this.postId=postId;
        }


        @Override
        protected Object doInBackground(Object[] params) {

            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(url_get_post);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("postId",postId);
                postDataParams.put("postType",postType);
                Log.e(LOG_TAG,"URL"+url_get_post);
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
            Log.e(LOG_TAG,"NOTIFICATION : "+o.toString());
              setHomeData(o);
        }
    }

    private void setHomeData(Object o)  {

        try {
            JSONObject jObject=new JSONObject(o.toString());
            Log.e(LOG_TAG,"Notification :"+o.toString());
            String checkStatus=jObject.getString("status");
            if(checkStatus.equals("1")&&o != null){
                JSONArray newsArray=jObject.getJSONArray("Post");
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
                    mPostDetailModel.setPostLocation(buzzObject.getString("PostLocation"));
                    mPostDetailModel.setIsShared(buzzObject.getString("IsShared"));
                    mPostDetailModel.setSharedCount(buzzObject.getString("ShareCount"));
                    mPostDetailModel.setSharedFrom(buzzObject.getString("SharedFrom"));
                    if(mPostDetailModel.getPostType().equals("BUZZ")){
                        mPostDetailModel.setPostRepParty(buzzObject.getString("RepParty"));
                        mPostDetailModel.setPostRepDesignation(buzzObject.getString("RepDesignation"));
                    }
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

                }

                Intent viewPost=new Intent(activity, ViewPostActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("postObj", mPostDetailModel);
                viewPost.putExtra("postObj",mBundle);
                activity.startActivity(viewPost);

            }
           else {
                Toast.makeText(activity, "Server Connection Error", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,e.toString());
        }



    }
}
