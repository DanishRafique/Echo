package in.co.echoindia.echo.HomePage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import in.co.echoindia.echo.Model.NewsDetailsModel;
import in.co.echoindia.echo.R;

public class NewsFragment extends Fragment {

    NewsAdapter mNewsAdapter;
    ArrayList<NewsDetailsModel> newsDetailsModels = new ArrayList<>();
    ListView newsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        newsList=(ListView)v.findViewById(R.id.news_list);
        NewsDetailsModel testObj = new NewsDetailsModel();
        testObj.setNewsDescription("Hello");
        testObj.setNewsImage("Hello");
        testObj.setNewsVendor("Hello");
        testObj.setNewsTitle("Hello");
        for(int i = 0;i<5;i++) {
            newsDetailsModels.add(testObj);
        }
        mNewsAdapter = new NewsAdapter(getActivity(), newsDetailsModels);
        newsList.setAdapter(mNewsAdapter);
        mNewsAdapter.notifyDataSetChanged();
        return v;
    }

}
