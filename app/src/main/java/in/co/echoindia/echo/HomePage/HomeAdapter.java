package in.co.echoindia.echo.HomePage;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;
import in.co.echoindia.echo.Model.PollCommentModel;
import in.co.echoindia.echo.Model.PostDetailModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

/**
 * Created by Danish Rafique on 21-04-2017.
 */

public class HomeAdapter extends BaseAdapter {
    ArrayList<PostDetailModel> homeDetailsModels = new ArrayList<>();
    Activity activity;

    TextView homeFullName;

    CircleImageView homeUserImage;
    TextView homeUserName;
    TextView homeTime;
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
        homeTime=(TextView)convertView.findViewById(R.id.home_time);
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
        sharedpreferences = AppUtil.getAppPreferences(activity);
        editor = sharedpreferences.edit();

        if(homeObj.getIsShared().equals("0")){
            homeShare.setVisibility(View.GONE);
        }
        else if(homeObj.getIsShared().equals("1")){
            homeSharedFrom.setText(homeObj.getSharedFrom());
        }

        homeFullName.setText(homeObj.getPostFirstName()+" "+homeObj.getPostLastName());

        Glide.with(activity).load(homeObj.getPostUserPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).into(homeUserImage);
        homeUserName.setText(homeObj.getPostUserName());

        homeTime.setText(homeObj.getPostTime());
        homeText.setText(homeObj.getPostText());
        homeUpvoteValue.setText(String.valueOf(homeObj.getPostUpVote()));
        homeDownvoteValue.setText(String.valueOf(homeObj.getPostDownVote()));
        final LinearLayout postCommentButton =(LinearLayout)convertView.findViewById(R.id.home_comment_link);
        final String postId=homeObj.getPostId();


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
        }
        else if(homeObj.isPostDownVoteValue()){
            homeDownvote.setChecked(true);
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
                ToggleButton t = (ToggleButton) rootParent.findViewById(R.id.home_upvote);

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

}

