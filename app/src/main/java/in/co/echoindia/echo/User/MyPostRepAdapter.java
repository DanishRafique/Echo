package in.co.echoindia.echo.User;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
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
import in.co.echoindia.echo.HomePage.BuzzImageAdapter;
import in.co.echoindia.echo.HomePage.PollCommentAdapter;
import in.co.echoindia.echo.Model.PollCommentModel;
import in.co.echoindia.echo.Model.PostDetailModel;
import in.co.echoindia.echo.Model.RepDetailModel;
import in.co.echoindia.echo.Model.UserDetailsModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

/**
 * Created by Danish Rafique on 28-04-2017.
 */

public class MyPostRepAdapter extends BaseAdapter {

    ArrayList<PostDetailModel> buzzDetailsModels = new ArrayList<>();
    Activity activity;

    TextView buzzFullName;

    CircleImageView buzzUserImage;
    TextView buzzUserName;
    TextView buzzUserDesignation;
    TextView buzzTime;
    TextView buzzText;
    ListView buzzImageListView;
    TextView buzzUpvoteValue;
    TextView buzzDownvoteValue;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    ImageView buzzImage1,buzzImage2,buzzImage3;
    RelativeLayout buzzMoreImage;
    TextView buzzMoreImageText;
    LinearLayout buzzMoreThanOneImage;
    RelativeLayout buzzMoreThanTwoImage;

    LinearLayout buzzShare;
    TextView buzzSharedFrom;


    BuzzImageAdapter mBuzzImageAdapter;
    private static final String LOG_TAG = "BuzzAdapter";
    ArrayList<PostDetailModel> homeDetailsModels = new ArrayList<>();

    LinearLayout buzzImageList;


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
    PostDetailModel mPostDetailModel;
    String postLocation;
    TextView postLocationText;
    LinearLayout postLocationll;
    public MyPostRepAdapter(Activity activity, ArrayList<PostDetailModel> buzzDetailsModels) {
        this.activity = activity;
        this.buzzDetailsModels = buzzDetailsModels;
    }

