package in.co.echoindia.echo.HomePage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;
import in.co.echoindia.echo.Model.PollCommentModel;
import in.co.echoindia.echo.Model.PostDetailModel;
import in.co.echoindia.echo.Model.RepDetailModel;
import in.co.echoindia.echo.Model.UserDetailsModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.User.OtherUserProfile;
import in.co.echoindia.echo.User.ViewPostActivity;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

/**
 * Created by Danish Rafique on 21-04-2017.
 */

public class HomeAdapter extends BaseAdapter {
    ArrayList<PostDetailModel> homeDetailsModels = new ArrayList<>();
    Activity activity;
    ArrayList<PostDetailModel> buzzDetailsModels = new ArrayList<>();

    TextView homeFullName;
    ArrayList<PostDetailModel> homeListArray = new ArrayList<>();

    ArrayList<PostDetailModel> userPost=new ArrayList<>();
    String userImageViewProfile;
    String userNameViewProfile;
    String userType;
    String userParty;
    String userDesignation;

    CircleImageView homeUserImage;
    TextView homeUserName;
    TextView homeText;
    ListView homeImageListView;
    TextView homeUpvoteValue;
    TextView homeDownvoteValue;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    ImageView homeImage1,homeImage2,homeImage3;
    RelativeLayout homeMoreImage;
    TextView homeMoreImageText;
    LinearLayout homeMoreThanOneImage;
    RelativeLayout homeMoreThanTwoImage;


    BuzzImageAdapter mBuzzImageAdapter;
    private static final String LOG_TAG = "HomeAdapter";

    LinearLayout homeImageList;


    Dialog commentDialog;
    ListView postCommentList;
    EditText postCommentEdit;
    ImageView postCommentSend;
    SwipeRefreshLayout postCommentSwipeRefresh;
    ArrayList<PollCommentModel> postCommentListArray = new ArrayList<PollCommentModel>();
    ArrayList<PollCommentModel> pollCommentListArray = new ArrayList<PollCommentModel>();
    PollCommentAdapter mPollCommentAdapter;
    PollCommentModel mPollCommentModel;

    ArrayList<PostDetailModel> postUpdatedList=new ArrayList<>();
    ToggleButton tempBtn;
    LinearLayout homeShare;
    TextView homeSharedFrom;
    PostDetailModel mPostDetailModel;
    LinearLayout commentListLL;
    String postLocation;
    TextView postLocationText;
    LinearLayout postLocationll;



    public HomeAdapter(Activity activity, ArrayList<PostDetailModel> homeDetailsModels) {
        this.activity = activity;
        this.homeDetailsModels = homeDetailsModels;
    }

    @Override
    public int getCount() {
        if(homeDetailsModels != null)
            return homeDetailsModels.size();
        else
            return  0;
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_home_child, null);
        final PostDetailModel homeObj = homeDetailsModels.get(position);
        homeFullName=(TextView)convertView.findViewById(R.id.home_full_name);
        homeUserImage=(CircleImageView) convertView.findViewById(R.id.home_user_image);
        homeUserName=(TextView) convertView.findViewById(R.id.home_user_name);
        final TextView buzzTime=(TextView)convertView.findViewById(R.id.home_time);
        homeText=(TextView)convertView.findViewById(R.id.home_text);
        homeImageList=(LinearLayout)convertView.findViewById(R.id.home_image_list);
        homeUpvoteValue=(TextView)convertView.findViewById(R.id.home_upvote_value);
        homeDownvoteValue=(TextView)convertView.findViewById(R.id.home_downvote_value);
        homeImage1=(ImageView)convertView.findViewById(R.id.home_image_1);
        homeImage2=(ImageView)convertView.findViewById(R.id.home_image_2);
        homeImage3=(ImageView)convertView.findViewById(R.id.home_image_3);

