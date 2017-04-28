package in.co.echoindia.echo.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Danish Rafique on 28-04-2017.
 */

public class PromiseModel implements Serializable {
    private String PromiseId="";
    private String PromiseImageId="";
    private String PromiseCountry="";
    private String PromiseState="";
    private String PromiseCity="";
    private String PromiseType="";
    private String PromiseCount="";
    private String PromiseImage="";
    private ArrayList<PromiseDetailModel> PromiseDetail;

    public String getPromiseId() {
        return PromiseId;
    }

    public void setPromiseId(String promiseId) {
        PromiseId = promiseId;
    }

    public String getPromiseImageId() {
        return PromiseImageId;
    }

    public void setPromiseImageId(String promiseImageId) {
        PromiseImageId = promiseImageId;
    }

    public String getPromiseCountry() {
        return PromiseCountry;
    }

    public void setPromiseCountry(String promiseCountry) {
        PromiseCountry = promiseCountry;
    }

    public String getPromiseState() {
        return PromiseState;
    }

    public void setPromiseState(String promiseState) {
        PromiseState = promiseState;
    }

    public String getPromiseCity() {
        return PromiseCity;
    }

    public void setPromiseCity(String promiseCity) {
        PromiseCity = promiseCity;
    }

    public String getPromiseType() {
        return PromiseType;
    }

    public void setPromiseType(String promiseType) {
        PromiseType = promiseType;
    }

    public String getPromiseCount() {
        return PromiseCount;
    }

    public void setPromiseCount(String promiseCount) {
        PromiseCount = promiseCount;
    }

    public String getPromiseImage() {
        return PromiseImage;
    }

    public void setPromiseImage(String promiseImage) {
        PromiseImage = promiseImage;
    }

    public ArrayList<PromiseDetailModel> getPromiseDetail() {
        return PromiseDetail;
    }

    public void setPromiseDetail(ArrayList<PromiseDetailModel> promiseDetail) {
        PromiseDetail = promiseDetail;
    }
}
