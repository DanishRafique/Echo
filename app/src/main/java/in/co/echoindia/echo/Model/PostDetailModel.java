package in.co.echoindia.echo.Model;

import java.util.ArrayList;

/**
 * Created by Danish Rafique on 18-04-2017.
 */

public class PostDetailModel {
    private String postId = "";
    private String postUserName = "";
    private String postText = "";
    private String postTime = "";
    private String postDate = "";
    private int postUpVote;
    private int postDownVote;
    private String postType="";
    private String postUserPhoto="";
    private String postRepParty="";
    private String postRepDesignation="";
    private ArrayList<String> postImages;

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostUserName() {
        return postUserName;
    }

    public void setPostUserName(String postUserName) {
        this.postUserName = postUserName;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public int getPostUpVote() {
        return postUpVote;
    }

    public void setPostUpVote(int postUpVote) {
        this.postUpVote = postUpVote;
    }

    public int getPostDownVote() {
        return postDownVote;
    }

    public void setPostDownVote(int postDownVote) {
        this.postDownVote = postDownVote;
    }

    public String getPostUserPhoto() {
        return postUserPhoto;
    }

    public void setPostUserPhoto(String postUserPhoto) {
        this.postUserPhoto = postUserPhoto;
    }

    public String getPostRepParty() {
        return postRepParty;
    }

    public void setPostRepParty(String postRepParty) {
        this.postRepParty = postRepParty;
    }

    public String getPostRepDesignation() {
        return postRepDesignation;
    }

    public void setPostRepDesignation(String postRepDesignation) {
        this.postRepDesignation = postRepDesignation;
    }

    public ArrayList<String> getPostImages() {
        return postImages;
    }

    public void setPostImages(ArrayList<String> postImages) {
        this.postImages = postImages;
    }
}
