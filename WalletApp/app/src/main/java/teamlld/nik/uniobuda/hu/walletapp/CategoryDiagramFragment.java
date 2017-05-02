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

public class CategoryDiagramFragment extends Fragment implements NewTransactionListener {


    View rootView;
    int maxGraphItem=20;
    BarGraphSeries<DataPoint> graphPoints;
    GraphView graphview;

    public static CategoryDiagramFragment newInstance() {

        Bundle args = new Bundle();
        CategoryDiagramFragment fragment = new CategoryDiagramFragment();
        fragment.setArguments(args);
        BaseActivity.database.addListener(fragment);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_category_diagram, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        graphPoints=new BarGraphSeries<DataPoint>();
        graphview=(GraphView)getView().findViewById(R.id.balanceGraph);

        DataPoint[] points= getDataPoints();
        for (int i=0;i<points.length;i++)
        {
            graphPoints.appendData(points[i], true, maxGraphItem);
        }
        setGraphProperties();

    }

    DataPoint[] getDataPoints()
    {
        //FIXME userid honnan jön
        Cursor c = BaseActivity.database.getAllTransactionsGroupByCategory(1000);
        DataPoint[] result=new DataPoint[c.getCount()];
        c.moveToFirst();

        if (c.getCount() > 0)
        {
            for(int i=0;i<result.length && !c.isAfterLast();i++)
            {
                int cat=c.getInt(0);
                /*Cursor c2=BaseActivity.database.getTypeById(cat);
                String name=c2.getString(c2.getColumnIndex("name"));*/
                result[i]=new DataPoint(cat, c.getInt(1));
                c.moveToNext();
            }
        }

        return  result;
    }

    void setGraphProperties()
    {
        graphPoints.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        graphPoints.setSpacing(50);
        graphPoints.setDrawValuesOnTop(true);
        graphPoints.setValuesOnTopColor(Color.RED);
    }

    @Override
    public void NewTransactionAdded(Transaction transaction) {

    }
}
