package in.co.echoindia.echo.Model;

import java.io.Serializable;

/**
 * Created by Danish Rafique on 19-04-2017.
 */

public class LokSabhaModel implements Serializable {
    private String ConstituencyId="";
    private String ConstituencyName="";
    private String ConstituencyReserved="";
    private String ConstituencyState="";

    public String getConstituencyId() {
        return ConstituencyId;
    }

    public void setConstituencyId(String constituencyId) {
        ConstituencyId = constituencyId;
    }

    public String getConstituencyName() {
        return ConstituencyName;
    }

    public void setConstituencyName(String constituencyName) {
        ConstituencyName = constituencyName;
    }

    public String getConstituencyReserved() {
        return ConstituencyReserved;
    }

    public void setConstituencyReserved(String constituencyReserved) {
        ConstituencyReserved = constituencyReserved;
    }

    public String getConstituencyState() {
        return ConstituencyState;
    }

    public void setConstituencyState(String constituencyState) {
        ConstituencyState = constituencyState;
    }
}
