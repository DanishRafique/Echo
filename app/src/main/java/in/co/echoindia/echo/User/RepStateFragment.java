package in.co.echoindia.echo.User;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import in.co.echoindia.echo.Model.RepInfoModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

public class RepStateFragment extends Fragment {
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    ElectedRepresentativeAdapter electedRepresentativeAdapter;
    ListView repCentralList;
    ArrayList<RepInfoModel> repArrayList=new ArrayList<>();
    private static final String LOG_TAG = "RepCentralFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_representative, container, false);
        sharedpreferences = AppUtil.getAppPreferences(getActivity());
        editor = sharedpreferences.edit();

        repCentralList=(ListView)v.findViewById(R.id.rep_list_view);
        Type type = new TypeToken<ArrayList<RepInfoModel>>() {}.getType();
        repArrayList = new Gson().fromJson(sharedpreferences.getString(Constants.STATE_ELECTED_MEMBERS, ""), type);
        Log.e(LOG_TAG,"State List Count "+repArrayList.size());
        electedRepresentativeAdapter = new ElectedRepresentativeAdapter(getActivity(), repArrayList);
        repCentralList.setAdapter(electedRepresentativeAdapter);
        electedRepresentativeAdapter.notifyDataSetChanged();
        return v;
    }
}
