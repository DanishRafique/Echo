package in.co.echoindia.echo.HomePage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import in.co.echoindia.echo.Model.PollCommentModel;
import in.co.echoindia.echo.R;

/**
 * Created by Danish Rafique on 09-04-2017.
 */

public class PollCommentAdapter extends BaseAdapter {

    ArrayList<PollCommentModel> pollCommentModel = new ArrayList<>();
    private Context activity;
    private LayoutInflater inflater;

    CircleImageView commentUserImage;
    TextView commentName;
    TextView commentText;
    TextView buzzTime;

    public PollCommentAdapter(Context activity, ArrayList<PollCommentModel> pollDetailModel) {
        this.activity = activity;
        this.pollCommentModel = pollDetailModel;
    }

    @Override
    public int getCount() {
        if (pollCommentModel != null)
            return pollCommentModel.size();
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
            convertView = inflater.inflate(R.layout.list_comment_child, null);
        }
        final PollCommentModel pollCommentObj = pollCommentModel.get(position);
        commentUserImage=(CircleImageView)convertView.findViewById(R.id.comment_user_image);
        commentName=(TextView)convertView.findViewById(R.id.comment_name);
        commentText=(TextView)convertView.findViewById(R.id.comment_text);
        buzzTime=(TextView)convertView.findViewById(R.id.comment_time);

        if(commentUserImage!=null) {
            Glide.with(activity).load(pollCommentObj.getPollCommentUserPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).into(commentUserImage);
        }
        commentName.setText(pollCommentObj.getPollCommentUserName());
        commentText.setText(pollCommentObj.getPollCommentText());
        //commentTime.setText(pollCommentObj.getPollCommentTime());


        final String startDate=pollCommentObj.getPollCommentDate();
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
            final String postTime=pollCommentObj.getPollCommentTime();
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
}
