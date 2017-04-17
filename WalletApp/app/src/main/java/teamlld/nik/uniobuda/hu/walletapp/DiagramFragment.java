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
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;

/**
 * Created by GERGO on 2017.04.08..
 */

public class DiagramFragment extends Fragment implements NewTransactionListener{

    View rootView;
    int maxGraphItem=20;
    LineGraphSeries<DataPoint> graphPoints;
    GraphView graphview;

    public static DiagramFragment newInstance(int userId) {

        Bundle args = new Bundle();
        args.putInt("userid",userId);
        DiagramFragment fragment = new DiagramFragment();
        fragment.setArguments(args);
        MainActivity.handler.addListener(fragment);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_diagram, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        graphPoints=new LineGraphSeries<DataPoint>();
        graphview=(GraphView)getView().findViewById(R.id.balanceGraph);

        DataPoint[] points= getDataPoints();
        for (int i=0;i<points.length;i++)
        {
            graphPoints.appendData(points[i],true,maxGraphItem);
        }

        graphview.addSeries(graphPoints);
        graphview.setTitle("Balance chart");
        setAxisLabels();
        graphview.getViewport().setYAxisBoundsManual(true);
        graphview.getViewport().setXAxisBoundsManual(true);
        graphview.getGridLabelRenderer().setLabelFormatter(
                new DateAsXAxisLabelFormatter(getActivity(),
                        SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT)) {
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if (isValueX)
                            return String.valueOf(value); //super.formatLabel(value, isValueX);
                        else
                            return super.formatLabel(value, isValueX);
                    }
                });
        graphview.getGridLabelRenderer().setNumHorizontalLabels(3);
        graphview.getGridLabelRenderer().setNumVerticalLabels(5);
        graphPoints.setDrawDataPoints(true);
        graphPoints.setDataPointsRadius(15f);
        graphPoints.setBackgroundColor(Color.RED);
        graphPoints.setThickness(8);
        graphPoints.setAnimated(true);

        //graphview.getGridLabelRenderer().setHumanRounding(false);
    }

    DataPoint[] getDataPoints()
    {
        Cursor c = MainActivity.handler.getAllTransactions(getArguments().getInt("userid"),false);
        DataPoint[] result=new DataPoint[c.getCount()];
        for(int i=0;i<result.length && !c.isAfterLast();i++)
        {
            result[i]=new DataPoint(c.getLong(c.getColumnIndex("date")), c.getInt(c.getColumnIndex("income")) == 1 ? c.getInt(c.getColumnIndex("value")) : c.getInt(c.getColumnIndex("value")) * (-1));
            c.moveToNext();

        }

        return  result;
    }

    void setAxisLabels()
    {
        //TODO új pont felvételénél nem köti össze
        graphview.getViewport().setMinY(graphPoints.getLowestValueY());
        graphview.getViewport().setMaxY(graphPoints.getHighestValueY());
        graphview.getViewport().setMinX(graphPoints.getLowestValueX());
        graphview.getViewport().setMaxX(graphPoints.getHighestValueX());
    }

    @Override
    public void NewTransactionAdded(Transaction transaction) {
        graphPoints.resetData(getDataPoints());
        setAxisLabels();
    }

}
