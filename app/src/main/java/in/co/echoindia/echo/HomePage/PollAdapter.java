package in.co.echoindia.echo.HomePage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.DecimalFormat;
import java.util.ArrayList;

import in.co.echoindia.echo.Model.PollDetailsModel;
import in.co.echoindia.echo.R;

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
    Button pollOptionOneText, pollOptionTwoText;
    TextRoundCornerProgressBar pollBarOne , pollBarTwo;

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
        pollOptionOneText = (Button) convertView.findViewById(R.id.poll_option_one_text);
        pollOptionTwoText = (Button) convertView.findViewById(R.id.poll_option_two_text);

        pollTitle.setText(pollObj.getPollTitle());
        pollDescription.setText(pollObj.getPollDescription());
        pollVendor.setText(pollObj.getPollVendor());
        Glide.with(activity).load(pollObj.getPollImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(pollImage);
        //Glide.with(activity).load(pollObj.getPollVendorLogo()).diskCacheStrategy(DiskCacheStrategy.ALL).into(pollVendorLogo);
        pollOptionOneVote.setText(String.valueOf(pollObj.getPollOptionOneVote()));
        pollOptionTwoVote.setText(String.valueOf(pollObj.getPollOptionTwoVote()));
        pollOptionOneText.setText(pollObj.getPollOptionOneText());
        pollOptionTwoText.setText(pollObj.getPollOptionTwoText());

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

        pollOptionOneColor=pollObj.getPollOptionOneColor();
        pollOptionTwoColor=pollObj.getPollOptionTwoColor();

        setupProgressColor(pollOptionOneColor,pollBarOne);
        setupProgressColor(pollOptionOneColor,pollBarTwo);

        return convertView;
    }

    void setupProgressColor(int pollOptionColor,TextRoundCornerProgressBar progressBar){
        if (pollOptionColor==1){
            progressBar.setProgressColor(R.color.custom_progress_green_header);
            progressBar.setSecondaryProgressColor(R.color.custom_progress_green_progress);
        }
        else if(pollOptionColor==2){
            progressBar.setProgressColor(R.color.custom_progress_orange_header);
            progressBar.setSecondaryProgressColor(R.color.custom_progress_orange_progress);
        }
        else if(pollOptionColor==3){
            progressBar.setProgressColor(R.color.custom_progress_blue_header);
            progressBar.setSecondaryProgressColor(R.color.custom_progress_blue_progress);
        }
        else if(pollOptionColor==4){
            progressBar.setProgressColor(R.color.custom_progress_purple_header);
            progressBar.setSecondaryProgressColor(R.color.custom_progress_purple_progress);
        }
        else if(pollOptionColor==4){
            progressBar.setProgressColor(R.color.custom_progress_red_header);
            progressBar.setSecondaryProgressColor(R.color.custom_progress_red_progress);
        }
    }
}