package in.co.echoindia.echo.HomePage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import in.co.echoindia.echo.Model.NewsDetailsModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

/**
 * Created by Danish Rafique on 12-03-2017.
 */

public class NewsAdapter extends BaseAdapter {

    ArrayList<NewsDetailsModel> newsDetailsModels = new ArrayList<>();
    Activity activity;
    TextView newsTitle;
    ImageView newsImage;
    TextView newsDescription;
    TextView newsVendor;
    ImageView newsVendorLogo;
    TextView newsTimeline;
    TextView newsUpvoteValue;
    TextView newsDownvoteValue;
    LinearLayout newsFullStory;
    ArrayList<NewsDetailsModel> newUpdatedNewsList=new ArrayList<>();
   // ToggleButton newsUpvote,newsDownvote;
    LinearLayout newsShareButton;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    ToggleButton tempBtn;


    public NewsAdapter(Activity activity, ArrayList<NewsDetailsModel> newsDetailsModels) {
        this.activity = activity;
        this.newsDetailsModels = newsDetailsModels;
    }
    @Override
    public int getCount() {
        if(newsDetailsModels != null)
            return newsDetailsModels.size();
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
    public View getView(final int position, View convertView, ViewGroup parent){

     // if (convertView == null) {
            LayoutInflater inflater=(LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_news_child, null);
      //  }
        final NewsDetailsModel newsObj = newsDetailsModels.get(position);
        newsTitle=(TextView) convertView.findViewById(R.id.news_title);
        newsImage=(ImageView) convertView.findViewById(R.id.news_image);
        newsDescription=(TextView) convertView.findViewById(R.id.news_description);
        newsVendor=(TextView) convertView.findViewById(R.id.news_vendor_name);
        newsVendorLogo=(ImageView) convertView.findViewById(R.id.news_vendor_logo);
        newsTimeline=(TextView) convertView.findViewById(R.id.news_timeline);
        newsUpvoteValue=(TextView)convertView.findViewById(R.id.news_upvote_value);
        newsDownvoteValue=(TextView)convertView.findViewById(R.id.news_downvote_value);
        newsFullStory=(LinearLayout)convertView.findViewById(R.id.news_full_story_link);
        newsShareButton =(LinearLayout)convertView.findViewById(R.id.news_share_button);

       final ToggleButton newsUpvote=(ToggleButton)convertView.findViewById(R.id.news_upvote);
        newsUpvote.setTag(String.valueOf(newsObj.getNewsID()));

        final ToggleButton newsDownvote=(ToggleButton)convertView.findViewById(R.id.news_downvote);
        newsDownvote.setTag(String.valueOf(newsObj.getNewsID()));
        sharedpreferences = AppUtil.getAppPreferences(activity);
        editor = sharedpreferences.edit();

        //Log.e("NEWS ELEMENT",String.valueOf(newsObj.getNewsUpVote()));

        newsTitle.setText(newsObj.getNewsTitle());
        newsDescription.setText(newsObj.getNewsDescription());
        newsVendor.setText(newsObj.getNewsVendor());
        newsTimeline.setText(newsObj.getNewsTimeline());
        Glide.with(activity).load(newsObj.getNewsImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(newsImage);
        Glide.with(activity).load(newsObj.getNewsVendorLogo()).diskCacheStrategy(DiskCacheStrategy.ALL).into(newsVendorLogo);
        newsUpvoteValue.setText(String.valueOf(newsObj.getNewsUpVote()));

        newsDownvoteValue.setText(String.valueOf(newsObj.getNewsDownVote()));

        if(newsObj.isNewsUpVoteValue()){
            newsUpvote.setChecked(true);
        }
        else if(newsObj.isNewsDownVoteValue()){
            newsDownvote.setChecked(true);
        }



        newsFullStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newsIntent=new Intent(activity,NewsActivity.class);
                newsIntent.putExtra("Title",newsObj.getNewsTitle());
                newsIntent.putExtra("Link",newsObj.getNewsVendorLink());
                activity.startActivity(newsIntent);
            }
        });

        newsShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, newsObj.getNewsDescription());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, newsObj.getNewsTitle());
                activity.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        newsUpvote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                newsVote newsVote = new newsVote();
                TextView textView = null;
                tempBtn = (ToggleButton) buttonView;
                ViewGroup view = (ViewGroup) tempBtn.getParent();
                textView = (TextView) view.findViewById(R.id.news_upvote_value);
                Log.d("Voting...", textView.getText().toString());
                int upvote = Integer.parseInt(textView.getText().toString());

                ViewGroup rootParent = (ViewGroup) view.getParent();
                ToggleButton t = (ToggleButton) rootParent.findViewById(R.id.news_downvote);

                if(!isChecked){
                    t.setEnabled(true);
                    upvote --;
                    //@TODO Here the up vote is decreasing. Simply save the above upvote variable value in GSON.
                    newsObj.setNewsUpVoteValue(false);
                    newsObj.setNewsUpVote(upvote);
                    newsVote.execute("up", "none");
                    textView.setText(upvote + "");
                }
                else {
                    t.setEnabled(false);
                    upvote ++;
                    //@TODO Here the up vote is increasing. Simply save the above upvote variable value in GSON.
                    newsObj.setNewsUpVoteValue(true);
                    newsObj.setNewsUpVote(upvote);
                    newsVote.execute("up", "up");
                    textView.setText(upvote + "");
                }
            }
        });




        newsDownvote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                newsVote newsVote = new newsVote();
                TextView textView = null;
                tempBtn = (ToggleButton) buttonView;
                ViewGroup view = (ViewGroup) tempBtn.getParent();
                textView = (TextView) view.findViewById(R.id.news_downvote_value);
                Log.d("Voting...", textView.getText().toString());
                int downvote = Integer.parseInt(textView.getText().toString());

                ViewGroup rootParent = (ViewGroup) view.getParent();
                ToggleButton t = (ToggleButton) rootParent.findViewById(R.id.news_upvote);

                if(!isChecked){
                    t.setEnabled(true);
                    downvote --;
                    //@TODO Here the down vote is decreasing. Simply save the above downvote variable value in GSON.
                    newsObj.setNewsDownVoteValue(false);
                    newsObj.setNewsDownVote(downvote);
                    newsVote.execute("down", "none");
                    textView.setText(downvote + "");
                }
                else {
                    t.setEnabled(false);
                    downvote ++;
                    //@TODO Here the up vote is increasing. Simply save the above downvote variable value in GSON.
                    newsObj.setNewsDownVoteValue(true);
                    newsObj.setNewsDownVote(downvote);
                    newsVote.execute("down", "down");
                    textView.setText(downvote + "");
                }
            }
        });
        newUpdatedNewsList.clear();

        for(int i=0;i<newsDetailsModels.size();i++){
            if(i!=position) {
                newUpdatedNewsList.add(newsDetailsModels.get(i));
            }
            else if(i==position){
                newUpdatedNewsList.add(newsObj);
            }
        }
        editor.putString(Constants.NEWS_LIST, new Gson().toJson(newUpdatedNewsList));
        editor.commit();

        return convertView;
    }



    private class newsVote extends AsyncTask<String, Void, String> {
        String newsId;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("Voting...", "News Id: " + tempBtn.getTag().toString());
            newsId = tempBtn.getTag().toString();
        }

        @Override
        protected String doInBackground(String... params) {
            String btn = params[0].toString();
            String voteType = params[1].toString();

            String voteUrl = "http://echoindia.co.in/php/newsVote.php";

            try {
                URL url = new URL(voteUrl);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("btn",btn);
                postDataParams.put("voteType",voteType);
                postDataParams.put("newsId",newsId);

                Log.d("Voting", "Params: " + postDataParams.toString());

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
            Log.d("Voting", "Response : " + aVoid);
        }
    }

}
