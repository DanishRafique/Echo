package in.co.echoindia.echo.HomePage;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import in.co.echoindia.echo.Model.PollCommentModel;
import in.co.echoindia.echo.Model.PollDetailsModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by Danish Rafique on 04-04-2017.
 */

public class PollAdapter extends BaseAdapter {

    ArrayList<PollDetailsModel> pollDetailModel = new ArrayList<>();
    private Context activity;

    TextView pollTitle;
    ImageView pollImage;
    TextView pollDescription;
    TextView pollVendor;
    ImageView pollVendorLogo;
    TextView pollOptionOneVote, pollOptionTwoVote;
   // Button pollOptionOneText, pollOptionTwoText;
    RadioButton pollOptionOneText, pollOptionTwoText;
    TextRoundCornerProgressBar pollBarOne , pollBarTwo;

    TextView pollQuestion;
    int colorCodePrimary[]={R.color.custom_progress_green_header,R.color.custom_progress_orange_header,R.color.custom_progress_blue_header,R.color.custom_progress_purple_header,R.color.custom_progress_red_header};
    int colorCodeSecondary[]={R.color.custom_progress_green_progress,R.color.custom_progress_orange_progress,R.color.custom_progress_blue_progress,R.color.custom_progress_purple_progress,R.color.custom_progress_red_progress};
    private static final String LOG_TAG = "PollAdapter";
    SegmentedGroup segmentedPoll;
    PollCommentModel mPollCommentModel;

    Dialog commentDialog;
    ListView pollCommentList;
    EditText pollCommentEdit;
    ImageView pollCommentSend;
    SwipeRefreshLayout pollCommentSwipeRefresh;

    ArrayList<PollCommentModel> pollCommentListArray = new ArrayList<PollCommentModel>();

    PollCommentAdapter mPollCommentAdapter;

    int totalVote;
    float optionOnePercent, optionTwoPercent;
    int pollOptionOneColor,pollOptionTwoColor;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String pollId;

    public PollAdapter(Context activity, ArrayList<PollDetailsModel> pollDetailModel) {
        this.activity = activity;
        this.pollDetailModel = pollDetailModel;
    }

    @Override
    public int getCount() {
        if (pollDetailModel != null)
            return pollDetailModel.size();
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

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_poll_child, null);
        sharedpreferences = AppUtil.getAppPreferences(activity);

        final PollDetailsModel pollObj = pollDetailModel.get(position);
        pollTitle = (TextView) convertView.findViewById(R.id.poll_title);
        pollImage = (ImageView) convertView.findViewById(R.id.poll_image);
        pollDescription = (TextView) convertView.findViewById(R.id.poll_description);
        pollVendor = (TextView) convertView.findViewById(R.id.poll_vendor);
        pollVendorLogo = (ImageView) convertView.findViewById(R.id.poll_vendor_logo);
        pollOptionOneVote = (TextView) convertView.findViewById(R.id.poll_option_one_vote);
        pollOptionTwoVote = (TextView) convertView.findViewById(R.id.poll_option_two_vote);
        pollOptionOneText = (RadioButton) convertView.findViewById(R.id.poll_option_one_text);
        pollOptionTwoText = (RadioButton) convertView.findViewById(R.id.poll_option_two_text);
        pollQuestion = (TextView)convertView.findViewById(R.id.poll_question);
        segmentedPoll = (SegmentedGroup)convertView.findViewById(R.id.segmented_poll);
        final LinearLayout pollShareButton =(LinearLayout)convertView.findViewById(R.id.poll_share_button);
        final LinearLayout pollCommentButton =(LinearLayout)convertView.findViewById(R.id.poll_comment_button);


        pollTitle.setText(pollObj.getPollTitle());
        pollDescription.setText(pollObj.getPollDescription());
        pollVendor.setText(pollObj.getPollVendor());
        Glide.with(activity).load(pollObj.getPollImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(pollImage);
        Glide.with(activity).load(pollObj.getPollVendorLogo()).diskCacheStrategy(DiskCacheStrategy.ALL).into(pollVendorLogo);
        pollOptionOneVote.setText(String.valueOf(pollObj.getPollOptionOneVote()));
        pollOptionTwoVote.setText(String.valueOf(pollObj.getPollOptionTwoVote()));
        pollOptionOneText.setText(pollObj.getPollOptionOneText());
        pollOptionTwoText.setText(pollObj.getPollOptionTwoText());
        pollId=pollObj.getPollId();

        pollQuestion.setText(pollObj.getPollQuestion());

        totalVote=pollObj.getPollOptionOneVote()+pollObj.getPollOptionTwoVote();

        pollBarOne=(TextRoundCornerProgressBar)convertView.findViewById(R.id.poll_bar_one);
        pollBarTwo=(TextRoundCornerProgressBar)convertView.findViewById(R.id.poll_bar_two);

        pollBarOne.setMax(totalVote);
        pollBarTwo.setMax(totalVote);
        pollBarOne.setProgress(pollObj.getPollOptionOneVote());
        pollBarTwo.setProgress(pollObj.getPollOptionTwoVote());
        pollBarOne.setSecondaryProgress(pollObj.getPollOptionOneVote()+100);
        pollBarTwo.setSecondaryProgress(pollObj.getPollOptionTwoVote()+100);

        optionOnePercent=((float)pollObj.getPollOptionOneVote()/(float)totalVote)*100;
        optionTwoPercent=((float)pollObj.getPollOptionTwoVote()/(float)totalVote)*100;

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);