    @Override
    public int getCount() {
        if(buzzDetailsModels != null)
            return buzzDetailsModels.size();
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
        convertView = inflater.inflate(R.layout.list_buzz_child, null);
        final PostDetailModel buzzObj = buzzDetailsModels.get(position);
        buzzFullName=(TextView)convertView.findViewById(R.id.buzz_full_name);
        buzzUserImage=(CircleImageView) convertView.findViewById(R.id.buzz_user_image);
        buzzUserName=(TextView) convertView.findViewById(R.id.buzz_user_name);
        buzzUserDesignation=(TextView)convertView.findViewById(R.id.buzz_user_designation);
        buzzTime=(TextView)convertView.findViewById(R.id.buzz_time);
        buzzText=(TextView)convertView.findViewById(R.id.buzz_text);
        buzzImageList=(LinearLayout)convertView.findViewById(R.id.buzz_image_list);
        buzzUpvoteValue=(TextView)convertView.findViewById(R.id.buzz_upvote_value);
        buzzDownvoteValue=(TextView)convertView.findViewById(R.id.buzz_downvote_value);
        buzzImage1=(ImageView)convertView.findViewById(R.id.buzz_image_1);
        buzzImage2=(ImageView)convertView.findViewById(R.id.buzz_image_2);
        buzzImage3=(ImageView)convertView.findViewById(R.id.buzz_image_3);

        buzzMoreImageText=(TextView)convertView.findViewById(R.id.buzz_more_image_text);
        buzzMoreImage=(RelativeLayout)convertView.findViewById(R.id.buzz_more_image);
        buzzMoreThanOneImage=(LinearLayout)convertView.findViewById(R.id.buzz_more_than_one_image);
        buzzMoreThanTwoImage=(RelativeLayout)convertView.findViewById(R.id.buzz_more_than_two_image);

        buzzShare=(LinearLayout)convertView.findViewById(R.id.buzz_ll_share);
        buzzSharedFrom=(TextView)convertView.findViewById(R.id.buzz_shared_from);
        postLocationll=(LinearLayout)convertView.findViewById(R.id.buzz_location_ll);
        postLocationText=(TextView)convertView.findViewById(R.id.buzz_location_text);

        sharedpreferences = AppUtil.getAppPreferences(activity);
        editor = sharedpreferences.edit();

        final LinearLayout  buzzShareButton=(LinearLayout)convertView.findViewById(R.id.buzz_share_button);



        if(buzzObj.getIsShared().equals("0")){
            buzzShare.setVisibility(View.GONE);
        }
        else if(buzzObj.getIsShared().equals("1")){
            buzzSharedFrom.setText(buzzObj.getSharedFrom());
        }

        buzzFullName.setText(buzzObj.getPostFirstName()+" "+buzzObj.getPostLastName());

        postLocation=buzzObj.getPostLocation();
        if(postLocation==null || postLocation.isEmpty() || postLocation.equals(" ") || postLocation.equals("")){
            postLocationll.setVisibility(View.GONE);
        }
        else{
            postLocationText.setText(postLocation);
        }

        Glide.with(activity).load(buzzObj.getPostUserPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).into(buzzUserImage);
        buzzUserName.setText(buzzObj.getPostUserName());
        buzzUserDesignation.setText(buzzObj.getPostRepDesignation()+" , "+buzzObj.getPostRepParty());
        buzzTime.setText(buzzObj.getPostTime());
        buzzText.setText(buzzObj.getPostText());
        buzzUpvoteValue.setText(String.valueOf(buzzObj.getPostUpVote()));
        buzzDownvoteValue.setText(String.valueOf(buzzObj.getPostDownVote()));
        final LinearLayout postCommentButton =(LinearLayout)convertView.findViewById(R.id.buzz_comment_link);
        final String postId=buzzObj.getPostId();
        final String fullnameStr=buzzObj.getPostFirstName()+" "+buzzObj.getPostLastName();


        final ArrayList<String> buzzImageArrayList = buzzObj.getPostImages();
        if(buzzImageArrayList!=null){
            for(int i=0;i<buzzImageArrayList.size();i++){
                if(i==0){
                    buzzImage1.setVisibility(View.VISIBLE);
                    Glide.with(activity).load(buzzImageArrayList.get(i)).diskCacheStrategy(DiskCacheStrategy.ALL).into(buzzImage1);
                }
                else if(i==1){
                    buzzMoreThanOneImage.setVisibility(View.VISIBLE);
                    buzzImage2.setVisibility(View.VISIBLE);
                    Glide.with(activity).load(buzzImageArrayList.get(i)).diskCacheStrategy(DiskCacheStrategy.ALL).into(buzzImage2);
                }
                else if(i==2){
                    buzzMoreThanTwoImage.setVisibility(View.VISIBLE);
                    buzzImage3.setVisibility(View.VISIBLE);
                    Glide.with(activity).load(buzzImageArrayList.get(i)).diskCacheStrategy(DiskCacheStrategy.ALL).into(buzzImage3);
                }
                else if(i>2){
                    buzzMoreImage.setVisibility(View.VISIBLE);
                    buzzMoreImageText.setVisibility(View.VISIBLE);
                    int numberOfImage=buzzImageArrayList.size()-3;
                    buzzMoreImageText.setText(String.valueOf(numberOfImage)+"+");
                }
            }
        }
        else{
            buzzImageList.setVisibility(View.GONE);
        }



        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FetchPostComment fetchPollComment=new FetchPostComment(postId);
                fetchPollComment.execute();
            }
        });


        final ToggleButton buzzUpvote = (ToggleButton) convertView.findViewById(R.id.buzz_upvote);
        buzzUpvote.setTag(String.valueOf(buzzObj.getPostId()));
        final ToggleButton buzzDownvote = (ToggleButton) convertView.findViewById(R.id.buzz_downvote);
        buzzDownvote.setTag(String.valueOf(buzzObj.getPostId()));

        if(buzzObj.isPostUpVoteValue()){
            buzzUpvote.setChecked(true);
            buzzDownvote.setEnabled(false);
        }
        else if(buzzObj.isPostDownVoteValue()){
            buzzDownvote.setChecked(true);
            buzzUpvote.setEnabled(false);
        }
        buzzUpvote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                postVote postVote = new postVote();
                TextView textView = null;
                tempBtn = (ToggleButton) buttonView;
                ViewGroup view = (ViewGroup) tempBtn.getParent();
                textView = (TextView) view.findViewById(R.id.buzz_upvote_value);
                Log.e("Voting...", textView.getText().toString());
                int upvote = Integer.parseInt(textView.getText().toString());

                ViewGroup rootParent = (ViewGroup) view.getParent();
                ToggleButton t = (ToggleButton) rootParent.findViewById(R.id.buzz_downvote);

                if(!isChecked){
                    t.setEnabled(true);
                    upvote --;
                    buzzObj.setPostUpVoteValue(false);
                    buzzObj.setPostUpVote(upvote);
                    postVote.execute("up", "none");
                    textView.setText(upvote + "");
                }
                else {
                    t.setEnabled(false);
                    upvote ++;
                    buzzObj.setPostUpVoteValue(true);
                    buzzObj.setPostUpVote(upvote);
                    postVote.execute("up", "up");
                    textView.setText(upvote + "");
                }
            }
        });

        buzzDownvote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                postVote postVote = new postVote();
                TextView textView = null;
                tempBtn = (ToggleButton) buttonView;
                ViewGroup view = (ViewGroup) tempBtn.getParent();
                textView = (TextView) view.findViewById(R.id.buzz_downvote_value);
                Log.e("Voting...", textView.getText().toString());
                int downvote = Integer.parseInt(textView.getText().toString());
                ViewGroup rootParent = (ViewGroup) view.getParent();
                ToggleButton t = (ToggleButton) rootParent.findViewById(R.id.buzz_upvote);
                if(!isChecked){
                    t.setEnabled(true);
                    downvote --;
                    buzzObj.setPostDownVoteValue(false);
                    buzzObj.setPostDownVote(downvote);
                    postVote.execute("down", "none");
                    textView.setText(downvote + "");
                }
                else {
                    t.setEnabled(false);
                    downvote ++;
                    buzzObj.setPostDownVoteValue(true);
                    buzzObj.setPostDownVote(downvote);
                    postVote.execute("down", "down");
                    textView.setText(downvote + "");
                }
            }
        });

        postUpdatedList.clear();

        for(int i=0;i<buzzDetailsModels.size();i++){
            if(i!=position) {
                postUpdatedList.add(buzzDetailsModels.get(i));
            }
            else if(i==position){
                postUpdatedList.add(buzzObj);
            }
        }
       // editor.putString(Constants.MY_POST, new Gson().toJson(postUpdatedList));
       // editor.commit();

        buzzShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharePost mSharePost=new SharePost(fullnameStr,postId);
                mSharePost.execute();
            }
        });



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


                        mPostDetailModel.setPostLocation(buzzObject.getString("PostLocation"));



                        mPostDetailModel.setSharedFromUserName(buzzObject.getString("SharedFromUserName"));
                        mPostDetailModel.setPostUpVoteValue(false);
                        mPostDetailModel.setPostDownVoteValue(false);
                        JSONArray postImageArray=buzzObject.getJSONArray("images");
                        ArrayList<String> postImageArrayList = new ArrayList<>();
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
                            editor.putInt(Constants.LAST_BUZZ_UPDATE,Integer.parseInt(mPostDetailModel.getPostId()));
                        }
                        else if(sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_TYPE,"").equals("USER")) {
                            mPostDetailModel.setPostFirstName(mUserDetails.getFirstName());
                            mPostDetailModel.setPostLastName(mUserDetails.getLastName());
                            mPostDetailModel.setPostUserPhoto(mUserDetails.getUserPhoto());
                            homeDetailsModels.add(0,mPostDetailModel);
                            editor.putString(Constants.HOME_LIST, new Gson().toJson(homeDetailsModels));
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


}
