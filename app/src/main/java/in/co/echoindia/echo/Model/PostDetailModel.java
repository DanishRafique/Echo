package in.co.echoindia.echo.Model;

import java.util.ArrayList;

/**
 * Created by Danish Rafique on 18-04-2017.
 */

public class PostDetailModel {
    private String postId = "";
    private String postFirstName="";
    private String postLastName="";
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
    private String postRepDetail="";
    private boolean postUpVoteValue;
    private boolean postDownVoteValue;
    private String isShared="";
    private String SharedFrom="";
    private String SharedCount="";
    private String PostImageRef="";
    private String SharedFromUserName="";
    private String PostLocation="";

    public String getPostLocation() {
        return PostLocation;
    }

    public void setPostLocation(String postLocation) {
        PostLocation = postLocation;
    }

    public String getSharedFromUserName() {
        return SharedFromUserName;
    }

    public void setSharedFromUserName(String sharedFromUserName) {
        SharedFromUserName = sharedFromUserName;
    }

    public String getPostImageRef() {
        return PostImageRef;
    }

    public void setPostImageRef(String postImageRef) {
        PostImageRef = postImageRef;
    }

    public String getSharedCount() {
        return SharedCount;
    }

    public void setSharedCount(String sharedCount) {
        SharedCount = sharedCount;
    }

    public String getIsShared() {
        return isShared;
    }

    public void setIsShared(String isShared) {
        this.isShared = isShared;
    }

    public String getSharedFrom() {
        return SharedFrom;
    }

    public void setSharedFrom(String sharedFrom) {
        SharedFrom = sharedFrom;
    }

    public boolean isPostUpVoteValue() {
        return postUpVoteValue;
    }

    public void setPostUpVoteValue(boolean postUpVoteValue) {
        this.postUpVoteValue = postUpVoteValue;
    }

    public boolean isPostDownVoteValue() {
        return postDownVoteValue;
    }

    public void setPostDownVoteValue(boolean postDownVoteValue) {
        this.postDownVoteValue = postDownVoteValue;
    }

    public String getPostRepDetail() {
        return postRepDetail;
    }

    public void setPostRepDetail(String postRepDetail) {
        this.postRepDetail = postRepDetail;
    }

    public String getPostFirstName() {
        return postFirstName;
    }

    public void setPostFirstName(String postFirstName) {
        this.postFirstName = postFirstName;
    }

    public String getPostLastName() {
        return postLastName;
    }

    public void setPostLastName(String postLastName) {
        this.postLastName = postLastName;
    }

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
