package in.co.echoindia.echo.Model;

import java.io.Serializable;

/**
 * Created by Danish Rafique on 19-04-2017.
 */

public class VidhanSabhaModel implements Serializable {
    private String ConstituencyId="";
    private String ConstituencyName="";
    private String ConstituencyDistrict="";
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

    public String getConstituencyDistrict() {
        return ConstituencyDistrict;
    }

    public void setConstituencyDistrict(String constituencyDistrict) {
        ConstituencyDistrict = constituencyDistrict;
    }

    public String getConstituencyState() {
        return ConstituencyState;
    }

    public void setConstituencyState(String constituencyState) {
        ConstituencyState = constituencyState;
    }
}
