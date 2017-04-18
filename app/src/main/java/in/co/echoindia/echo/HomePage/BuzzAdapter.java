package in.co.echoindia.echo.HomePage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import in.co.echoindia.echo.Model.PostDetailModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;

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

        return convertView;
    }
}
