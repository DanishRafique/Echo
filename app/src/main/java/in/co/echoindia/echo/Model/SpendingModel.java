package in.co.echoindia.echo.Model;

import java.util.ArrayList;

/**
 * Created by Danish Rafique on 30-04-2017.
 */

public class SpendingModel {
    private String SpendingId = "";
    private String SpendingCentral="";
    private String SpendingState = "";
    private String SpendingLocal="";
    private String SpendingBudget = "";
    private ArrayList<SpendingDetailModel> SpendingDetail;

    public ArrayList<SpendingDetailModel> getSpendingDetail() {
        return SpendingDetail;
    }

    public void setSpendingDetail(ArrayList<SpendingDetailModel> spendingDetail) {
        SpendingDetail = spendingDetail;
    }

    public String getSpendingId() {
        return SpendingId;
    }

    public void setSpendingId(String spendingId) {
        SpendingId = spendingId;
    }

    public String getSpendingCentral() {
        return SpendingCentral;
    }

    public void setSpendingCentral(String spendingCentral) {
        SpendingCentral = spendingCentral;
    }

    public String getSpendingState() {
        return SpendingState;
    }

    public void setSpendingState(String spendingState) {
        SpendingState = spendingState;
    }

    public String getSpendingLocal() {
        return SpendingLocal;
    }

    public void setSpendingLocal(String spendingLocal) {
        SpendingLocal = spendingLocal;
    }

    public String getSpendingBudget() {
        return SpendingBudget;
    }

    public void setSpendingBudget(String spendingBudget) {
        SpendingBudget = spendingBudget;
    }
}
