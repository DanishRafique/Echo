package in.co.echoindia.echo.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import in.co.echoindia.echo.HomePage.HomePageActivity;
import in.co.echoindia.echo.Model.NewsDetailsModel;
import in.co.echoindia.echo.Model.PostDetailModel;
import in.co.echoindia.echo.Model.PromiseDetailModel;
import in.co.echoindia.echo.Model.PromiseModel;
import in.co.echoindia.echo.Model.RepDetailModel;
import in.co.echoindia.echo.Model.RepInfoModel;
import in.co.echoindia.echo.Model.UserDetailsModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

public class LoginActivity extends AppCompatActivity{

    private ProgressDialog pDialog;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    EditText userName,password;
    LinearLayout login;
    LinearLayout signUp;

    UserDetailsModel mUserDetailsModel;
    RepDetailModel repDetailModel;
    ArrayList<RepInfoModel> centralList=new ArrayList<RepInfoModel>();
    ArrayList<RepInfoModel> stateList=new ArrayList<RepInfoModel>();
    ArrayList<RepInfoModel> localList=new ArrayList<RepInfoModel>();

    ArrayList<PromiseModel> centralPromiseList=new ArrayList<PromiseModel>();
    ArrayList<PromiseModel> statePromiseList=new ArrayList<PromiseModel>();
    ArrayList<PromiseModel> localPromiseList=new ArrayList<PromiseModel>();
    ArrayList<PromiseDetailModel> promiseDetail=new ArrayList<PromiseDetailModel>();

    PromiseModel promiseModel;
    PromiseDetailModel promiseDetailModel;


    RepInfoModel mRepInfoModel;
    private static final String LOG_TAG = "LoginActivity";
    LinearLayout forgotPasswordll;

