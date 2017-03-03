package in.co.echoindia.echo.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Danish Rafique on 03-03-2017.
 */

public class AppUtil {

    public static SharedPreferences getAppPreferences(Context context){
        return context.getSharedPreferences(Constants.SETTINGS_APP_SETTINGS, Context.MODE_PRIVATE);
    }


    public static String convertDateFormat_MMddyyyy(String getDateStr){

        String convertedDateStr="";
        SimpleDateFormat originalFormat  =  new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat targetFormat =new SimpleDateFormat("MM/dd/yyyy");
        Date dateObj =null;
        try {
            dateObj = originalFormat.parse(getDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(dateObj != null){
            convertedDateStr = targetFormat.format(dateObj);
        }
        return convertedDateStr.toUpperCase();
    }

    public static String convertDateFormat_ddMMyyyy(String getDateStr){

        String convertedDateStr="";
        SimpleDateFormat originalFormat  =  new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat targetFormat =new SimpleDateFormat("dd-MM-yyyy");
        Date dateObj =null;
        try {
            dateObj = originalFormat.parse(getDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(dateObj != null){
            convertedDateStr = targetFormat.format(dateObj);
        }
        return convertedDateStr.toUpperCase();
    }



    public static String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    public static DisplayMetrics getDeviceMetrics(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics;
    }


}

