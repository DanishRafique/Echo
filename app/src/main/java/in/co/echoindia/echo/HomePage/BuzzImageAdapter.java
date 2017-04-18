package in.co.echoindia.echo.HomePage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import in.co.echoindia.echo.R;

/**
 * Created by Danish Rafique on 18-04-2017.
 */

public class BuzzImageAdapter extends BaseAdapter{


    ArrayList<String> buzzImageList = new ArrayList<>();
    Activity activity;

    ImageView buzzImage;
    private static final String LOG_TAG = "BuzzImageAdapter";

    public BuzzImageAdapter(Activity activity, ArrayList<String> buzzImageList) {
        this.activity = activity;
        this.buzzImageList = buzzImageList;
    }

    @Override
    public int getCount() {
        if(buzzImageList != null)
            return buzzImageList.size();
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_buzz_image_child, null);
        String buzzImageUrl = buzzImageList.get(position);
        //Log.e(LOG_TAG,"Called "+position+" "+buzzImageUrl);
        buzzImage=(ImageView)convertView.findViewById(R.id.buzz_image);
        Glide.with(activity).load("http://images.indianexpress.com/2016/04/hrithikroshan7592.jpg").diskCacheStrategy(DiskCacheStrategy.ALL).into(buzzImage);
        return convertView;
    }
}
