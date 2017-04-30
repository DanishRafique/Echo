package in.co.echoindia.echo.Model;

/**
 * Created by Danish Rafique on 30-04-2017.
 */

public class SpendingDetailModel {
    private String SpendingDetailId = "";
    private String SpendingId="";
    private String SpendingType = "";
    private String SpendingTypeBudget="";

    public String getSpendingDetailId() {
        return SpendingDetailId;
    }

    public void setSpendingDetailId(String spendingDetailId) {
        SpendingDetailId = spendingDetailId;
    }

    public String getSpendingId() {
        return SpendingId;
    }

    public void setSpendingId(String spendingId) {
        SpendingId = spendingId;
    }

    public String getSpendingType() {
        return SpendingType;
    }

    public void setSpendingType(String spendingType) {
        SpendingType = spendingType;
    }

    public String getSpendingTypeBudget() {
        return SpendingTypeBudget;
    }

    public void setSpendingTypeBudget(String spendingTypeBudget) {
        SpendingTypeBudget = spendingTypeBudget;
    }
}
