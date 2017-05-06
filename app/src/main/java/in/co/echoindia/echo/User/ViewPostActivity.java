package in.co.echoindia.echo.User;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import in.co.echoindia.echo.HomePage.BuzzImageAdapter;
import in.co.echoindia.echo.HomePage.PollCommentAdapter;
import in.co.echoindia.echo.Model.PollCommentModel;
import in.co.echoindia.echo.Model.PostDetailModel;
import in.co.echoindia.echo.Model.RepDetailModel;
import in.co.echoindia.echo.Model.UserDetailsModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

public class ViewPostActivity extends AppCompatActivity {

    ArrayList<PostDetailModel> homeDetailsModels = new ArrayList<>();
    ArrayList<PostDetailModel> buzzDetailsModels = new ArrayList<>();

    TextView homeFullName;
    ArrayList<PostDetailModel> homeListArray = new ArrayList<>();

    CircleImageView homeUserImage;
    TextView homeUserName;
    TextView homeTime;
    TextView homeText;
    ListView homeImageListView;
    TextView homeUpvoteValue;
    TextView homeDownvoteValue;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    ImageView homeImage1,homeImage2,homeImage3,homeImage4,homeImage5;

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
    TextView buzz_user_designation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle mBundle=getIntent().getExtras();
        Bundle repBundle=mBundle.getBundle("postObj");
        final PostDetailModel homeObj=(PostDetailModel)repBundle.getSerializable("postObj");

        homeFullName=(TextView)findViewById(R.id.home_full_name);
        homeUserImage=(CircleImageView)findViewById(R.id.home_user_image);
        homeUserName=(TextView)findViewById(R.id.home_user_name);
        homeTime=(TextView)findViewById(R.id.home_time);
        homeText=(TextView)findViewById(R.id.home_text);
        homeImageList=(LinearLayout)findViewById(R.id.home_image_list);
        homeUpvoteValue=(TextView)findViewById(R.id.home_upvote_value);
        homeDownvoteValue=(TextView)findViewById(R.id.home_downvote_value);
        homeImage1=(ImageView)findViewById(R.id.home_image_1);
        homeImage2=(ImageView)findViewById(R.id.home_image_2);
        homeImage3=(ImageView)findViewById(R.id.home_image_3);
        homeImage4=(ImageView)findViewById(R.id.home_image_4);
        homeImage5=(ImageView)findViewById(R.id.home_image_5);

        homeShare=(LinearLayout)findViewById(R.id.home_ll_share);
        homeSharedFrom=(TextView)findViewById(R.id.home_shared_from);
        postLocationll=(LinearLayout)findViewById(R.id.home_location_ll);
        postLocationText=(TextView)findViewById(R.id.home_location_text);
        buzz_user_designation=(TextView)findViewById(R.id.buzz_user_designation);
        final LinearLayout buzzShareButton=(LinearLayout)findViewById(R.id.home_share_button);
        sharedpreferences = AppUtil.getAppPreferences(this);
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

        Glide.with(this).load(homeObj.getPostUserPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).into(homeUserImage);
        homeUserName.setText(homeObj.getPostUserName());

        homeTime.setText(homeObj.getPostTime());
        homeText.setText(homeObj.getPostText());
        homeUpvoteValue.setText(String.valueOf(homeObj.getPostUpVote()));
        homeDownvoteValue.setText(String.valueOf(homeObj.getPostDownVote()));
        final LinearLayout postCommentButton =(LinearLayout)findViewById(R.id.home_comment_link);
        final String postId=homeObj.getPostId();
        final String fullNameStr=homeObj.getPostFirstName()+" "+homeObj.getPostLastName();


        final ArrayList<String> homeImageArrayList = homeObj.getPostImages();
        if(homeImageArrayList!=null){
            for(int i=0;i<homeImageArrayList.size();i++){
                if(i==0){
                    homeImageList.setVisibility(View.VISIBLE);
                    homeImage1.setVisibility(View.VISIBLE);
                    Glide.with(this).load(homeImageArrayList.get(i)).diskCacheStrategy(DiskCacheStrategy.ALL).into(homeImage1);
                }
                else if(i==1){
                    homeImage2.setVisibility(View.VISIBLE);
                    Glide.with(this).load(homeImageArrayList.get(i)).diskCacheStrategy(DiskCacheStrategy.ALL).into(homeImage2);
                }
                else if(i==2){
                    homeImage3.setVisibility(View.VISIBLE);
                    Glide.with(this).load(homeImageArrayList.get(i)).diskCacheStrategy(DiskCacheStrategy.ALL).into(homeImage3);
                }
                else if(i==3){
                    homeImage4.setVisibility(View.VISIBLE);
                    Glide.with(this).load(homeImageArrayList.get(i)).diskCacheStrategy(DiskCacheStrategy.ALL).into(homeImage4);
                }
                else if(i==4){
                    homeImage5.setVisibility(View.VISIBLE);
                    Glide.with(this).load(homeImageArrayList.get(i)).diskCacheStrategy(DiskCacheStrategy.ALL).into(homeImage5);
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


        final ToggleButton homeUpvote = (ToggleButton) findViewById(R.id.home_upvote);
        homeUpvote.setTag(String.valueOf(homeObj.getPostId()));
        final ToggleButton homeDownvote = (ToggleButton)findViewById(R.id.home_downvote);
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

        buzzShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharePost mSharePost=new SharePost(fullNameStr,postId);
                mSharePost.execute();
            }
        });

