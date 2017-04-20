package in.co.echoindia.echo.User;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class ElectedRepresentativeDetailActivity extends AppCompatActivity {

    TextView fullName,repDetail,party,designation,qualification,assumedOffice,echoId,website,twitter,state,city,career,controversy;
    ImageView profileImage;
    CircleImageView partyLogo;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    CardView introduction,cardControversy,cardCareer,cardInfo,cardAddress;
    LinearLayout llState, llCity,llParty,llDesignation,llAssumedOffice,llQualification,llEcho,llWebsite,llTwitter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elected_representative_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedpreferences = AppUtil.getAppPreferences(this);
        editor = sharedpreferences.edit();
        profileImage=(ImageView)findViewById(R.id.rep_detail_profile_image);
        fullName=(TextView)findViewById(R.id.rep_detail_full_name);
        repDetail=(TextView)findViewById(R.id.rep_detail);
        party=(TextView)findViewById(R.id.rep_detail_party);
        designation=(TextView)findViewById(R.id.rep_detail_designation);
        qualification=(TextView)findViewById(R.id.rep_detail_qualification);
        assumedOffice=(TextView)findViewById(R.id.rep_detail_assumed_office);
        echoId=(TextView)findViewById(R.id.rep_detail_echo_id);
        website=(TextView)findViewById(R.id.rep_detail_website);
        twitter=(TextView)findViewById(R.id.rep_detail_twitter);
        state=(TextView)findViewById(R.id.rep_detail_state);
        city=(TextView)findViewById(R.id.rep_detail_city);
        career=(TextView)findViewById(R.id.rep_detail_career);
        controversy=(TextView)findViewById(R.id.rep_detail_controversy);
        partyLogo=(CircleImageView)findViewById(R.id.rep_detail_party_logo);
        introduction=(CardView)findViewById(R.id.rep_detail_card_introduction);
        cardControversy=(CardView)findViewById(R.id.rep_detail_card_controversy);
        cardCareer=(CardView)findViewById(R.id.rep_detail_card_career);
        llState=(LinearLayout)findViewById(R.id.rep_detail_ll_state);
        llCity=(LinearLayout)findViewById(R.id.rep_detail_ll_city);
        llParty=(LinearLayout)findViewById(R.id.rep_detail_ll_party);
        llDesignation=(LinearLayout)findViewById(R.id.rep_detail_ll_designation);
        llAssumedOffice=(LinearLayout)findViewById(R.id.rep_detail_ll_assumed_office);
        llQualification=(LinearLayout)findViewById(R.id.rep_detail_ll_qualification);
        llEcho=(LinearLayout)findViewById(R.id.rep_detail_ll_echo);
        llWebsite=(LinearLayout)findViewById(R.id.rep_detail_ll_website);
        llTwitter=(LinearLayout)findViewById(R.id.rep_detail_ll_twitter);
        cardInfo=(CardView)findViewById(R.id.rep_detail_card_info);
        cardAddress=(CardView)findViewById(R.id.rep_detail_card_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle mBundle=getIntent().getExtras();
        Bundle repBundle=mBundle.getBundle("RepObj");
        RepInfoModel repInfoModel=(RepInfoModel)repBundle.getSerializable("RepObj");
        fullName.setText(repInfoModel.getFullName());
        Glide.with(this).load(repInfoModel.getUserPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).into(profileImage);
        repDetail.setText(repInfoModel.getRepDetail());
        party.setText(repInfoModel.getRepParty());
        if(repInfoModel.getIsPM().equals("1")){
            designation.setText("Prime Minister , India");
        }
        else if(repInfoModel.getIsCM().equals("1")){
            designation.setText("Chief Minister , "+repInfoModel.getRepState());
        }
        else if(repInfoModel.getIsMayor().equals("1")){
            designation.setText("Mayor , "+repInfoModel.getRepCity());
        }
        else {
            if(repInfoModel.getRepDesignation().equals("MP")) {
                designation.setText(repInfoModel.getRepDesignation() + " , " + repInfoModel.getRepLokSabha());
            }
            else if(repInfoModel.getRepDesignation().equals("MLA")) {
                designation.setText(repInfoModel.getRepDesignation() + " , " + repInfoModel.getRepVidhanSabha());
            }
            else if(repInfoModel.getRepDesignation().equals("Councillor")) {
                designation.setText(repInfoModel.getRepDesignation() + " , " + repInfoModel.getRepWard());
            }
        }

        qualification.setText(repInfoModel.getRepQualification());
        echoId.setText(repInfoModel.getRepCode());
        website.setText(repInfoModel.getRepHomePage());
        twitter.setText(repInfoModel.getRepTwitterId());
        state.setText(repInfoModel.getRepState());
        city.setText(repInfoModel.getRepCity());
        career.setText(repInfoModel.getRepCareer());
        controversy.setText(repInfoModel.getRepControversy());
        assumedOffice.setText(repInfoModel.getRepAssumedOffice());

        Type type = new TypeToken<ArrayList<PoliticalPartyModel>>() {}.getType();
        ArrayList<PoliticalPartyModel> politicalList= new Gson().fromJson(sharedpreferences.getString(Constants.POLITICAL_PARTY_LIST, ""), type);
        for(int i=0;i<politicalList.size();i++){
            if(repInfoModel.getRepParty().equals(politicalList.get(i).getPartyNameShort())){
                Glide.with(this).load(politicalList.get(i).getPartyLogo()).diskCacheStrategy(DiskCacheStrategy.ALL).into(partyLogo);
            }
        }

        if(repInfoModel.getRepDetail().equals("")){
            introduction.setVisibility(View.GONE);
        }

        if(repInfoModel.getRepControversy().equals("")){
            cardControversy.setVisibility(View.GONE);
        }

        if(repInfoModel.getRepCareer().equals("")){
            cardCareer.setVisibility(View.GONE);
        }

        if(repInfoModel.getRepState().equals("")){
            llState.setVisibility(View.GONE);
        }
        if(repInfoModel.getRepCity().equals("")){
            llCity.setVisibility(View.GONE);
        }

        if(repInfoModel.getRepParty().equals("")){
            llParty.setVisibility(View.GONE);
        }

        if(repInfoModel.getRepDesignation().equals("")){
            llDesignation.setVisibility(View.GONE);
        }
        if(repInfoModel.getRepAssumedOffice().equals("")){
            llAssumedOffice.setVisibility(View.GONE);
        }

        if(repInfoModel.getRepQualification().equals("")){
            llQualification.setVisibility(View.GONE);
        }

        if(repInfoModel.getRepCode().equals("")){
            llEcho.setVisibility(View.GONE);
        }
        if(repInfoModel.getRepHomePage().equals("")){
            llWebsite.setVisibility(View.GONE);
        }

        if(repInfoModel.getRepTwitterId().equals("")){
            llTwitter.setVisibility(View.GONE);
        }

        if(repInfoModel.getRepTwitterId().equals("")&&repInfoModel.getRepHomePage().equals("")&&
                repInfoModel.getRepCode().equals("")&& repInfoModel.getRepQualification().equals("")
                &&repInfoModel.getRepAssumedOffice().equals("")&&repInfoModel.getRepDesignation().equals("")
                &&repInfoModel.getRepParty().equals("")){
            cardInfo.setVisibility(View.GONE);

        }

        if(repInfoModel.getRepCity().equals("") && repInfoModel.getRepState().equals("")){
            cardAddress.setVisibility(View.GONE);
        }



    }


}