        pollBarOne.setProgressText(df.format(optionOnePercent)+"%");
        pollBarTwo.setProgressText(df.format(optionTwoPercent)+"%");

        pollOptionOneColor=pollObj.getPollOptionOneColor()-1;
        pollOptionTwoColor=pollObj.getPollOptionTwoColor()-1;
        pollBarOne.setProgressColor(ContextCompat.getColor(activity,colorCodePrimary[pollOptionOneColor]));
        pollBarTwo.setProgressColor(ContextCompat.getColor(activity,colorCodePrimary[pollOptionTwoColor]));
        pollBarOne.setSecondaryProgressColor(ContextCompat.getColor(activity,colorCodeSecondary[pollOptionOneColor]));
        pollBarTwo.setSecondaryProgressColor(ContextCompat.getColor(activity,colorCodeSecondary[pollOptionTwoColor]));

        segmentedPoll.setTintColor(ContextCompat.getColor(activity,R.color.colorPrimary));
        pollShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, pollObj.getPollDescription());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, pollObj.getPollTitle());
                activity.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        pollCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(LOG_TAG,"Comment Button Clicked");
                FetchPollComment fetchPollComment=new FetchPollComment();
                fetchPollComment.execute();
            }
        });

        return convertView;
    }

    void openPollCommentDialog() {
        commentDialog = new Dialog(activity);
        commentDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        commentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        commentDialog.setContentView(R.layout.dialog_poll_comment);
        pollCommentList = (ListView) commentDialog.findViewById(R.id.poll_comment_list);
        pollCommentEdit = (EditText) commentDialog.findViewById(R.id.poll_comment_edt);
        pollCommentSend = (ImageView) commentDialog.findViewById(R.id.poll_comment_send);
        pollCommentSwipeRefresh = (SwipeRefreshLayout)commentDialog.findViewById(R.id.poll_comment_swipe_refresh);
        commentDialog.show();
        pollCommentSwipeRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.e(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
                        FetchPollComment fetchPollComment=new FetchPollComment();
                        fetchPollComment.execute();
                    }
                }
        );
        pollCommentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(LOG_TAG,"Send Button Clicked");
                InsertPollComment insertPollComment=new InsertPollComment(pollCommentEdit.getText().toString());
                insertPollComment.execute();

            }
        });
    }

    class FetchPollComment extends AsyncTask {

        String url_poll_comment = "http://echoindia.co.in/php/getComment.php";

        @Override
        protected Object doInBackground(Object[] params) {

            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(url_poll_comment);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("pollId",pollId);
                Log.e(LOG_TAG,"URL"+url_poll_comment);
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
            setPollCommentData(o);

        }
    }

    void setPollCommentData(Object o){
        if(commentDialog==null || !commentDialog.isShowing()) {
            openPollCommentDialog();
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
                    mPollCommentModel.setPollId(pollCommentObject.getInt("PollId"));
                    mPollCommentModel.setPollCommentId(pollCommentObject.getInt("PollCommentId"));
                    mPollCommentModel.setPollCommentText(pollCommentObject.getString("PollCommentText"));
                   // mPollCommentModel.setPollCommentUserPhoto(pollCommentObject.getString("PollCommentUserPhoto"));
                    mPollCommentModel.setPollCommentUserName(pollCommentObject.getString("PollCommentUserName"));
                    mPollCommentModel.setPollCommentTime(pollCommentObject.getString("PollCommentTime"));
                    mPollCommentModel.setPollCommentDate(pollCommentObject.getString("PollCommentDate"));
                    pollCommentListArray.add(mPollCommentModel);
                }
                Collections.sort(pollCommentListArray,new PollCommentsComparator());
                mPollCommentAdapter = new PollCommentAdapter(activity, pollCommentListArray);
                pollCommentList.setAdapter(mPollCommentAdapter);
                mPollCommentAdapter.notifyDataSetChanged();

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
        pollCommentSwipeRefresh.setRefreshing(false);

    }
    public class PollCommentsComparator implements Comparator<PollCommentModel> {

        @Override
        public int compare(PollCommentModel lhs, PollCommentModel rhs) {

            return lhs.getPollCommentId()<rhs.getPollCommentId() ? 1:(lhs.getPollCommentId()==rhs.getPollCommentId()?0:-1);
        }
    }

    class InsertPollComment extends AsyncTask {

        String url_poll_comment = "http://echoindia.co.in/php/insertComment.php";
        String pollCommentEditText="";

        public InsertPollComment(String pollCommentEditText){
            this.pollCommentEditText=pollCommentEditText;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            Log.e(LOG_TAG,"INSIDE INSERT COMMENT");
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(url_poll_comment);
                JSONObject postDataParams = new JSONObject();
                Date currentDate = new Date();
                String dateToday = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String timeNow = sdf.format(new Date());
                Log.e(LOG_TAG,"POLLID"+pollId);
                Log.e(LOG_TAG,"username"+sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_USER_CODE,""));
                postDataParams.put("pollId",pollId);
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
            Log.e(LOG_TAG,"POLL INSERT COMMENT : "+o.toString());
            JSONObject jObject=new JSONObject(o.toString());
            String checkStatus=jObject.getString("status");
            if(checkStatus.equals("1")&&o != null) {
                pollCommentEdit.setText("");
                FetchPollComment fetchPollComment=new FetchPollComment();
                fetchPollComment.execute();

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