package in.co.echoindia.echo.HomePage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.co.echoindia.echo.Model.NewsDetailsModel;
import in.co.echoindia.echo.R;

/**
 * Created by Danish Rafique on 12-03-2017.
 */

public class NewsAdapter extends BaseAdapter {

    ArrayList<NewsDetailsModel> newsDetailsModels = new ArrayList<>();
    private Context activity;
    private LayoutInflater inflater;

    TextView newsTitle;
    ImageView newsImage;
    TextView newsDescription;
    TextView newsVendor;
    ImageView newsVendorLogo;
    TextView newsTimeline;

    public NewsAdapter(Context activity, ArrayList<NewsDetailsModel> newsDetailsModels) {
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
    public View getView(int position, View convertView, ViewGroup parent){

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_news_child, null);
        }
        final NewsDetailsModel newsObj = newsDetailsModels.get(position);
        newsTitle=(TextView) convertView.findViewById(R.id.news_title);
        newsImage=(ImageView) convertView.findViewById(R.id.news_image);
        newsDescription=(TextView) convertView.findViewById(R.id.news_description);
        newsVendor=(TextView) convertView.findViewById(R.id.news_vendor_name);
        newsVendorLogo=(ImageView) convertView.findViewById(R.id.news_vendor_logo);
        newsTimeline=(TextView) convertView.findViewById(R.id.news_timeline);
        return convertView;
    }
}
