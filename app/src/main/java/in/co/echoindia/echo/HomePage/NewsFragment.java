package in.co.echoindia.echo.HomePage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import in.co.echoindia.echo.Model.NewsDetailsModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

public class NewsFragment extends Fragment {

    NewsAdapter mNewsAdapter;
    ArrayList<NewsDetailsModel> newsListArray = new ArrayList<>();
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    ListView newsList;
    NewsDetailsModel newsElement;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        newsList=(ListView)v.findViewById(R.id.news_list);
        sharedpreferences = AppUtil.getAppPreferences(getActivity());
        editor = sharedpreferences.edit();
        Type type = new TypeToken<ArrayList<NewsDetailsModel>>() {}.getType();
        newsListArray = new Gson().fromJson(sharedpreferences.getString(Constants.NEWS_LIST, ""), type);
        mNewsAdapter = new NewsAdapter(getActivity(), newsListArray);
        newsList.setAdapter(mNewsAdapter);
        mNewsAdapter.notifyDataSetChanged();
        return v;
    }

}
