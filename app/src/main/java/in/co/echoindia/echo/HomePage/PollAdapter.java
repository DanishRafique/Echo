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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import in.co.echoindia.echo.Model.PollCommentModel;
import in.co.echoindia.echo.Model.PollDetailsModel;
import in.co.echoindia.echo.Model.PollVoteModel;
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
    TextView pollTimeline;
    //TextView pollOptionOneVote, pollOptionTwoVote;
   // Button pollOptionOneText, pollOptionTwoText;
    //RadioButton pollOptionOneText, pollOptionTwoText;
    TextRoundCornerProgressBar pollBarOne , pollBarTwo;

    TextView pollQuestion;
    int colorCodePrimary[]={R.color.custom_progress_green_header,R.color.custom_progress_orange_header,R.color.custom_progress_blue_header,R.color.custom_progress_purple_header,R.color.custom_progress_red_header};
    int colorCodeSecondary[]={R.color.custom_progress_green_progress,R.color.custom_progress_orange_progress,R.color.custom_progress_blue_progress,R.color.custom_progress_purple_progress,R.color.custom_progress_red_progress};
    private static final String LOG_TAG = "PollAdapter";

    PollCommentModel mPollCommentModel;

    Dialog commentDialog;
    ListView pollCommentList;
    EditText pollCommentEdit;
    ImageView pollCommentSend;
    SwipeRefreshLayout pollCommentSwipeRefresh;

    ArrayList<PollCommentModel> pollCommentListArray = new ArrayList<PollCommentModel>();

    PollCommentAdapter mPollCommentAdapter;
    LinearLayout pollButton;

    int totalVote;
    float optionOnePercent, optionTwoPercent;
    int pollOptionOneColor,pollOptionTwoColor;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

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
        editor = sharedpreferences.edit();

        final PollDetailsModel pollObj = pollDetailModel.get(position);
        pollTitle = (TextView) convertView.findViewById(R.id.poll_title);
        pollImage = (ImageView) convertView.findViewById(R.id.poll_image);
        pollDescription = (TextView) convertView.findViewById(R.id.poll_description);
        pollVendor = (TextView) convertView.findViewById(R.id.poll_vendor);
        pollVendorLogo = (ImageView) convertView.findViewById(R.id.poll_vendor_logo);
      final TextView  pollOptionOneVote = (TextView) convertView.findViewById(R.id.poll_option_one_vote);
        final TextView pollOptionTwoVote = (TextView) convertView.findViewById(R.id.poll_option_two_vote);
       final RadioButton pollOptionOneText = (RadioButton) convertView.findViewById(R.id.poll_option_one_text);
       final RadioButton pollOptionTwoText = (RadioButton) convertView.findViewById(R.id.poll_option_two_text);
        pollQuestion = (TextView)convertView.findViewById(R.id.poll_question);
        final SegmentedGroup segmentedPoll = (SegmentedGroup)convertView.findViewById(R.id.segmented_poll);
        pollButton=(LinearLayout)convertView.findViewById(R.id.poll_buttons);
        final LinearLayout pollShareButton =(LinearLayout)convertView.findViewById(R.id.poll_share_button);
        final LinearLayout pollCommentButton =(LinearLayout)convertView.findViewById(R.id.poll_comment_button);
        pollTimeline=(TextView)convertView.findViewById(R.id.poll_timeline);

        pollTitle.setText(pollObj.getPollTitle());
        pollDescription.setText(pollObj.getPollDescription());
        pollVendor.setText(pollObj.getPollVendor());
        Glide.with(activity).load(pollObj.getPollImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(pollImage);
        Glide.with(activity).load(pollObj.getPollVendorLogo()).diskCacheStrategy(DiskCacheStrategy.ALL).into(pollVendorLogo);
        pollOptionOneVote.setText(String.valueOf(pollObj.getPollOptionOneVote()));
        pollOptionTwoVote.setText(String.valueOf(pollObj.getPollOptionTwoVote()));
        pollOptionOneText.setText(pollObj.getPollOptionOneText());
        pollOptionTwoText.setText(pollObj.getPollOptionTwoText());
        final String pollId=pollObj.getPollId();

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
        //Log.e(LOG_TAG,position+" "+pollOptionOneColor+" "+pollOptionTwoColor);
        pollBarOne.setProgressColor(ContextCompat.getColor(activity,colorCodePrimary[pollOptionOneColor]));
        pollBarTwo.setProgressColor(ContextCompat.getColor(activity,colorCodePrimary[pollOptionTwoColor]));
        pollBarOne.setSecondaryProgressColor(ContextCompat.getColor(activity,colorCodeSecondary[pollOptionOneColor]));
        pollBarTwo.setSecondaryProgressColor(ContextCompat.getColor(activity,colorCodeSecondary[pollOptionTwoColor]));


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
                FetchPollComment fetchPollComment=new FetchPollComment(pollId);
                fetchPollComment.execute();
            }
        });
        segmentedPoll.setTintColor(ContextCompat.getColor(activity,R.color.colorPrimary));

        pollOptionOneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pollVote vote=new pollVote(pollId,"1");
                segmentedPoll.setEnabled(false);
                pollOptionOneText.setEnabled(false);
                pollOptionTwoText.setEnabled(false);
                pollOptionOneVote.setText(String.valueOf(pollObj.getPollOptionOneVote()+1));
                vote.execute();
            }
        });
        pollOptionTwoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pollVote vote=new pollVote(pollId,"2");
                segmentedPoll.setEnabled(false);
                pollOptionOneText.setEnabled(false);
                pollOptionTwoText.setEnabled(false);
                pollOptionTwoVote.setText(String.valueOf(pollObj.getPollOptionTwoVote()+1));
                vote.execute();
            }
        });
        Type type = new TypeToken<ArrayList<PollVoteModel>>() {}.getType();

        ArrayList<PollVoteModel> pollVoteModelArrayList=new Gson().fromJson(sharedpreferences.getString(Constants.POLL_VOTE, ""), type);
        //Log.e(LOG_TAG,"Number of Poll Vote : " +pollVoteModelArrayList.size());
        for(int i=0;i<pollVoteModelArrayList.size();i++){
            if(pollObj.getPollId().equals(pollVoteModelArrayList.get(i).getPollId())){
                if(pollVoteModelArrayList.get(i).getPollVoteOption().equals("1")){
                    pollOptionOneText.setChecked(true);
                }
                else if(pollVoteModelArrayList.get(i).getPollVoteOption().equals("2")){
                    pollOptionTwoText.setChecked(true);
                }
                segmentedPoll.setEnabled(false);
                pollOptionOneText.setEnabled(false);
                pollOptionTwoText.setEnabled(false);
            }
        }

       final String startDate=pollObj.getPollStartDate();
        Date currentDate = new Date();
       final String dateToday = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
        int dateDay=Integer.parseInt(dateToday.substring(8));
        int dateMonth=Integer.parseInt(dateToday.substring(5,7));
        int dateYear=Integer.parseInt(dateToday.substring(0,4));
        int startDateDay=Integer.parseInt(startDate.substring(8));
        int startDateMonth=Integer.parseInt(startDate.substring(5,7));
        int startDateYear=Integer.parseInt(startDate.substring(0,4));
        if(dateMonth>startDateMonth){
            if((dateMonth-startDateMonth)==1) {
                pollTimeline.setText(dateMonth - startDateMonth + " month ago");
            }
            else
            {
                pollTimeline.setText(dateMonth - startDateMonth + " months ago");
            }

        }
        else if(startDateYear<dateYear){
            pollTimeline.setText(dateMonth+(12-startDateMonth) + " months ago");
        }
        else if(startDateYear==dateYear&&startDateMonth==dateMonth&&startDateDay<dateDay){
            pollTimeline.setText((dateDay-startDateDay) + " days ago");
        }
        else if(startDateYear==dateYear&&startDateMonth==dateMonth&&startDateDay==dateDay){
            pollTimeline.setText("Today");
        }

        //Log.e(LOG_TAG,"dayDate "+dateDay+" "+dateMonth);
        return convertView;
    }

    void openPollCommentDialog(final String pollId) {
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

                        FetchPollComment fetchPollComment=new FetchPollComment(pollId);
                        fetchPollComment.execute();
                    }
                }
        );
        pollCommentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(LOG_TAG,"Send Button Clicked");
                InsertPollComment insertPollComment=new InsertPollComment(pollCommentEdit.getText().toString(),pollId);
                insertPollComment.execute();

            }
        });

    }

    class FetchPollComment extends AsyncTask {

        String url_poll_comment = "http://echoindia.co.in/php/getComment.php";
        String pollId="";

        public FetchPollComment(String pollId){
            this.pollId=pollId;
        }

        @Override
        protected Object doInBackground(Object[] params) {

            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(url_poll_comment);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("pollId",pollId);
                Log.e(LOG_TAG,"URL"+url_poll_comment);
                Log.e(LOG_TAG,"PollId "+pollId);
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
                setPollCommentData(o, pollId);
            }
            else{
                Toast.makeText(activity, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
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
                    mPollCommentModel.setPollId(pollCommentObject.getInt("PollId"));
                    mPollCommentModel.setPollCommentId(pollCommentObject.getInt("PollCommentId"));
                    mPollCommentModel.setPollCommentText(pollCommentObject.getString("PollCommentText"));
                    mPollCommentModel.setPollCommentUserPhoto(pollCommentObject.getString("UserPhoto"));
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
        String pollId="";

        public InsertPollComment(String pollCommentEditText,String pollId){
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
                Log.e(LOG_TAG,"POLLID"+pollId);
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
            if (o != null) {
                try {
                    JSONObject jObject = new JSONObject(o.toString());
                    String checkStatus = jObject.getString("status");
                    if (checkStatus.equals("1") && o != null) {
                        pollCommentEdit.setText("");
                        FetchPollComment fetchPollComment = new FetchPollComment(pollId);
                        fetchPollComment.execute();

                    } else if (checkStatus.equals("0")) {
                        Toast.makeText(activity, "No Comment Found", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "Server Connection Error", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, e.toString());
                }
            }
        else{
                Toast.makeText(activity, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }

        }


    class pollVote extends AsyncTask {

        String url_poll_comment = "http://echoindia.co.in/php/pollVote.php";
        String pollOption="";
        String pollId="";

        public pollVote(String pollId,String pollOption){
            this.pollOption=pollOption;
            this.pollId=pollId;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(url_poll_comment);
                JSONObject postDataParams = new JSONObject();
                Log.e(LOG_TAG,"POLLID"+pollId);
                postDataParams.put("pollId",pollId);
                postDataParams.put("username",sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_USER_CODE,""));
                postDataParams.put("pollOption",pollOption);
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
            if (o != null) {
                try {
                    JSONObject jObject = new JSONObject(o.toString());
                    String checkStatus = jObject.getString("status");
                    if (checkStatus.equals("1") && o != null) {
                  /* pollButton.setVisibility(View.GONE);
                    if(pollOption.equals("1")){
                        pollQuestion.setText("Your vote has been submitted for "+pollOptionOneText.getText());
                    }
                    else if(pollOption.equals("2")){
                        pollQuestion.setText("Your vote has been submitted for "+pollOptionTwoText.getText());
                    }*/
                        PollVoteModel pollVoteModel = new PollVoteModel();
                        pollVoteModel.setPollId(pollId);
                        pollVoteModel.setPollVoteOption(pollOption);
                        Type type = new TypeToken<ArrayList<PollVoteModel>>() {
                        }.getType();

                        ArrayList<PollVoteModel> pollVoteModelArrayListUpdate = new Gson().fromJson(sharedpreferences.getString(Constants.POLL_VOTE, ""), type);
                        pollVoteModelArrayListUpdate.add(pollVoteModel);
                        editor.putString(Constants.POLL_VOTE, new Gson().toJson(pollVoteModelArrayListUpdate));
                        editor.commit();
                        Toast.makeText(activity, "Thanks for your feedback", Toast.LENGTH_SHORT).show();
                    } else if (checkStatus.equals("0")) {
                        Toast.makeText(activity, "Your Vote wasn't submitted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "Server Connection Error", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, e.toString());
                }
            }
            else{
                Toast.makeText(activity, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    }