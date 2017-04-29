package in.co.echoindia.echo.User;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import in.co.echoindia.echo.Model.PromiseModel;
import in.co.echoindia.echo.R;

/**
 * Created by Danish Rafique on 29-04-2017.
 */

public class PromiseAdapter extends BaseAdapter {

    ArrayList<PromiseModel> promiseModels = new ArrayList<>();
    Activity activity;
    TextView promiseType,promiseCount;
    ImageView promiseImage;
    Button promiseViewButton;
    private static final String LOG_TAG = "PromiseAdapter";

    public PromiseAdapter(Activity activity, ArrayList<PromiseModel> promiseModels) {
        this.activity = activity;
        this.promiseModels = promiseModels;
    }

    @Override
    public int getCount() {
        if(promiseModels != null)
            return promiseModels.size();
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
        convertView = inflater.inflate(R.layout.list_promises, null);
        final PromiseModel promiseObj = promiseModels.get(position);
        promiseType=(TextView)convertView.findViewById(R.id.promise_type);
        promiseCount=(TextView)convertView.findViewById(R.id.promise_count);
        promiseImage=(ImageView)convertView.findViewById(R.id.promise_image);
        promiseViewButton=(Button) convertView.findViewById(R.id.promise_view_button);
        promiseType.setText(promiseObj.getPromiseType());
        promiseCount.setText(promiseObj.getPromiseCount());
        //Log.e(LOG_TAG,"Size inside Adapter "+promiseObj.getPromiseDetail().size());
        Glide.with(activity).load(promiseObj.getPromiseImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(promiseImage);
        promiseViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent promiseIntent=new Intent(activity,PromiseDetailActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("promiseObj", promiseObj);
                promiseIntent.putExtra("promiseObj",mBundle);
                activity.startActivity(promiseIntent);
            }
        });
        return convertView;
    }
}
