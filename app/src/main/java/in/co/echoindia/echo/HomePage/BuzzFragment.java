package in.co.echoindia.echo.HomePage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import in.co.echoindia.echo.Model.PostDetailModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;


public class BuzzFragment extends Fragment {
    BuzzAdapter mBuzzAdapter;
    ArrayList<PostDetailModel> buzzListArray = new ArrayList<>();
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    ListView buzzListView;
    ArrayList<PostDetailModel> buzzList=new ArrayList<PostDetailModel>();
    PostDetailModel mPostDetailModel;
    SwipeRefreshLayout buzzSwipeRefresh;
    private static final String LOG_TAG = "BuzzFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buzz, container, false);
        buzzListView=(ListView)v.findViewById(R.id.buzz_list);
        buzzSwipeRefresh=(SwipeRefreshLayout)v.findViewById(R.id.buzz_swipe_refresh);
        sharedpreferences = AppUtil.getAppPreferences(getActivity());
        editor = sharedpreferences.edit();
        Type type = new TypeToken<ArrayList<PostDetailModel>>() {}.getType();
        buzzListArray = new Gson().fromJson(sharedpreferences.getString(Constants.BUZZ_LIST, ""), type);
        Log.e(LOG_TAG,"News Element Count "+buzzListArray.size());
        mBuzzAdapter = new BuzzAdapter(getActivity(), buzzListArray);
        buzzListView.setAdapter(mBuzzAdapter);
        mBuzzAdapter.notifyDataSetChanged();
        buzzSwipeRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.e(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
                    }
                }
        );
        return v;
    }
}
