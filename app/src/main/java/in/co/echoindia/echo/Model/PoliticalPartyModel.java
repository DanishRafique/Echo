package in.co.echoindia.echo.Model;

import java.io.Serializable;

/**
 * Created by Danish Rafique on 20-04-2017.
 */

public class PoliticalPartyModel implements Serializable{
    private String PartyId="";
    private String PartyName="";
    private String PartyNameShort="";
    private String PartyLogo="";

    public String getPartyId() {
        return PartyId;
    }

    public void setPartyId(String partyId) {
        PartyId = partyId;
    }

    public String getPartyName() {
        return PartyName;
    }

    public void setPartyName(String partyName) {
        PartyName = partyName;
    }

    public String getPartyNameShort() {
        return PartyNameShort;
    }

    public void setPartyNameShort(String partyNameShort) {
        PartyNameShort = partyNameShort;
    }

    public String getPartyLogo() {
        return PartyLogo;
    }

    public void setPartyLogo(String partyLogo) {
        PartyLogo = partyLogo;
    }
}
