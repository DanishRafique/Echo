package in.co.echoindia.echo.HomePage;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.DecimalFormat;
import java.util.ArrayList;

import in.co.echoindia.echo.Model.PollDetailsModel;
import in.co.echoindia.echo.R;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by Danish Rafique on 04-04-2017.
 */

public class PollAdapter extends BaseAdapter {

    ArrayList<PollDetailsModel> pollDetailModel = new ArrayList<>();
    private Context activity;
    private LayoutInflater inflater;

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

    int totalVote;
    float optionOnePercent, optionTwoPercent;
    int pollOptionOneColor,pollOptionTwoColor;

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
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_poll_child, null);
        }
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

        pollTitle.setText(pollObj.getPollTitle());
        pollDescription.setText(pollObj.getPollDescription());
        pollVendor.setText(pollObj.getPollVendor());
        Glide.with(activity).load(pollObj.getPollImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(pollImage);
        Glide.with(activity).load(pollObj.getPollVendorLogo()).diskCacheStrategy(DiskCacheStrategy.ALL).into(pollVendorLogo);
        pollOptionOneVote.setText(String.valueOf(pollObj.getPollOptionOneVote()));
        pollOptionTwoVote.setText(String.valueOf(pollObj.getPollOptionTwoVote()));
        pollOptionOneText.setText(pollObj.getPollOptionOneText());
        pollOptionTwoText.setText(pollObj.getPollOptionTwoText());

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

        segmentedPoll.setTintColor(ContextCompat.getColor(activity,R.color.colorPrimaryDark));

        return convertView;
    }
}