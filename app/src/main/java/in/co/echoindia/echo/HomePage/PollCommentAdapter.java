package in.co.echoindia.echo.HomePage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

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
    TextView commentTime;

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
        commentTime=(TextView)convertView.findViewById(R.id.comment_time);

        if(commentUserImage!=null) {
            Glide.with(activity).load(pollCommentObj.getPollCommentUserPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).into(commentUserImage);
        }
        commentName.setText(pollCommentObj.getPollCommentUserName());
        commentText.setText(pollCommentObj.getPollCommentText());
        commentTime.setText(pollCommentObj.getPollCommentTime());

        return convertView;
    }
}
