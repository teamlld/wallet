package teamlld.nik.uniobuda.hu.walletapp.fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

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
    BarGraphSeries<DataPoint> series;
    GraphView graphview;
    DatabaseHandler database;

    /*public static CategoryIncomeDiagramFragment newInstance() {

        Bundle args = new Bundle();
        CategoryIncomeDiagramFragment fragment = new CategoryIncomeDiagramFragment();
        fragment.setArguments(args);
        BaseActivity.database.addListener(fragment);
        return fragment;
    }*/

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

        graphview=(GraphView)getView().findViewById(R.id.categoryIncomeGraph);

        final DataPoint[] points= getDataPoints(true);
        series=new BarGraphSeries<DataPoint>(points);
        graphview.addSeries(series);

        SetGraphAttributes();
    }

    void SetGraphAttributes()
    {
        graphview.setTitle(String.valueOf(R.string.catIncomeDiagramTitle));
        graphview.setTitleTextSize(64);
        graphview.setTitleColor(Color.BLACK);
        graphview.getGridLabelRenderer().setVerticalAxisTitle(String.valueOf(R.string.diagramYAxisValueCurrency));

        graphview.getGridLabelRenderer().setHighlightZeroLines(true);
        graphview.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graphview.getGridLabelRenderer().setPadding(50);
        series.setSpacing(40);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        series.setValuesOnTopSize(45);
        series.setAnimated(true);

        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/6, (int) Math.abs(data.getY()*255/6), 100);
            }
        });


        /*graphview.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    return super.formatLabel(value, isValueX);
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX) + " HUF";
                }
            }
        });*/
    }

    DataPoint[] getDataPoints(boolean isIncome)
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
        DataPoint[] result=new DataPoint[cursorCat.getCount()];

        if (c.getCount() > 0)
        {
            for(int i=0;i<result.length;i++)
            {
                int catValueY=0;
                if (!c.isAfterLast() && categoriesId[i]==c.getInt(0))
                {
                    catValueY = c.getInt(1);
                    c.moveToNext();
                }
                result[i]=new DataPoint(i, catValueY);
            }
        }
        return  result;
    }


    @Override
    public void NewTransactionAdded(Transaction transaction) {
        series.resetData(getDataPoints(true));
    }
}
