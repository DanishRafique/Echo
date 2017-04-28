package in.co.echoindia.echo.Model;

import java.io.Serializable;

/**
 * Created by Danish Rafique on 28-04-2017.
 */

public class PromiseDetailModel implements Serializable{
    private String PromiseDetailId="";
    private String PromiseId="";
    private String PromiseType="";
    private String PromiseDetailName="";
    private String PromiseDetailSynopsis="";

    public String getPromiseDetailId() {
        return PromiseDetailId;
    }

    public void setPromiseDetailId(String promiseDetailId) {
        PromiseDetailId = promiseDetailId;
    }

    public String getPromiseId() {
        return PromiseId;
    }

    public void setPromiseId(String promiseId) {
        PromiseId = promiseId;
    }

    public String getPromiseType() {
        return PromiseType;
    }

    public void setPromiseType(String promiseType) {
        PromiseType = promiseType;
    }

    public String getPromiseDetailName() {
        return PromiseDetailName;
    }

    public void setPromiseDetailName(String promiseDetailName) {
        PromiseDetailName = promiseDetailName;
    }

    public String getPromiseDetailSynopsis() {
        return PromiseDetailSynopsis;
    }

    public void setPromiseDetailSynopsis(String promiseDetailSynopsis) {
        PromiseDetailSynopsis = promiseDetailSynopsis;
    }
}