        homeMoreImageText=(TextView)convertView.findViewById(R.id.home_more_image_text);
        homeMoreImage=(RelativeLayout)convertView.findViewById(R.id.home_more_image);
        homeMoreThanOneImage=(LinearLayout)convertView.findViewById(R.id.home_more_than_one_image);
        homeMoreThanTwoImage=(RelativeLayout)convertView.findViewById(R.id.home_more_than_two_image);

        homeShare=(LinearLayout)convertView.findViewById(R.id.home_ll_share);
        homeSharedFrom=(TextView)convertView.findViewById(R.id.home_shared_from);
        postLocationll=(LinearLayout)convertView.findViewById(R.id.home_location_ll);
        postLocationText=(TextView)convertView.findViewById(R.id.home_location_text);
        final LinearLayout buzzShareButton=(LinearLayout)convertView.findViewById(R.id.home_share_button);
        sharedpreferences = AppUtil.getAppPreferences(activity);
        editor = sharedpreferences.edit();

        if(homeObj.getIsShared().equals("0")){
            homeShare.setVisibility(View.GONE);
        }
        else if(homeObj.getIsShared().equals("1")){
            homeSharedFrom.setText(homeObj.getSharedFrom());
        }

        homeFullName.setText(homeObj.getPostFirstName()+" "+homeObj.getPostLastName());

        postLocation=homeObj.getPostLocation();
        if(postLocation==null || postLocation.isEmpty() || postLocation.equals(" ") || postLocation.equals("")){
            postLocationll.setVisibility(View.GONE);
        }
        else{
            postLocationText.setText(postLocation);
        }

        Glide.with(activity).load(homeObj.getPostUserPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).into(homeUserImage);
        homeUserName.setText(homeObj.getPostUserName());
        homeText.setText(homeObj.getPostText());
        homeUpvoteValue.setText(String.valueOf(homeObj.getPostUpVote()));
        homeDownvoteValue.setText(String.valueOf(homeObj.getPostDownVote()));
        final LinearLayout postCommentButton =(LinearLayout)convertView.findViewById(R.id.home_comment_link);
        final String postId=homeObj.getPostId();
        final String fullNameStr=homeObj.getPostFirstName()+" "+homeObj.getPostLastName();


