package teamlld.nik.uniobuda.hu.walletapp;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

/**
 * Created by GERGO on 2017.05.02..
 */

public class CategoryIncomeDiagramFragment extends Fragment implements NewTransactionListener {


    View rootView;
    int maxGraphItem=20;
    BarGraphSeries<DataPoint> graphPoints;
    GraphView graphview;

    public static CategoryIncomeDiagramFragment newInstance() {

        Bundle args = new Bundle();
        CategoryIncomeDiagramFragment fragment = new CategoryIncomeDiagramFragment();
        fragment.setArguments(args);
        BaseActivity.database.addListener(fragment);
        return fragment;
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

        graphPoints=new BarGraphSeries<DataPoint>();
        graphview=(GraphView)getView().findViewById(R.id.categoryIncomeGraph);
        graphview.setTitle("Kategóriák szerinti bevételek");
        graphview.setTitleTextSize(64);
        graphview.setTitleColor(Color.BLACK);
        graphview.setPadding(10,0,0,10);

        DataPoint[] points= getDataPoints(true);
        BarGraphSeries<DataPoint> series=new BarGraphSeries<DataPoint>(points);

        graphview.addSeries(series);

        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });
        series.setSpacing(50);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        series.setValuesOnTopSize(50);
        series.setAnimated(true);

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

        //TODO 0 érték az y tengelyen legyen az alap
    }

    DataPoint[] getDataPoints(boolean isIncome)
    {
        Cursor cursorCat=BaseActivity.database.getTypes(isIncome);
        int[] categoriesId=new int[cursorCat.getCount()];
        for(int i=0; !cursorCat.isAfterLast();i++)
        {
            categoriesId[i]=cursorCat.getInt(0);
            cursorCat.moveToNext();
        }


        //FIXME userid honnan jön
        Cursor c = BaseActivity.database.getAllTransactionsGroupByCategory(1000, isIncome);

        //cursorCat.getCount() a hossza ha minden kategóriát ki akarunk rakni majd
        DataPoint[] result=new DataPoint[c.getCount()];

        if (c.getCount() > 0)
        {
            int j=0;
            for(int i=0;i<result.length && !c.isAfterLast();i++)
            {
                int catId=c.getInt(0);
                int catCount=0;
                while(j<cursorCat.getCount() && catId>=categoriesId[j])
                {
                    if (catId == categoriesId[j]) {
                        catCount = c.getInt(1);
                    }
                    j++;
                }

                result[i]=new DataPoint(catId, catCount);
                c.moveToNext();
            }
        }
        return  result;
    }


    @Override
    public void NewTransactionAdded(Transaction transaction) {

    }
}
