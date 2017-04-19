package in.co.echoindia.echo.Model;

import java.io.Serializable;

/**
 * Created by Danish Rafique on 19-04-2017.
 */

public class MunicipalCorporationModel implements Serializable {

    private String WardId="";
    private String WardState="";
    private String WardCity="";
    private String WardCityRegion="";
    private int WardStartNumber;
    private int WardEndNumber;

    public String getWardId() {
        return WardId;
    }

    public void setWardId(String wardId) {
        WardId = wardId;
    }

    public String getWardState() {
        return WardState;
    }

    public void setWardState(String wardState) {
        WardState = wardState;
    }

    public String getWardCity() {
        return WardCity;
    }

    public void setWardCity(String wardCity) {
        WardCity = wardCity;
    }

    public String getWardCityRegion() {
        return WardCityRegion;
    }

    public void setWardCityRegion(String wardCityRegion) {
        WardCityRegion = wardCityRegion;
    }

    public int getWardStartNumber() {
        return WardStartNumber;
    }

    public void setWardStartNumber(int wardStartNumber) {
        WardStartNumber = wardStartNumber;
    }

    public int getWardEndNumber() {
        return WardEndNumber;
    }

    public void setWardEndNumber(int wardEndNumber) {
        WardEndNumber = wardEndNumber;
    }
}
