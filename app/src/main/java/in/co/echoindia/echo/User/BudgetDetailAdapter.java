package in.co.echoindia.echo.User;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import in.co.echoindia.echo.Model.SpendingDetailModel;
import in.co.echoindia.echo.R;

/**
 * Created by Danish Rafique on 29-04-2017.
 */

public class BudgetDetailAdapter extends BaseAdapter {

    ArrayList<SpendingDetailModel> spendingDetailModels = new ArrayList<>();
    Activity activity;
    TextView budgetDetailName,budgetDetailValue;
    ImageView budgetColor;

    ArrayList<Integer> colors = new ArrayList<Integer>();



    public BudgetDetailAdapter(Activity activity, ArrayList<SpendingDetailModel> spendingDetailModels) {
        this.activity = activity;
        this.spendingDetailModels = spendingDetailModels;
    }

    @Override
    public int getCount() {
        if(spendingDetailModels != null)
            return spendingDetailModels.size();
        else
            return  0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_budget_detail, null);
        final SpendingDetailModel spendingDetailObj = spendingDetailModels.get(position);
        budgetDetailName=(TextView)convertView.findViewById(R.id.budget_detail_name);
        budgetDetailValue=(TextView)convertView.findViewById(R.id.budget_detail_value);
        budgetColor=(ImageView)convertView.findViewById(R.id.budget_color);
        budgetDetailName.setText(spendingDetailObj.getSpendingType());
        float budget=Float.parseFloat(spendingDetailObj.getSpendingTypeBudget());
        budgetDetailValue.setText((budget/10000000)+" Cr");




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

        budgetColor.setBackgroundColor(colors.get(position));



        return convertView;
    }
}