    PostDetailModel mPostDetailModel;
    ArrayList<PostDetailModel> myPostList=new ArrayList<PostDetailModel>();
    ArrayList<PostDetailModel> myPostListUpdated=new ArrayList<PostDetailModel>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.my_statusbar_color));
        }

        sharedpreferences = AppUtil.getAppPreferences(this);
        editor = sharedpreferences.edit();
        pDialog = new ProgressDialog(this);

        userName = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (LinearLayout)findViewById(R.id.login);
        signUp = (LinearLayout)findViewById(R.id.ll_sign_up);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login login = new Login();
                login.execute("");
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(signUpIntent);
                LoginActivity.this.finish();
            }
        });

        forgotPasswordll=(LinearLayout)findViewById(R.id.forgot_password_ll);

        forgotPasswordll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotIntent=new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(forgotIntent);
            }
        });
    }
    class Login extends AsyncTask {

        String url_user_login = "http://echoindia.co.in/php/UserLogin.php";
        //http://echoindia.co.in/php/UserLogin.php?username=user&password=123&deviceType=Android&deviceId=1234
        String userNameStr=userName.getText().toString().trim();
        String passwordStr=password.getText().toString().trim();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();
            pDialog.setMessage("Please Wait");
            pDialog.setCancelable(true);
        }

        @Override
        protected Object doInBackground(Object[] params) {

            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(url_user_login);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username",userNameStr);
                postDataParams.put("password",passwordStr);
                postDataParams.put("deviceType","ANDROID");
                postDataParams.put("deviceId",sharedpreferences.getString(Constants.REG_ID,""));
                Log.e(LOG_TAG,"URL"+url_user_login);
                Log.e(LOG_TAG,"PostParam"+postDataParams.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8"));
                writer.write(AppUtil.getPostDataString(postDataParams));
                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader( conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");

                    String line = "";
                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    Log.e(LOG_TAG,sb.toString());
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception ex) {
                return null;
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            pDialog.dismiss();
            setUserData(o);


        }
    }

    private void setUserData(Object o)  {
        try {
            JSONObject jObject=new JSONObject(o.toString());
            String checkStatus=jObject.getString("status");
            if(checkStatus.equals("0")&& o != null){
            JSONObject responseObject = jObject.getJSONObject("response");
            JSONArray jArray = responseObject.getJSONArray("UserDetail");
            JSONObject userObj = jArray.getJSONObject(0);

                if(jObject.getString("userType").equals("user")) {
                    mUserDetailsModel = new UserDetailsModel();
                    mUserDetailsModel.setUserName(userObj.getString("UserCode"));
                    mUserDetailsModel.setPassword(password.getText().toString().trim());
                    mUserDetailsModel.setFirstName(userObj.getString("FirstName"));
                    mUserDetailsModel.setLastName(userObj.getString("LastName"));
                    mUserDetailsModel.setEmailId(userObj.getString("EmailId"));
                    mUserDetailsModel.setPhoneNo(userObj.getString("PhoneNo"));
                    mUserDetailsModel.setAddress(userObj.getString("Address"));
                    mUserDetailsModel.setCity(userObj.getString("City"));
                    mUserDetailsModel.setWard(userObj.getString("Ward"));
                    mUserDetailsModel.setPinCode(userObj.getString("PinCode"));
                    mUserDetailsModel.setDistrict(userObj.getString("District"));
                    mUserDetailsModel.setState(userObj.getString("State"));
                    mUserDetailsModel.setUserPhoto(userObj.getString("UserPhoto"));
                    mUserDetailsModel.setVoterIdPhoto(userObj.getString("VoterId"));
                    mUserDetailsModel.setAadhaarPhoto(userObj.getString("AadharCard"));
                    mUserDetailsModel.setIssueMaker(userObj.getString("IssueMaker"));
                    mUserDetailsModel.setIsVerified(userObj.getString("isVerified"));
                    mUserDetailsModel.setLokSabha(userObj.getString("LokSabha"));
                    mUserDetailsModel.setVidhanSabha(userObj.getString("VidhanSabha"));
                    mUserDetailsModel.setUserPhoto(userObj.getString("UserPhoto"));
                    editor.putString(Constants.SETTINGS_IS_LOGGED_TYPE,"USER");
                    editor.putString(Constants.SETTINGS_OBJ_USER, new Gson().toJson(mUserDetailsModel));
                    editor.putString(Constants.SETTINGS_IS_LOGGED_USER_CODE, userObj.getString("UserCode"));
                }
                else if(jObject.getString("userType").equals("rep")) {
                    repDetailModel = new RepDetailModel();
                    repDetailModel.setRepName(userObj.getString("RepCode"));
                    repDetailModel.setPassword(password.getText().toString().trim());
                    repDetailModel.setFirstName(userObj.getString("FirstName"));
                    repDetailModel.setLastName(userObj.getString("LastName"));
                    repDetailModel.setEmailId(userObj.getString("EmailId"));
                    repDetailModel.setPhoneNo(userObj.getString("PhoneNo"));
                    repDetailModel.setCity(userObj.getString("City"));
                    repDetailModel.setWard(userObj.getString("Ward"));
                    repDetailModel.setPinCode(userObj.getString("PinCode"));
                    repDetailModel.setState(userObj.getString("State"));
                    repDetailModel.setUserPhoto(userObj.getString("UserPhoto"));
                    repDetailModel.setVoterIdPhoto(userObj.getString("VoterId"));
                    repDetailModel.setAadhaarPhoto(userObj.getString("AadharCard"));
                    repDetailModel.setIsVerified(userObj.getString("isVerified"));
                    repDetailModel.setLokSabha(userObj.getString("LokSabha"));
                    repDetailModel.setVidhanSabha(userObj.getString("VidhanSabha"));
                    repDetailModel.setUserPhoto(userObj.getString("UserPhoto"));
                    repDetailModel.setRepParty(userObj.getString("RepParty"));
                    repDetailModel.setRepDesignation(userObj.getString("RepDesignation"));
                    repDetailModel.setRepLocation(userObj.getString("RepLocation"));
                    repDetailModel.setRepQualification(userObj.getString("RepQualification"));
                    repDetailModel.setRepHomePage(userObj.getString("RepHomePage"));
                    repDetailModel.setRepTwitter(userObj.getString("RepTwitterId"));
                    editor.putString(Constants.SETTINGS_IS_LOGGED_TYPE,"REP");
                    editor.putString(Constants.SETTINGS_OBJ_USER, new Gson().toJson(repDetailModel));
                    editor.putString(Constants.SETTINGS_IS_LOGGED_USER_CODE, userObj.getString("RepCode"));
                }

                int noOfPost = responseObject.getInt("NoOfPosts");
                Log.e(LOG_TAG,"Number of Post"+noOfPost);
                if(noOfPost>0){
                    JSONArray jArrayMyPost=responseObject.getJSONArray("Posts");
                    Log.e(LOG_TAG,"Post"+jArrayMyPost.toString());
                    for(int i =0 ; i<jArrayMyPost.length();i++){
                        JSONObject buzzObject=jArrayMyPost.getJSONObject(i);
                        mPostDetailModel=new PostDetailModel();
                        mPostDetailModel.setPostId(buzzObject.getString("PostId"));
                        mPostDetailModel.setPostUserName(buzzObject.getString("PostUserName"));
                        mPostDetailModel.setPostFirstName(userObj.getString("FirstName"));
                        mPostDetailModel.setPostLastName(userObj.getString("LastName"));
                        mPostDetailModel.setPostText(buzzObject.getString("PostText"));
                        mPostDetailModel.setPostTime(buzzObject.getString("PostTime"));
                        mPostDetailModel.setPostDate(buzzObject.getString("PostDate"));
                        mPostDetailModel.setPostUpVote(buzzObject.getInt("PostUpVote"));
                        mPostDetailModel.setPostDownVote(buzzObject.getInt("PostDownVote"));
                        mPostDetailModel.setPostType(buzzObject.getString("PostType"));
                        mPostDetailModel.setIsShared(buzzObject.getString("IsShared"));
                        mPostDetailModel.setSharedCount(buzzObject.getString("ShareCount"));
                        mPostDetailModel.setSharedFrom(buzzObject.getString("SharedFrom"));
                        mPostDetailModel.setPostUpVoteValue(false);
                        mPostDetailModel.setPostDownVoteValue(false);
                        mPostDetailModel.setPostLocation(buzzObject.getString("PostLocation"));
                        mPostDetailModel.setPostUserPhoto(userObj.getString("UserPhoto"));
                        if(jObject.getString("userType").equals("rep")){
                            mPostDetailModel.setPostRepParty(userObj.getString("RepParty"));
                            mPostDetailModel.setPostRepDesignation(userObj.getString("RepDesignation"));
                            mPostDetailModel.setPostRepDetail(userObj.getString("RepDetail"));
                        }
                        JSONArray postImageArray=buzzObject.getJSONArray("images");
                        ArrayList<String>postImageArrayList = new ArrayList<>();
                        for(int j =0 ; j<postImageArray.length();j++) {
                            postImageArrayList.add(postImageArray.getString(j));
                        }
                        if(postImageArray.length()>0) {
                            mPostDetailModel.setPostImages(postImageArrayList);
                        }
                        else{
                            mPostDetailModel.setPostImages(null);
                        }
                        myPostList.add(mPostDetailModel);
                    }
                    editor.putString(Constants.MY_POST, new Gson().toJson(myPostList));
                }

                int noOfVotes= responseObject.getInt("NoOfVotes");
                if(noOfVotes>0){
                    JSONArray jArrayNewsVotes=responseObject.getJSONArray("Votes");
                    ArrayList<NewsDetailsModel> newsList;
                    ArrayList<NewsDetailsModel> newsListUpdated=new ArrayList<>();
                    Type type = new TypeToken<ArrayList<NewsDetailsModel>>() {}.getType();
                    newsList = new Gson().fromJson(sharedpreferences.getString(Constants.NEWS_LIST, ""), type);
                    for(int i = 0; i < newsList.size(); i++) {
                        NewsDetailsModel newsObj = newsList.get(i);
                        for (int j=0;j<noOfVotes;j++) {
                            JSONObject voteObj=jArrayNewsVotes.getJSONObject(j);
                            String newsIDVote=voteObj.getString("NewsId");
                            String newsVote=voteObj.getString("NewsVoteType");
                            if(newsObj.getNewsID().equals(newsIDVote)){
                                if(newsVote.equals(("1"))){
                                    newsObj.setNewsUpVoteValue(true);
                                }
                                else if(newsVote.equals("-1")){
                                    newsObj.setNewsDownVoteValue(true);
                                }
                            }
                        }
                        newsListUpdated.add(newsObj);
                    }
                    Log.e(LOG_TAG,"NEWS ELEMENT SIZE"+newsListUpdated.size());
                    editor.putString(Constants.NEWS_LIST, new Gson().toJson(newsListUpdated));
                }

                Log.e(LOG_TAG,"PromiseCountry");

                JSONArray jArrayPromiseCountry = responseObject.getJSONArray("PromisesCountry");
                Log.e(LOG_TAG,"PromiseCountry"+jArrayPromiseCountry.toString());
                for(int i =0 ; i<jArrayPromiseCountry.length();i++) {
                    JSONObject promiseObj=jArrayPromiseCountry.getJSONObject(i);
                    promiseModel=new PromiseModel();
                    promiseModel.setPromiseId(promiseObj.getString("PromiseId"));
                    promiseModel.setPromiseImageId(promiseObj.getString("PromiseImageId"));
                    promiseModel.setPromiseCountry(promiseObj.getString("Country"));
                    promiseModel.setPromiseState(promiseObj.getString("State"));
                    promiseModel.setPromiseCity(promiseObj.getString("City"));
                    promiseModel.setPromiseType(promiseObj.getString("PromiseType"));
                    promiseModel.setPromiseCount(promiseObj.getString("PromiseCount"));
                    promiseModel.setPromiseImage(promiseObj.getString("PromiseImage"));
                    promiseDetail.clear();
                    if(!promiseObj.getString("PromiseCount").equals("0")){
                        JSONArray jArrayPromiseDetail =promiseObj.getJSONArray("PromiseDetail");
                        Log.e(LOG_TAG, "PromiseDetail"+jArrayPromiseDetail.length());
                        for (int j = 0; j < jArrayPromiseDetail.length(); j++) {
                            JSONObject promiseDetailObj = jArrayPromiseDetail.getJSONObject(j);
                            promiseDetailModel = new PromiseDetailModel();
                            promiseDetailModel.setPromiseDetailId(promiseDetailObj.getString("PromiseDetailId"));
                            promiseDetailModel.setPromiseId(promiseDetailObj.getString("PromiseId"));
                            promiseDetailModel.setPromiseType(promiseDetailObj.getString("PromiseType"));
                            promiseDetailModel.setPromiseDetailName(promiseDetailObj.getString("PromiseDetailName"));
                            promiseDetailModel.setPromiseDetailSynopsis(promiseDetailObj.getString("PromiseDetailSynopsis"));
                            promiseDetail.add(promiseDetailModel);
                        }
                    }
                    promiseModel.setPromiseDetail(promiseDetail);
                    centralPromiseList.add(promiseModel);
                }
                editor.putString(Constants.PROMISE_COUNTRY, new Gson().toJson(centralPromiseList));

                JSONArray jArrayPromiseState = responseObject.getJSONArray("PromisesState");

                for(int i =0 ; i<jArrayPromiseState.length();i++) {
                    JSONObject promiseObj=jArrayPromiseState.getJSONObject(i);
                    promiseModel=new PromiseModel();
                    promiseModel.setPromiseId(promiseObj.getString("PromiseId"));
                    promiseModel.setPromiseImageId(promiseObj.getString("PromiseImageId"));
                    promiseModel.setPromiseCountry(promiseObj.getString("Country"));
                    promiseModel.setPromiseState(promiseObj.getString("State"));
                    promiseModel.setPromiseCity(promiseObj.getString("City"));
                    promiseModel.setPromiseType(promiseObj.getString("PromiseType"));
                    promiseModel.setPromiseCount(promiseObj.getString("PromiseCount"));
                    promiseModel.setPromiseImage(promiseObj.getString("PromiseImage"));
                    promiseDetail.clear();
                    if(!promiseObj.getString("PromiseCount").equals("0")){
                        JSONArray jArrayPromiseDetail =promiseObj.getJSONArray("PromiseDetail");
                        Log.e(LOG_TAG, "PromiseDetail"+jArrayPromiseDetail.length());
                        for (int j = 0; j < jArrayPromiseDetail.length(); j++) {
                            JSONObject promiseDetailObj = jArrayPromiseDetail.getJSONObject(j);
                            promiseDetailModel = new PromiseDetailModel();
                            promiseDetailModel.setPromiseDetailId(promiseDetailObj.getString("PromiseDetailId"));
                            promiseDetailModel.setPromiseId(promiseDetailObj.getString("PromiseId"));
                            promiseDetailModel.setPromiseType(promiseDetailObj.getString("PromiseType"));
                            promiseDetailModel.setPromiseDetailName(promiseDetailObj.getString("PromiseDetailName"));
                            promiseDetailModel.setPromiseDetailSynopsis(promiseDetailObj.getString("PromiseDetailSynopsis"));
                            promiseDetail.add(promiseDetailModel);
                        }
                    }
                    promiseModel.setPromiseDetail(promiseDetail);
                    statePromiseList.add(promiseModel);
                }
                editor.putString(Constants.PROMISE_STATE, new Gson().toJson(statePromiseList));


                JSONArray jArrayPromiseLocal = responseObject.getJSONArray("PromisesCity");

                for(int i =0 ; i<jArrayPromiseLocal.length();i++) {
                    JSONObject promiseObj=jArrayPromiseLocal.getJSONObject(i);
                    promiseModel=new PromiseModel();
                    promiseModel.setPromiseId(promiseObj.getString("PromiseId"));
                    promiseModel.setPromiseImageId(promiseObj.getString("PromiseImageId"));
                    promiseModel.setPromiseCountry(promiseObj.getString("Country"));
                    promiseModel.setPromiseState(promiseObj.getString("State"));
                    promiseModel.setPromiseCity(promiseObj.getString("City"));
                    promiseModel.setPromiseType(promiseObj.getString("PromiseType"));
                    promiseModel.setPromiseCount(promiseObj.getString("PromiseCount"));
                    promiseModel.setPromiseImage(promiseObj.getString("PromiseImage"));
                    promiseDetail.clear();
                    if(!promiseObj.getString("PromiseCount").equals("0")){
                        JSONArray jArrayPromiseDetail =promiseObj.getJSONArray("PromiseDetail");
                        Log.e(LOG_TAG, "PromiseDetail"+jArrayPromiseDetail.length());
                        for (int j = 0; j < jArrayPromiseDetail.length(); j++) {
                            JSONObject promiseDetailObj = jArrayPromiseDetail.getJSONObject(j);
                            promiseDetailModel = new PromiseDetailModel();
                            promiseDetailModel.setPromiseDetailId(promiseDetailObj.getString("PromiseDetailId"));
                            promiseDetailModel.setPromiseId(promiseDetailObj.getString("PromiseId"));
                            promiseDetailModel.setPromiseType(promiseDetailObj.getString("PromiseType"));
                            promiseDetailModel.setPromiseDetailName(promiseDetailObj.getString("PromiseDetailName"));
                            promiseDetailModel.setPromiseDetailSynopsis(promiseDetailObj.getString("PromiseDetailSynopsis"));
                            promiseDetail.add(promiseDetailModel);
                        }
                    }
                    promiseModel.setPromiseDetail(promiseDetail);
                    localPromiseList.add(promiseModel);
                }
                editor.putString(Constants.PROMISE_CITY, new Gson().toJson(localPromiseList));


                int noOfVotesPost= responseObject.getInt("NoOfVotes_Post");
                Log.e(LOG_TAG,String.valueOf(noOfVotesPost));
                if(noOfVotesPost>0){
                    JSONArray jArrayNewsVotes=responseObject.getJSONArray("PostVotes");
                    ArrayList<PostDetailModel> postListBuzz;
                    ArrayList<PostDetailModel> postListHome;
                    ArrayList<PostDetailModel> postListUpdatedBuzz=new ArrayList<>();
                    ArrayList<PostDetailModel> postListUpdatedHome=new ArrayList<>();
                    Type type = new TypeToken<ArrayList<PostDetailModel>>() {}.getType();
                    postListBuzz = new Gson().fromJson(sharedpreferences.getString(Constants.BUZZ_LIST, ""), type);
                    for(int i = 0; i < postListBuzz.size(); i++) {
                        PostDetailModel postObj = postListBuzz.get(i);
                        for (int j=0;j<noOfVotesPost;j++) {
                            JSONObject voteObj=jArrayNewsVotes.getJSONObject(j);
                            String newsIDVote=voteObj.getString("PostId");
                            String newsVote=voteObj.getString("PostVoteType");

                            if(postObj.getPostId().equals(newsIDVote)){
                                if(newsVote.equals(("1"))){

                                    postObj.setPostUpVoteValue(true);
                                }
                                else if(newsVote.equals("-1")){
                                    postObj.setPostDownVoteValue(true);
                                }
                            }
                        }
                        postListUpdatedBuzz.add(postObj);
                    }
                    Log.e(LOG_TAG,"BUZZ ELEMENT SIZE"+postListUpdatedBuzz.size());
                    editor.putString(Constants.BUZZ_LIST, new Gson().toJson(postListUpdatedBuzz));

                    postListHome = new Gson().fromJson(sharedpreferences.getString(Constants.HOME_LIST, ""), type);
                    for(int i = 0; i < postListHome.size(); i++) {
                        PostDetailModel postObj = postListHome.get(i);
                        for (int j=0;j<noOfVotesPost;j++) {
                            JSONObject voteObj=jArrayNewsVotes.getJSONObject(j);
                            String newsIDVote=voteObj.getString("PostId");

                            String newsVote=voteObj.getString("PostVoteType");
                            if(postObj.getPostId().equals(newsIDVote)){
                                if(newsVote.equals(("1"))){

                                    postObj.setPostUpVoteValue(true);
                                }
                                else if(newsVote.equals("-1")){
                                    postObj.setPostDownVoteValue(true);
                                }
                            }
                        }
                        postListUpdatedHome.add(postObj);
                    }
                    Log.e(LOG_TAG,"HOME ELEMENT SIZE"+postListUpdatedHome.size());
                    editor.putString(Constants.HOME_LIST, new Gson().toJson(postListUpdatedHome));


                    for(int i = 0; i < myPostList.size(); i++) {
                        PostDetailModel postObj = myPostList.get(i);
                        for (int j=0;j<noOfVotesPost;j++) {
                            JSONObject voteObj=jArrayNewsVotes.getJSONObject(j);
                            String newsIDVote=voteObj.getString("PostId");

                            String newsVote=voteObj.getString("PostVoteType");
                            if(postObj.getPostId().equals(newsIDVote)){
                                if(newsVote.equals(("1"))){

                                    postObj.setPostUpVoteValue(true);
                                }
                                else if(newsVote.equals("-1")){
                                    postObj.setPostDownVoteValue(true);
                                }
                            }
                        }
                        myPostListUpdated.add(postObj);
                    }
                    Log.e(LOG_TAG,"MY POST ELEMENT SIZE"+myPostListUpdated.size());
                    editor.putString(Constants.MY_POST, new Gson().toJson(myPostListUpdated));



                }



                centralList.clear();
                String hasPM=jObject.getString("hasPM");
                if(hasPM.equals("1")){
                    JSONArray pmArray=jObject.getJSONArray("PM");
                    JSONObject pmObj=pmArray.getJSONObject(0);
                    mRepInfoModel=new RepInfoModel();
                    mRepInfoModel=extractRepresentativeData(pmObj);
                    centralList.add(mRepInfoModel);
                }
                String hasMP=jObject.getString("hasMP");
                if(hasMP.equals("1")){
                    JSONArray mpArray=jObject.getJSONArray("MP");
                    JSONObject mpObj=mpArray.getJSONObject(0);
                    mRepInfoModel=new RepInfoModel();
                    mRepInfoModel=extractRepresentativeData(mpObj);
                    centralList.add(mRepInfoModel);
                }

                stateList.clear();
                String hasCM=jObject.getString("hasCM");
                if(hasCM.equals("1")){
                    JSONArray cmArray=jObject.getJSONArray("CM");
                    JSONObject cmObj=cmArray.getJSONObject(0);
                    mRepInfoModel=new RepInfoModel();
                    mRepInfoModel=extractRepresentativeData(cmObj);
                    stateList.add(mRepInfoModel);
                }
                String hasMLA=jObject.getString("hasMLA");
                if(hasMLA.equals("1")){
                    JSONArray mlaArray=jObject.getJSONArray("MLA");
                    JSONObject mlaObj=mlaArray.getJSONObject(0);
                    mRepInfoModel=new RepInfoModel();
                    mRepInfoModel=extractRepresentativeData(mlaObj);
                    stateList.add(mRepInfoModel);
                }

                localList.clear();
                String hasMayor=jObject.getString("hasMayor");
                if(hasMayor.equals("1")){
                    JSONArray mayorArray=jObject.getJSONArray("Mayor");
                    JSONObject mayorObj=mayorArray.getJSONObject(0);
                    mRepInfoModel=new RepInfoModel();
                    mRepInfoModel=extractRepresentativeData(mayorObj);
                    localList.add(mRepInfoModel);
                }
                String hasCouncillor=jObject.getString("hasCouncillor");
                if(hasCouncillor.equals("1")){
                    JSONArray councillorArray=jObject.getJSONArray("Councillor");
                    JSONObject councillorObj=councillorArray.getJSONObject(0);
                    mRepInfoModel=new RepInfoModel();
                    mRepInfoModel=extractRepresentativeData(councillorObj);
                    localList.add(mRepInfoModel);
                }




                editor.putBoolean(Constants.SETTINGS_IS_LOGGED,true);


                editor.putString(Constants.CENTRAL_ELECTED_MEMBERS, new Gson().toJson(centralList));
                editor.putString(Constants.STATE_ELECTED_MEMBERS, new Gson().toJson(stateList));
                editor.putString(Constants.LOCAL_ELECTED_MEMBERS, new Gson().toJson(localList));

                editor.commit();
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(LoginActivity.this, HomePageActivity.class);
                startActivity(i);

            }
            else if(checkStatus.equals("1")){
                Toast.makeText(this, "Please Check Your Username and Password", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Login Failure", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,e.toString());
        }

    }

    public RepInfoModel extractRepresentativeData(JSONObject obj) throws JSONException {
        RepInfoModel tempRepModel=new RepInfoModel();

        tempRepModel.setRepId(obj.getString("RepId"));
        tempRepModel.setRepCode(obj.getString("RepCode"));
        tempRepModel.setFullName(obj.getString("Full Name"));
        tempRepModel.setEmailId(obj.getString("EmailId"));

        tempRepModel.setOficeAddress(obj.getString("OfficeAddress"));
        tempRepModel.setRepParty(obj.getString("RepParty"));
        tempRepModel.setRepDesignation(obj.getString("RepDesignation"));
        tempRepModel.setIsCM(obj.getString("IsCM"));
        tempRepModel.setIsPM(obj.getString("IsPM"));

        tempRepModel.setIsMayor(obj.getString("IsMayor"));
        tempRepModel.setRepDetail(obj.getString("RepDetail"));
        tempRepModel.setRepQualification(obj.getString("RepQualification"));
        tempRepModel.setRepHomePage(obj.getString("RepHomePage"));
        tempRepModel.setRepTwitterId(obj.getString("RepTwitterId"));

        tempRepModel.setRepState(obj.getString("RepState"));
        tempRepModel.setRepDistrict(obj.getString("RepDistrict"));
        tempRepModel.setRepCity(obj.getString("RepCity"));
        tempRepModel.setRepWard(obj.getString("RepWard"));
        tempRepModel.setRepVidhanSabha(obj.getString("RepVidhanSabha"));
        tempRepModel.setRepLokSabha(obj.getString("RepLokSabha"));

        tempRepModel.setRepPinCode(obj.getString("RepPinCode"));
        tempRepModel.setRepAssumedOffice(obj.getString("RepAssumedOffice"));
        tempRepModel.setRepCareer(obj.getString("RepCareer"));
        tempRepModel.setRepControversy(obj.getString("RepControversy"));
        tempRepModel.setUserPhoto(obj.getString("UserPhoto"));



        return tempRepModel;
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(pDialog!=null)
            pDialog.dismiss();
    }
}