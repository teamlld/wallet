package teamlld.nik.uniobuda.hu.walletapp.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import teamlld.nik.uniobuda.hu.walletapp.NewTransactionListener;
import teamlld.nik.uniobuda.hu.walletapp.R;
import teamlld.nik.uniobuda.hu.walletapp.data.DatabaseHandler;
import teamlld.nik.uniobuda.hu.walletapp.models.Transaction;

/**
 * Created by GERGO on 2017.05.02..
 */

public class CategoryIncomeDiagramFragment extends Fragment implements NewTransactionListener {

    View rootView;
    int maxGraphItem=20;
    BarChart chart;
    DatabaseHandler database;

    public CategoryIncomeDiagramFragment()
    {
        database=DatabaseHandler.getInstance(getContext());
        database.addListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_category_income_diagram, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        chart=(BarChart)getView().findViewById(R.id.incomeChart);

        BarDataSet dataset=new BarDataSet(getDataPoints(true),"categories");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        dataset.setValueTextSize(15f);

        BarData data=new BarData(dataset);
        chart.setData(data);

        SetGraphAttributes();
    }

    void SetGraphAttributes()
    {
        List<String> labels= Arrays.asList(getResources().getStringArray(R.array.types_income));
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        Description desc=new Description();
        desc.setText("");
        chart.setDescription(desc);
        chart.getLegend().setEnabled(false);

        XAxis x=chart.getXAxis();
        x.setTextSize(12f);
        x.setDrawGridLines(false);
        x.setLabelRotationAngle(30);

        YAxis yl=chart.getAxisLeft();
        YAxis yr=chart.getAxisRight();
        yl.setTextSize(12f);
        yr.setTextSize(12f);

        chart.animateY(500);
        chart.setExtraBottomOffset(10f);
    }

    List<BarEntry> getDataPoints(boolean isIncome)
    {
        Cursor cursorCat=database.getTypes(isIncome);
        int[] categoriesId=new int[cursorCat.getCount()];
        for(int i=0; !cursorCat.isAfterLast();i++)
        {
            categoriesId[i]=cursorCat.getInt(0);
            cursorCat.moveToNext();
        }

        //FIXME userid honnan jön
        Cursor c = database.getAllTransactionsGroupByCategory(0, isIncome);
        List<BarEntry> result=new ArrayList<BarEntry>();

        if (c.getCount() > 0)
        {
            for(int i=0;i<cursorCat.getCount();i++)
            {
                int catValueY=0;
                if (!c.isAfterLast() && categoriesId[i]==c.getInt(0))
                {
                    catValueY = c.getInt(1);
                    c.moveToNext();
                }
                result.add(new BarEntry(i, catValueY));
            }
        }
        else
        {
            //azért kell hogy ne legyen a diagram helye üres ha még nincs tranzakció
            for(int i=0;i<categoriesId.length;i++)
            {
                result.add(new BarEntry(i,0));
            }
        }
        return  result;
    }


    @Override
    public void NewTransactionAdded(Transaction transaction) {
        chart.invalidate();
    }
}
