package teamlld.nik.uniobuda.hu.walletapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by GERGO on 2017.04.08..
 */

public class DiagramFragment extends Fragment implements NewTransactionListener{

    View rootView;
    int maxGraphItem=20;
    LineGraphSeries<DataPoint> graphPoints;
    GraphView graphview;

    public static DiagramFragment newInstance() {

        Bundle args = new Bundle();
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

        graphview=(GraphView)getView().findViewById(R.id.balanceGraph);

        //TODO userId elérése + a DBhandlernek listát kellene visszadnia kurzor helyett itt

        Cursor c=MainActivity.handler.getLatestTransactions(maxGraphItem,1000);
        graphPoints = new LineGraphSeries<DataPoint>();

        /*for(; !c.isAfterLast(); c.moveToNext()) {
            DataPoint point=new DataPoint(c.getLong(5), c.getInt(3)>0? c.getInt(2)*-1:c.getInt(2));
            graphPoints.appendData(point,true, maxGraphItem);

        }*/

        for(int i=0;i<maxGraphItem;i++) {
            Random rnd= new Random();
            boolean b=rnd.nextBoolean();
            DataPoint point=new DataPoint(i*2,b?rnd.nextInt(1000):rnd.nextInt(1000)*-1);
            graphPoints.appendData(point,true, maxGraphItem);

        }

        graphview.addSeries(graphPoints);
        graphview.setTitle("Balance chart");
       /* StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphview);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"old", "middle", "new"});
        graphview.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);*/

        graphview.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graphview.getContext()));
        graphview.getGridLabelRenderer().setNumHorizontalLabels(3);


        /*Date d1 = Calendar.getInstance().getTime();
        Date d2 = Calendar.getInstance().getTime();

        graphview.getViewport().setMinX(d1.getTime());
        graphview.getViewport().setMaxX(d2.getTime());*/
        graphview.getViewport().setXAxisBoundsManual(true);

        graphview.getGridLabelRenderer().setHumanRounding(false);

    }

    @Override
    public void NewTransactionAdded(Transaction transaction) {
        //TODO új tranzakció hozzáadásakor jelenjen meg a grafikonon.
        DataPoint point= new DataPoint(transaction.getDate(), transaction.isIncome()?transaction.getValue():transaction.getValue()*-1);
        graphview.getViewport().setMaxX(transaction.getDate());
        graphPoints.appendData(point,true,maxGraphItem);
    }

}
