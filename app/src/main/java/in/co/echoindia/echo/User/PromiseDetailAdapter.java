package in.co.echoindia.echo.User;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.co.echoindia.echo.Model.PromiseDetailModel;
import in.co.echoindia.echo.R;

/**
 * Created by Danish Rafique on 29-04-2017.
 */

public class PromiseDetailAdapter extends BaseAdapter {

    ArrayList<PromiseDetailModel> promiseDetailModels = new ArrayList<>();
    Activity activity;
    TextView promiseNumber,promiseDetailName,promiseDetailSynopsis;

    public PromiseDetailAdapter(Activity activity, ArrayList<PromiseDetailModel> promiseDetailModels) {
        this.activity = activity;
        this.promiseDetailModels = promiseDetailModels;
    }

    @Override
    public int getCount() {
        if(promiseDetailModels != null)
            return promiseDetailModels.size();
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
        convertView = inflater.inflate(R.layout.list_promise_detail, null);
        final PromiseDetailModel promiseDetailObj = promiseDetailModels.get(position);
        promiseNumber=(TextView)convertView.findViewById(R.id.promise_number);
        promiseDetailName=(TextView)convertView.findViewById(R.id.promise_detail_name);
        promiseDetailSynopsis=(TextView)convertView.findViewById(R.id.promise_detail_synopsis);
        promiseNumber.setText(String.valueOf(position+1));
        promiseDetailName.setText(promiseDetailObj.getPromiseDetailName());
        promiseDetailSynopsis.setText(promiseDetailObj.getPromiseDetailSynopsis());
        return convertView;
    }
}
