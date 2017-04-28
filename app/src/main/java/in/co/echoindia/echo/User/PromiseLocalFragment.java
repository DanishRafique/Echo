package in.co.echoindia.echo.User;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import in.co.echoindia.echo.Model.PromiseModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

/**
 * Created by Danish Rafique on 29-04-2017.
 */

public class PromiseLocalFragment extends Fragment {

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    PromiseAdapter promiseAdapter;
    ListView promiseCentralList;
    ArrayList<PromiseModel> promiseArrayList = new ArrayList<>();
    private static final String LOG_TAG = "RepCentralFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_promises, container, false);
        sharedpreferences = AppUtil.getAppPreferences(getActivity());
        editor = sharedpreferences.edit();

        promiseCentralList = (ListView) v.findViewById(R.id.promise_list_view);
        Type type = new TypeToken<ArrayList<PromiseModel>>() {
        }.getType();
        promiseArrayList = new Gson().fromJson(sharedpreferences.getString(Constants.PROMISE_CITY, ""), type);
        Log.e(LOG_TAG, "Promise Central List Count " + promiseArrayList.size());
        promiseAdapter = new PromiseAdapter(getActivity(), promiseArrayList);
        promiseCentralList.setAdapter(promiseAdapter);
        promiseAdapter.notifyDataSetChanged();
        return v;
    }
}