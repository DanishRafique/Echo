package in.co.echoindia.echo.User;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import in.co.echoindia.echo.Model.PoliticalPartyModel;
import in.co.echoindia.echo.Model.RepInfoModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

/**
 * Created by Danish Rafique on 20-04-2017.
 */

public class ElectedRepresentativeAdapter extends BaseAdapter {

    ArrayList<RepInfoModel> repDetailsModels = new ArrayList<>();
    Activity activity;

    TextView repName,repDesignation,repParty;
    ImageView repProfileImage;
    CircleImageView repPartyLogo;
    Button repInfoButton;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;




    public ElectedRepresentativeAdapter(Activity activity, ArrayList<RepInfoModel> repDetailsModels) {
        this.activity = activity;
        this.repDetailsModels = repDetailsModels;
    }
    @Override
    public int getCount() {
        if(repDetailsModels != null)
            return repDetailsModels.size();
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
        convertView = inflater.inflate(R.layout.list_elected_representative_item, null);
        final RepInfoModel repObj = repDetailsModels.get(position);
        sharedpreferences = AppUtil.getAppPreferences(activity);
        editor = sharedpreferences.edit();


        repName=(TextView)convertView.findViewById(R.id.rep_name);
        repParty=(TextView)convertView.findViewById(R.id.rep_party);
        repDesignation=(TextView)convertView.findViewById(R.id.rep_designation);
        repPartyLogo=(CircleImageView)convertView.findViewById(R.id.rep_party_logo);
        repProfileImage=(ImageView)convertView.findViewById(R.id.rep_profile_image);
        repInfoButton=(Button)convertView.findViewById(R.id.rep_info_button);

        if(repObj.getIsPM().equals("1")){
            repDesignation.setText("Prime Minister , India");
        }
        else if(repObj.getIsCM().equals("1")){
            repDesignation.setText("Chief Minister , "+repObj.getRepState());
        }
        else if(repObj.getIsMayor().equals("1")){
            repDesignation.setText("Mayor , "+repObj.getRepCity());
        }
        else {
            if(repObj.getRepDesignation().equals("MP")) {
                repDesignation.setText(repObj.getRepDesignation() + " , " + repObj.getRepLokSabha());
            }
            else if(repObj.getRepDesignation().equals("MLA")) {
                repDesignation.setText(repObj.getRepDesignation() + " , " + repObj.getRepVidhanSabha());
            }
            else if(repObj.getRepDesignation().equals("Councillor")) {
                repDesignation.setText(repObj.getRepDesignation() + " , " + repObj.getRepWard());
            }
        }

        repName.setText(repObj.getFullName());
        repParty.setText(repObj.getRepParty());
        Glide.with(activity).load(repObj.getUserPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).into(repProfileImage);

        Type type = new TypeToken<ArrayList<PoliticalPartyModel>>() {}.getType();
        ArrayList<PoliticalPartyModel> politicalList= new Gson().fromJson(sharedpreferences.getString(Constants.POLITICAL_PARTY_LIST, ""), type);
        for(int i=0;i<politicalList.size();i++){
            if(repObj.getRepParty().equals(politicalList.get(i).getPartyNameShort())){
                Glide.with(activity).load(politicalList.get(i).getPartyLogo()).diskCacheStrategy(DiskCacheStrategy.ALL).into(repPartyLogo);
            }
        }
        return convertView;
    }
}