        if(homeObj.getPostType().equals("BUZZ")){
            buzz_user_designation.setVisibility(View.VISIBLE);
            buzz_user_designation.setText(homeObj.getPostRepDesignation()+" , "+homeObj.getPostRepParty());
        }



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
            if (aVoid != null) {
                Log.e("Voting", "Response : " + aVoid);
            }
            else {
                Toast.makeText(ViewPostActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }



    void openPollCommentDialog(final String pollId) {
        commentDialog = new Dialog(this);
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
            if(o!=null) {
                setPollCommentData(o, postId);
            }else {
                Toast.makeText(ViewPostActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
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
                mPollCommentAdapter = new PollCommentAdapter(this, pollCommentListArray);
                postCommentList.setAdapter(mPollCommentAdapter);
                mPollCommentAdapter.notifyDataSetChanged();

            }
            else if(checkStatus.equals("2")){
                Toast.makeText(this, "No Comment Found", Toast.LENGTH_SHORT).show();
            }
            else if(checkStatus.equals("0")){
                Toast.makeText(this, "Server Connection Error", Toast.LENGTH_SHORT).show();
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
            if(o!=null) {
                try {
                    JSONObject jObject = new JSONObject(o.toString());
                    String checkStatus = jObject.getString("status");
                    if (checkStatus.equals("1") && o != null) {
                        postCommentEdit.setText("");
                        FetchPostComment fetchPostComment = new FetchPostComment(pollId);
                        fetchPostComment.execute();

                    } else if (checkStatus.equals("0")) {
                        Toast.makeText(ViewPostActivity.this, "No Comment Found", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ViewPostActivity.this, "Server Connection Error", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, e.toString());
                }
            }
            else {
                Toast.makeText(ViewPostActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
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
            if(o!=null) {
                try {
                    JSONObject jObject = new JSONObject(o.toString());
                    String checkStatus = jObject.getString("status");
                    if (checkStatus.equals("1") && o != null) {
                        Type type = new TypeToken<ArrayList<PostDetailModel>>() {
                        }.getType();
                        UserDetailsModel mUserDetails = new Gson().fromJson(sharedpreferences.getString(Constants.SETTINGS_OBJ_USER, ""), UserDetailsModel.class);
                        RepDetailModel mRepDetails = new Gson().fromJson(sharedpreferences.getString(Constants.SETTINGS_OBJ_USER, ""), RepDetailModel.class);
                        buzzDetailsModels = new Gson().fromJson(sharedpreferences.getString(Constants.BUZZ_LIST, ""), type);
                        homeDetailsModels = new Gson().fromJson(sharedpreferences.getString(Constants.HOME_LIST, ""), type);
                        JSONArray jArrayMyPost = jObject.getJSONArray("Post");
                        for (int i = 0; i < jArrayMyPost.length(); i++) {
                            JSONObject buzzObject = jArrayMyPost.getJSONObject(i);
                            mPostDetailModel = new PostDetailModel();
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
                            JSONArray postImageArray = buzzObject.getJSONArray("images");
                            ArrayList<String> postImageArrayList = new ArrayList<>();
                            for (int j = 0; j < postImageArray.length(); j++) {
                                postImageArrayList.add(postImageArray.getString(j));
                            }
                            if (postImageArray.length() > 0) {
                                mPostDetailModel.setPostImages(postImageArrayList);
                            } else {
                                mPostDetailModel.setPostImages(null);
                            }
                            if (sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_TYPE, "").equals("REP")) {

                                mPostDetailModel.setPostFirstName(mRepDetails.getFirstName());
                                mPostDetailModel.setPostLastName(mRepDetails.getLastName());
                                mPostDetailModel.setPostUserPhoto(mRepDetails.getUserPhoto());
                                mPostDetailModel.setPostRepParty(mRepDetails.getRepParty());
                                mPostDetailModel.setPostRepDesignation(mRepDetails.getRepDesignation());
                                buzzDetailsModels.add(0, mPostDetailModel);
                                editor.putString(Constants.BUZZ_LIST, new Gson().toJson(buzzDetailsModels));
                                Log.e(LOG_TAG, "Added to Buzz List");
                                editor.putInt(Constants.LAST_BUZZ_UPDATE, Integer.parseInt(mPostDetailModel.getPostId()));

                            } else if (sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_TYPE, "").equals("USER")) {

                                mPostDetailModel.setPostFirstName(mUserDetails.getFirstName());
                                mPostDetailModel.setPostLastName(mUserDetails.getLastName());
                                mPostDetailModel.setPostUserPhoto(mUserDetails.getUserPhoto());
                                homeDetailsModels.add(0, mPostDetailModel);
                                editor.putString(Constants.HOME_LIST, new Gson().toJson(homeDetailsModels));
                                Log.e(LOG_TAG, "Added to Home List" + ", Number of elements " + homeDetailsModels.size());
                                editor.putInt(Constants.LAST_USER_UPDATE, Integer.parseInt(mPostDetailModel.getPostId()));

                            }

                        }
                        editor.commit();
                        Toast.makeText(ViewPostActivity.this, "Post Shared Successfully", Toast.LENGTH_SHORT).show();
                    } else if (checkStatus.equals("0")) {
                        Toast.makeText(ViewPostActivity.this, "Post Share Failed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ViewPostActivity.this, "Server Connection Error", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, e.toString());
                }
            }else {
                Toast.makeText(ViewPostActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
