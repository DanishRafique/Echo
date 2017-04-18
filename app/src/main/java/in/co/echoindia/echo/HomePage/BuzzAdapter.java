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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
 * Created by Danish Rafique on 18-04-2017.
 */

public class BuzzAdapter extends BaseAdapter {

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


    BuzzImageAdapter mBuzzImageAdapter;
    private static final String LOG_TAG = "BuzzAdapter";

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





    public BuzzAdapter(Activity activity, ArrayList<PostDetailModel> buzzDetailsModels) {
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

        sharedpreferences = AppUtil.getAppPreferences(activity);
        editor = sharedpreferences.edit();

        buzzFullName.setText(buzzObj.getPostFirstName()+" "+buzzObj.getPostLastName());

        Glide.with(activity).load(buzzObj.getPostUserPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).into(buzzUserImage);
        buzzUserName.setText(buzzObj.getPostUserName());
        buzzUserDesignation.setText(buzzObj.getPostRepDesignation()+" , "+buzzObj.getPostRepParty());
        buzzTime.setText(buzzObj.getPostTime());
        buzzText.setText(buzzObj.getPostText());
        buzzUpvoteValue.setText(String.valueOf(buzzObj.getPostUpVote()));
        buzzDownvoteValue.setText(String.valueOf(buzzObj.getPostDownVote()));
        final LinearLayout postCommentButton =(LinearLayout)convertView.findViewById(R.id.buzz_comment_link);
        final String postId=buzzObj.getPostId();


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

        return convertView;
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
