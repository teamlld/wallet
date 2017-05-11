package teamlld.nik.uniobuda.hu.walletapp.fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import teamlld.nik.uniobuda.hu.walletapp.NewTransactionListener;
import teamlld.nik.uniobuda.hu.walletapp.R;
import teamlld.nik.uniobuda.hu.walletapp.data.DatabaseHandler;
import teamlld.nik.uniobuda.hu.walletapp.models.Transaction;
import teamlld.nik.uniobuda.hu.walletapp.models.User;

/**
 * Created by GERGO on 2017.04.08..
 */

public class BalanceDiagramFragment extends Fragment implements NewTransactionListener {

    View rootView;
    int maxGraphItem=20;
    LineChart chart;
    User user;
    DatabaseHandler database;

    public static BalanceDiagramFragment newInstance(User user) {

        Bundle args = new Bundle();
        args.putParcelable("user",user);
        BalanceDiagramFragment fragment = new BalanceDiagramFragment();
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

        database=DatabaseHandler.getInstance(getContext());
        database.addListener(this);

        user = getArguments().getParcelable("user");

        chart=(LineChart)getView().findViewById(R.id.balanceChart);


        LineDataSet dataset=new LineDataSet(getDataPoints(),"balance");
        LineData data=new LineData(dataset);
        chart.setData(data);

        dataset.setCircleColor(Color.parseColor("#800000"));
        //dataset.setCircleColorHole(Color.parseColor("#800000"));
        dataset.setCircleRadius(5f);
        dataset.setColor(Color.parseColor("#ff3300"));
        dataset.setLineWidth(3f);

        setGraphProperties();

    }
    void setGraphProperties()
    {

        XAxis x=chart.getXAxis();
        x.setTextSize(12f);
        x.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");

                return sdf.format(new Date((long)value));
            }
        });

        YAxis yl=chart.getAxisLeft();
        YAxis yr=chart.getAxisRight();
        yl.setTextSize(12f);
        yr.setTextSize(12f);

        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        Description desc=new Description();
        desc.setText("");
        chart.setDescription(desc);
        chart.getLegend().setEnabled(false);

        chart.animateY(500);
        chart.setExtraBottomOffset(10f);
    }

    List<Entry> getDataPoints()
    {
        Cursor c = database.getAllTransactionsOrderByDate(user.getId(),false);
        List<Entry> result=new ArrayList<>();

        if (c.getCount() > 0)
        {
            //user balance alap értékétől induljon a grafikon
            float yvalue = c.getFloat(c.getColumnIndex("value"))+user.getBalance();
            float date=c.getFloat(c.getColumnIndex("date"));
            result.add(new Entry(date, yvalue));
            c.moveToNext();


            for(int i=1;i<c.getCount() && !c.isAfterLast();i++)
            {
                // meg kell nézni az előző értékhez képest és úgy beállítani az értéket
                float d=result.get(i-1).getY();
                float cur = c.getFloat(c.getColumnIndex("value"));
                yvalue=d+ cur;
                date=c.getFloat(c.getColumnIndex("date"));
                result.add(new Entry(date, yvalue));
                c.moveToNext();
            }
        }
        return  result;
    }


    @Override
    public void NewTransactionAdded(Transaction transaction) {
        chart.invalidate();
    }

}
