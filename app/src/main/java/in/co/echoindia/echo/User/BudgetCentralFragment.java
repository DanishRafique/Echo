package in.co.echoindia.echo.User;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.gson.Gson;

import java.util.ArrayList;

import in.co.echoindia.echo.Model.SpendingDetailModel;
import in.co.echoindia.echo.Model.SpendingModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

/**
 * Created by Danish Rafique on 03-05-2017.
 */

public class BudgetCentralFragment extends Fragment implements OnChartValueSelectedListener {

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private static final String LOG_TAG = "BudgetCentralFragment";
    private PieChart mChart;
    SpendingModel centralBudget;
    ArrayList<SpendingDetailModel> centralBudgetDetail=new ArrayList<>();
    TextView totalBudget;
    ListView budgetListView;
    BudgetDetailAdapter budgetDetailAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_budget, container, false);
        sharedpreferences = AppUtil.getAppPreferences(getActivity());
        editor = sharedpreferences.edit();

        totalBudget=(TextView)v.findViewById(R.id.budget_total);

        mChart = (PieChart) v.findViewById(R.id.budget_pie);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(40f);
        mChart.setTransparentCircleRadius(45f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" ?");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);


        centralBudget = new Gson().fromJson(sharedpreferences.getString(Constants.BUDGET_COUNTRY, ""), SpendingModel.class);

        centralBudgetDetail=centralBudget.getSpendingDetail();
        ArrayList<PieEntry> centralPieEntry=new ArrayList<>();
        for(int i=0;i<centralBudgetDetail.size();i++){
            centralPieEntry.add(i,new PieEntry(Float.parseFloat(centralBudgetDetail.get(i).getSpendingTypeBudget()),centralBudgetDetail.get(i).getSpendingType()));
        }

        PieDataSet dataSet = new PieDataSet(centralPieEntry, "Central Budgets");
        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
       // int colorCodePrimary[]={R.color.custom_progress_green_header,R.color.custom_progress_orange_header,R.color.custom_progress_blue_header,R.color.custom_progress_purple_header,R.color.custom_progress_red_header};

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);
        mChart.highlightValues(null);

        mChart.invalidate();

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);


        totalBudget.setText(" "+Float.parseFloat(centralBudget.getSpendingBudget())/10000000+" Cr");

        budgetListView=(ListView)v.findViewById(R.id.budget_list_view);
        budgetDetailAdapter = new BudgetDetailAdapter(getActivity(), centralBudgetDetail);
        budgetListView.setAdapter(budgetDetailAdapter);
        budgetDetailAdapter.notifyDataSetChanged();

        mChart.setCenterText(centralBudget.getSpendingCentral());



        return v;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.e(LOG_TAG,"VAL SELECTED "+
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {

    }
}
