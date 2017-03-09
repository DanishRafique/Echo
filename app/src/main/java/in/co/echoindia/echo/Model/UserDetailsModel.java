package in.co.echoindia.echo.Model;

import java.io.Serializable;

/**
 * Created by Danish Rafique on 09-03-2017.
 */

public class UserDetailsModel implements Serializable{
    private String userName="";
    private String password="";
    private String firstName="";
    private String lastName="";
    private String emailId="";
    private String phoneNo="";
    private String address="";
    private String ward="";
    private String city="";
    private String pinCode="";
    private String district="";
    private String state="";
    private String userPhoto="";
    private String aadhaarPhoto="";
    private String voterIdPhoto="";
    private String isVerified="";
    private String issueMaker="";

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }



    public String getIssueMaker() {
        return issueMaker;
    }

    public void setIssueMaker(String issueMaker) {
        this.issueMaker = issueMaker;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getAadhaarPhoto() {
        return aadhaarPhoto;
    }

    public void setAadhaarPhoto(String aadhaarPhoto) {
        this.aadhaarPhoto = aadhaarPhoto;
    }

    public String getVoterIdPhoto() {
        return voterIdPhoto;
    }

    public void setVoterIdPhoto(String voterIdPhoto) {
        this.voterIdPhoto = voterIdPhoto;
    }



}