        final ArrayList<String> homeImageArrayList = homeObj.getPostImages();
        if(homeImageArrayList!=null){
            for(int i=0;i<homeImageArrayList.size();i++){
                if(i==0){
                    homeImage1.setVisibility(View.VISIBLE);
                    Glide.with(activity).load(homeImageArrayList.get(i)).diskCacheStrategy(DiskCacheStrategy.ALL).into(homeImage1);
                }
                else if(i==1){
                    homeMoreThanOneImage.setVisibility(View.VISIBLE);
                    homeImage2.setVisibility(View.VISIBLE);
                    Glide.with(activity).load(homeImageArrayList.get(i)).diskCacheStrategy(DiskCacheStrategy.ALL).into(homeImage2);
                }
                else if(i==2){
                    homeMoreThanTwoImage.setVisibility(View.VISIBLE);
                    homeImage3.setVisibility(View.VISIBLE);
                    Glide.with(activity).load(homeImageArrayList.get(i)).diskCacheStrategy(DiskCacheStrategy.ALL).into(homeImage3);
                }
                else if(i>2){
                    homeMoreImage.setVisibility(View.VISIBLE);
                    homeMoreImageText.setVisibility(View.VISIBLE);
                    int numberOfImage=homeImageArrayList.size()-3;
                    homeMoreImageText.setText(String.valueOf(numberOfImage)+"+");
                }
            }
        }
        else{
            homeImageList.setVisibility(View.GONE);
        }



        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FetchPostComment fetchPollComment=new FetchPostComment(postId);
                fetchPollComment.execute();
            }
        });


        final ToggleButton homeUpvote = (ToggleButton) convertView.findViewById(R.id.home_upvote);
        homeUpvote.setTag(String.valueOf(homeObj.getPostId()));
        final ToggleButton homeDownvote = (ToggleButton) convertView.findViewById(R.id.home_downvote);
        homeDownvote.setTag(String.valueOf(homeObj.getPostId()));

        if(homeObj.isPostUpVoteValue()){
            homeUpvote.setChecked(true);
            homeDownvote.setEnabled(false);
        }
        else if(homeObj.isPostDownVoteValue()){
            homeDownvote.setChecked(true);
            homeUpvote.setEnabled(false);
        }


        homeUpvote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                postVote postVote = new postVote();
                TextView textView = null;
                tempBtn = (ToggleButton) buttonView;
                ViewGroup view = (ViewGroup) tempBtn.getParent();
                textView = (TextView) view.findViewById(R.id.home_upvote_value);
                Log.e("Voting...", textView.getText().toString());
                int upvote = Integer.parseInt(textView.getText().toString());
                ViewGroup rootParent = (ViewGroup) view.getParent();
                ToggleButton t = (ToggleButton) rootParent.findViewById(R.id.home_downvote);

                if(!isChecked){
                    t.setEnabled(true);
                    upvote --;
                    homeObj.setPostUpVoteValue(false);
                    homeObj.setPostUpVote(upvote);
                    postVote.execute("up", "none");
                    textView.setText(upvote + "");
                }
                else {
                    t.setEnabled(false);
                    upvote ++;
                    homeObj.setPostUpVoteValue(true);
                    homeObj.setPostUpVote(upvote);
                    postVote.execute("up", "up");
                    textView.setText(upvote + "");
                }
            }
        });

        homeDownvote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                postVote postVote = new postVote();
                TextView textView = null;
                tempBtn = (ToggleButton) buttonView;
                ViewGroup view = (ViewGroup) tempBtn.getParent();
                textView = (TextView) view.findViewById(R.id.home_downvote_value);
                Log.e("Voting...", textView.getText().toString());
                int downvote = Integer.parseInt(textView.getText().toString());
                ViewGroup rootParent = (ViewGroup) view.getParent();
                ToggleButton t = (ToggleButton) rootParent.findViewById(R.id.home_upvote);
                if(!isChecked){
                    t.setEnabled(true);
                    downvote --;
                    homeObj.setPostDownVoteValue(false);
                    homeObj.setPostDownVote(downvote);
                    postVote.execute("down", "none");
                    textView.setText(downvote + "");
                }
                else {
                    t.setEnabled(false);
                    downvote ++;
                    homeObj.setPostDownVoteValue(true);
                    homeObj.setPostDownVote(downvote);
                    postVote.execute("down", "down");
                    textView.setText(downvote + "");
                }
            }
        });

        postUpdatedList.clear();

        for(int i=0;i<homeDetailsModels.size();i++){
            if(i!=position) {
                postUpdatedList.add(homeDetailsModels.get(i));
            }
            else if(i==position){
                postUpdatedList.add(homeObj);
            }
        }
        editor.putString(Constants.HOME_LIST, new Gson().toJson(postUpdatedList));
        editor.commit();

        buzzShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharePost mSharePost=new SharePost(fullNameStr,postId);
                mSharePost.execute();
            }
        });
        homeImageList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewPost=new Intent(activity, ViewPostActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("postObj", homeObj);
                viewPost.putExtra("postObj",mBundle);
                activity.startActivity(viewPost);
            }
        });

        homeFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNameViewProfile=homeObj.getPostFirstName()+" "+homeObj.getPostLastName();
                userImageViewProfile=homeObj.getPostUserPhoto();
                if(homeObj.getPostType().equals("USER")){
                    userType="USER";
                }
                else if(homeObj.getPostType().equals("BUZZ")){
                    userType="REP";
                    userDesignation=homeObj.getPostRepDesignation();
                    userParty=homeObj.getPostRepParty();
                }
                ViewUser mViewUser=new ViewUser(homeObj.getPostUserName());
                mViewUser.execute();
            }
        });

        final String startDate=homeObj.getPostDate();
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
            final String postTime=homeObj.getPostTime();
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



        return convertView;
    }


    private class postVote extends AsyncTask<String, Void, String> {
        String postId;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("Voting...", "Post Id: " + tempBtn.getTag().toString());
            postId = tempBtn.getTag().toString();
        }

        @Override
        protected String doInBackground(String... params) {
            String btn = params[0].toString();
            String voteType = params[1].toString();

            String voteUrl = "http://echoindia.co.in/php/postVote.php";

            try {
                URL url = new URL(voteUrl);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("btn",btn);
                postDataParams.put("voteType",voteType);
                postDataParams.put("postId",postId);
                postDataParams.put("username",sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_USER_CODE,""));

                Log.e("Voting", "Params: " + postDataParams.toString());

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
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            Log.e("Voting", "Response : " + aVoid);
        }
    }



    void openPollCommentDialog(final String pollId) {
        commentDialog = new Dialog(activity);
        commentDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        commentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        commentDialog.setContentView(R.layout.dialog_poll_comment);
        commentListLL=(LinearLayout)commentDialog.findViewById(R.id.comment_list_ll);
        postCommentList = (ListView) commentDialog.findViewById(R.id.poll_comment_list);
        postCommentEdit = (EditText) commentDialog.findViewById(R.id.poll_comment_edt);
        postCommentSend = (ImageView) commentDialog.findViewById(R.id.poll_comment_send);
        postCommentSwipeRefresh = (SwipeRefreshLayout)commentDialog.findViewById(R.id.poll_comment_swipe_refresh);
        commentDialog.show();

        postCommentSwipeRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        FetchPostComment fetchPollComment=new FetchPostComment(pollId);
                        fetchPollComment.execute();
                    }
                }
        );
        postCommentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(LOG_TAG,"Send Button Clicked");
                InsertPostComment insertPollComment=new InsertPostComment(postCommentEdit.getText().toString(),pollId);
                insertPollComment.execute();

            }
        });
    }

    class FetchPostComment extends AsyncTask {

        String url_poll_comment = "http://echoindia.co.in/php/getPostComment.php";
        String postId="";

        public FetchPostComment(String postId){
            this.postId=postId;
        }

        @Override
        protected Object doInBackground(Object[] params) {

            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(url_poll_comment);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("postId",postId);
                Log.e(LOG_TAG,"URL"+url_poll_comment);
                Log.e(LOG_TAG,"PollId "+postId);
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
            setPollCommentData(o,postId);

        }
    }

    void setPollCommentData(Object o,final String pollId){
        if(commentDialog==null || !commentDialog.isShowing()) {
            openPollCommentDialog(pollId);
        }
        try {
            JSONObject jObject=new JSONObject(o.toString());
            Log.e(LOG_TAG,"PollCommentData : "+o.toString());
            String checkStatus=jObject.getString("status");
            if(checkStatus.equals("1")&&o != null){
                pollCommentListArray.clear();
                JSONArray newsArray=jObject.getJSONArray("comment");
                for(int i =0 ; i<newsArray.length();i++){
                    JSONObject pollCommentObject=newsArray.getJSONObject(i);
                    mPollCommentModel=new PollCommentModel();
                    mPollCommentModel.setPollId(pollCommentObject.getInt("PostId"));
                    mPollCommentModel.setPollCommentId(pollCommentObject.getInt("PostCommentId"));
                    mPollCommentModel.setPollCommentText(pollCommentObject.getString("PostCommentText"));
                    mPollCommentModel.setPollCommentUserPhoto(pollCommentObject.getString("UserPhoto"));
                    mPollCommentModel.setPollCommentUserName(pollCommentObject.getString("PostCommentUserName"));
                    mPollCommentModel.setPollCommentTime(pollCommentObject.getString("PostCommentTime"));
                    mPollCommentModel.setPollCommentDate(pollCommentObject.getString("PostCommentDate"));
                    pollCommentListArray.add(mPollCommentModel);
                }
                Collections.sort(pollCommentListArray,new PollCommentsComparator());
                mPollCommentAdapter = new PollCommentAdapter(activity, pollCommentListArray);
                postCommentList.setAdapter(mPollCommentAdapter);
                mPollCommentAdapter.notifyDataSetChanged();

            }
            else if(checkStatus.equals("2")){
                Toast.makeText(activity, "No Comment Found", Toast.LENGTH_SHORT).show();
            }
            else if(checkStatus.equals("0")){
                Toast.makeText(activity, "Server Connection Error", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,e.toString());
        }
        postCommentSwipeRefresh.setRefreshing(false);

    }
    public class PollCommentsComparator implements Comparator<PollCommentModel> {

        @Override
        public int compare(PollCommentModel lhs, PollCommentModel rhs) {

            return lhs.getPollCommentId()<rhs.getPollCommentId() ? 1:(lhs.getPollCommentId()==rhs.getPollCommentId()?0:-1);
        }
    }

    class InsertPostComment extends AsyncTask {

        String url_poll_comment = "http://echoindia.co.in/php/insertPostComment.php";
        String pollCommentEditText="";
        String pollId="";

        public InsertPostComment(String pollCommentEditText,String pollId){
            this.pollCommentEditText=pollCommentEditText;
            this.pollId=pollId;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(url_poll_comment);
                JSONObject postDataParams = new JSONObject();
                Date currentDate = new Date();
                String dateToday = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String timeNow = sdf.format(new Date());
                Log.e(LOG_TAG,"POSTID"+pollId);
                postDataParams.put("postId",pollId);
                postDataParams.put("username",sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_USER_CODE,""));
                postDataParams.put("comment",pollCommentEditText);
                postDataParams.put("time",timeNow);
                postDataParams.put("date",dateToday);
                Log.e(LOG_TAG,"URL"+url_poll_comment);
                Log.e(LOG_TAG,"PostParam Insert Comment "+postDataParams.toString());
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
            try {
                JSONObject jObject=new JSONObject(o.toString());
                String checkStatus=jObject.getString("status");
                if(checkStatus.equals("1")&&o != null) {
                    postCommentEdit.setText("");
                    FetchPostComment fetchPostComment=new FetchPostComment(pollId);
                    fetchPostComment.execute();

                }
                else if(checkStatus.equals("0")){
                    Toast.makeText(activity, "No Comment Found", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(activity, "Server Connection Error", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(LOG_TAG,e.toString());
            }
        }
    }

    class SharePost extends AsyncTask {

        String url_share_post = "http://echoindia.co.in/php/share.php";
        String postFullName="";
        String postId="";
        public SharePost(String postFullName,String postId){
            this.postFullName=postFullName;
            this.postId=postId;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(url_share_post);
                JSONObject postDataParams = new JSONObject();
                Date currentDate = new Date();
                String dateToday = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String timeNow = sdf.format(new Date());
                postDataParams.put("postId",postId);
                if(sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_TYPE,"").equals("REP")) {
                    postDataParams.put("postType","BUZZ");
                }
                else if(sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_TYPE,"").equals("USER")) {
                    postDataParams.put("postType","USER");
                }
                postDataParams.put("username",sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_USER_CODE,""));
                postDataParams.put("postFullName",postFullName);
                postDataParams.put("postTime",timeNow);
                postDataParams.put("postDate",dateToday);
                Log.e(LOG_TAG,"URL"+url_share_post);
                Log.e(LOG_TAG,"PostParam Share Post "+postDataParams.toString());
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
            try {
                JSONObject jObject=new JSONObject(o.toString());
                String checkStatus=jObject.getString("status");
                if(checkStatus.equals("1")&&o != null) {
                    Type type = new TypeToken<ArrayList<PostDetailModel>>() {}.getType();
                    UserDetailsModel mUserDetails=new Gson().fromJson(sharedpreferences.getString(Constants.SETTINGS_OBJ_USER, ""), UserDetailsModel.class);
                    RepDetailModel mRepDetails=new Gson().fromJson(sharedpreferences.getString(Constants.SETTINGS_OBJ_USER, ""), RepDetailModel.class);
                    buzzDetailsModels = new Gson().fromJson(sharedpreferences.getString(Constants.BUZZ_LIST, ""), type);
                    homeDetailsModels = new Gson().fromJson(sharedpreferences.getString(Constants.HOME_LIST, ""), type);
                    JSONArray jArrayMyPost=jObject.getJSONArray("Post");
                    for(int i =0 ; i<jArrayMyPost.length();i++){
                        JSONObject buzzObject=jArrayMyPost.getJSONObject(i);
                        mPostDetailModel=new PostDetailModel();
                        mPostDetailModel.setPostId(buzzObject.getString("PostId"));
                        mPostDetailModel.setPostUserName(buzzObject.getString("PostUserName"));
                        mPostDetailModel.setPostText(buzzObject.getString("PostText"));
                        mPostDetailModel.setPostTime(buzzObject.getString("PostTime"));
                        mPostDetailModel.setPostDate(buzzObject.getString("PostDate"));
                        mPostDetailModel.setPostUpVote(buzzObject.getInt("PostUpVote"));
                        mPostDetailModel.setPostDownVote(buzzObject.getInt("PostDownVote"));
                        mPostDetailModel.setPostType(buzzObject.getString("PostType"));
                        mPostDetailModel.setPostImageRef(buzzObject.getString("PostImageRef"));
                        mPostDetailModel.setIsShared(buzzObject.getString("IsShared"));
                        mPostDetailModel.setSharedCount(buzzObject.getString("ShareCount"));
                        mPostDetailModel.setSharedFrom(buzzObject.getString("SharedFrom"));
                        mPostDetailModel.setSharedFromUserName(buzzObject.getString("SharedFromUserName"));
                        mPostDetailModel.setPostUpVoteValue(false);
                        mPostDetailModel.setPostLocation(buzzObject.getString("PostLocation"));
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
                        if(sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_TYPE,"").equals("REP")) {

                            mPostDetailModel.setPostFirstName(mRepDetails.getFirstName());
                            mPostDetailModel.setPostLastName(mRepDetails.getLastName());
                            mPostDetailModel.setPostUserPhoto(mRepDetails.getUserPhoto());
                            mPostDetailModel.setPostRepParty(mRepDetails.getRepParty());
                            mPostDetailModel.setPostRepDesignation(mRepDetails.getRepDesignation());
                            buzzDetailsModels.add(0,mPostDetailModel);
                            editor.putString(Constants.BUZZ_LIST, new Gson().toJson(buzzDetailsModels));
                            Log.e(LOG_TAG,"Added to Buzz List");
                            editor.putInt(Constants.LAST_BUZZ_UPDATE,Integer.parseInt(mPostDetailModel.getPostId()));

                        }
                        else if(sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_TYPE,"").equals("USER")) {

                            mPostDetailModel.setPostFirstName(mUserDetails.getFirstName());
                            mPostDetailModel.setPostLastName(mUserDetails.getLastName());
                            mPostDetailModel.setPostUserPhoto(mUserDetails.getUserPhoto());
                            homeDetailsModels.add(0,mPostDetailModel);
                            editor.putString(Constants.HOME_LIST, new Gson().toJson(homeDetailsModels));
                            Log.e(LOG_TAG,"Added to Home List"+", Number of elements "+homeDetailsModels.size());
                            editor.putInt(Constants.LAST_USER_UPDATE,Integer.parseInt(mPostDetailModel.getPostId()));

                        }

                    }
                    editor.commit();
                    notifyDataSetChanged();
                    Toast.makeText(activity, "Post Shared Successfully", Toast.LENGTH_SHORT).show();
                }
                else if(checkStatus.equals("0")){
                    Toast.makeText(activity, "Post Share Failed", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(activity, "Server Connection Error", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(LOG_TAG,e.toString());
            }

        }
    }

    class ViewUser extends AsyncTask {

        String url_share_post = "http://echoindia.co.in/php/oneUserPost.php";
        String postUserName="";
        public ViewUser(String postUserName){
            this.postUserName=postUserName;

        }

        @Override
        protected Object doInBackground(Object[] params) {
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(url_share_post);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username",postUserName);
                Log.e(LOG_TAG,"URL"+url_share_post);
                Log.e(LOG_TAG,"View Profile "+postDataParams.toString());
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
            try {
                JSONObject jObject=new JSONObject(o.toString());
                String checkStatus=jObject.getString("status");
                if(checkStatus.equals("1")&&o != null) {

                    JSONArray jArrayMyPost=jObject.getJSONArray("Posts");
                    for(int i =0 ; i<jArrayMyPost.length();i++){
                        JSONObject buzzObject=jArrayMyPost.getJSONObject(i);
                        mPostDetailModel=new PostDetailModel();
                        mPostDetailModel.setPostId(buzzObject.getString("PostId"));
                        mPostDetailModel.setPostUserName(buzzObject.getString("PostUserName"));
                        mPostDetailModel.setPostText(buzzObject.getString("PostText"));
                        mPostDetailModel.setPostTime(buzzObject.getString("PostTime"));
                        mPostDetailModel.setPostDate(buzzObject.getString("PostDate"));
                        mPostDetailModel.setPostUpVote(buzzObject.getInt("PostUpVote"));
                        mPostDetailModel.setPostDownVote(buzzObject.getInt("PostDownVote"));
                        mPostDetailModel.setPostType(buzzObject.getString("PostType"));
                        mPostDetailModel.setPostLocation(buzzObject.getString("PostLocation"));
                        mPostDetailModel.setPostImageRef(buzzObject.getString("PostImageRef"));
                        mPostDetailModel.setIsShared(buzzObject.getString("IsShared"));
                        mPostDetailModel.setSharedCount(buzzObject.getString("ShareCount"));
                        mPostDetailModel.setSharedFrom(buzzObject.getString("SharedFrom"));
                        mPostDetailModel.setSharedFromUserName(buzzObject.getString("SharedFromUserName"));
                        mPostDetailModel.setPostFirstName(buzzObject.getString("FirstName"));
                        mPostDetailModel.setPostLastName(buzzObject.getString("LastName"));
                        mPostDetailModel.setPostUpVoteValue(false);
                        mPostDetailModel.setPostLocation(buzzObject.getString("PostLocation"));
                        mPostDetailModel.setPostDownVoteValue(false);
                        if(userType.equals("REP")){
                            mPostDetailModel.setPostRepParty(userParty);
                            mPostDetailModel.setPostRepDesignation(userDesignation);
                        }
                        mPostDetailModel.setPostUserPhoto(buzzObject.getString("UserPhoto"));
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
                        userPost.add(mPostDetailModel);
                    }
                    Intent viewProfile=new Intent(activity, OtherUserProfile.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("userPost",userPost);
                    mBundle.putString("userName",userNameViewProfile);
                    mBundle.putString("userImage",userImageViewProfile);
                    mBundle.putString("userType",userType);
                    viewProfile.putExtra("userPost",mBundle);
                    activity.startActivity(viewProfile);
                }
                else if(checkStatus.equals("0")){
                    Toast.makeText(activity, "Post Share Failed", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(activity, "Server Connection Error", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(LOG_TAG,e.toString());
            }

        }
    }


}

