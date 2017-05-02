package teamlld.nik.uniobuda.hu.walletapp;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Date;

/**
 * Created by GERGO on 2017.04.08..
 */

public class DiagramFragment extends Fragment implements NewTransactionListener {

    View rootView;
    int maxGraphItem=20;
    LineGraphSeries<DataPoint> graphPoints;
    GraphView graphview;
    User user;

    public static DiagramFragment newInstance(User user) {

        Bundle args = new Bundle();
        args.putParcelable("user",user);
        DiagramFragment fragment = new DiagramFragment();
        fragment.setArguments(args);
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

        BaseActivity.database.addListener(this);

        user = getArguments().getParcelable("user");

        graphPoints=new LineGraphSeries<DataPoint>();
        graphview=(GraphView)getView().findViewById(R.id.balanceGraph);

        DataPoint[] points= getDataPoints();
        for (int i=0;i<points.length;i++)
        {
            graphPoints.appendData(points[i], true, maxGraphItem);
        }
        setGraphProperties();

    }
    void setGraphProperties()
    {
        graphview.addSeries(graphPoints);
        graphview.setTitle("Balance chart");
        graphview.setTitleTextSize(64);
        graphview.setTitleColor(Color.RED);
        graphview.setPadding(10,0,0,10);
        setAxisLabels();
        graphview.getViewport().setScalable(true);

        final android.text.format.DateFormat df = new android.text.format.DateFormat();
        graphview.getGridLabelRenderer().setLabelFormatter(
                new DateAsXAxisLabelFormatter(getActivity()));/*,
                            SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT)) {
                        @Override
                        public String formatLabel(double value, boolean isValueX) {
                            if (isValueX)
                                return super.formatLabel(value, isValueX);
                            else
                            return super.formatLabel(value,isValueX);
                        }
                    });*/
        graphview.getGridLabelRenderer().setVerticalLabelsAlign(Paint.Align.RIGHT);
        graphview.getGridLabelRenderer().setHorizontalLabelsAngle(30);
        graphview.getGridLabelRenderer().setNumHorizontalLabels(3);
        graphview.getGridLabelRenderer().setNumVerticalLabels(5);
        graphview.getGridLabelRenderer().setLabelsSpace(10);  // tengelyfeliratok a grapview-tól
        graphview.getGridLabelRenderer().setPadding(20);
        graphPoints.setDrawDataPoints(true);
        graphPoints.setDataPointsRadius(15f);
        graphPoints.setBackgroundColor(Color.RED);
        graphPoints.setThickness(8);
        graphPoints.setAnimated(true);
            /*graphview.getGridLabelRenderer().setHorizontalAxisTitle("Date");
            graphview.getGridLabelRenderer().setHorizontalAxisTitleTextSize(48);*/
    }

    DataPoint[] getDataPoints()
    {

        Cursor c = BaseActivity.database.getAllTransactionsOrderByDate(user.getId(),false);

        DataPoint[] result=new DataPoint[c.getCount()];

        if (c.getCount() > 0)
        {
            double yvalue=c.getInt(c.getColumnIndex("income")) == 1 ? c.getInt(c.getColumnIndex("value")) : c.getInt(c.getColumnIndex("value")) * (-1);
            // TODO esetleg az első érték lehetne alapból a balance értéke a usernek
            // user.getBalance() -al már el lehet érni
            Date date = new Date(c.getLong(c.getColumnIndex("date")));

            result[0]=new DataPoint(date, yvalue);
            c.moveToNext();


            for(int i=1;i<result.length && !c.isAfterLast();i++)
            {
                // meg kell nézni az előző értékhez képest és úgy beállítani az értéket
                double d=result[i-1].getY();
                double cur=c.getInt(c.getColumnIndex("income")) == 1 ? c.getInt(c.getColumnIndex("value")) : c.getInt(c.getColumnIndex("value")) * (-1);
                yvalue=d+ cur;
                date = new Date(c.getLong(c.getColumnIndex("date")));
                result[i]=new DataPoint(date, yvalue);
                c.moveToNext();
            }
        }


        return  result;
    }

    void setAxisLabels()
    {
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
