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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import teamlld.nik.uniobuda.hu.walletapp.NewTransactionListener;
import teamlld.nik.uniobuda.hu.walletapp.R;
import teamlld.nik.uniobuda.hu.walletapp.data.DatabaseHandler;
import teamlld.nik.uniobuda.hu.walletapp.models.Transaction;
import teamlld.nik.uniobuda.hu.walletapp.models.User;

/**
 * Created by GERGO on 2017.04.08..
 */

public class BalanceDiagramFragment extends Fragment implements NewTransactionListener {

    private View rootView;
    private int maxGraphItem=20;
    private LineChart chart;
    private LineData data;
    private LineDataSet dataset;
    private User user;
    private DatabaseHandler database;

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

        initializeDataAndDataSet();
    }

    private void initializeDataAndDataSet()
    {
        dataset=new LineDataSet(getDataPoints(),"balance");
        data=new LineData(dataset);
        chart.setData(data);

        setGraphProperties();
    }

    private void setGraphProperties()
    {
        XAxis x=chart.getXAxis();
        x.setTextSize(12f);
        x.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd", Locale.getDefault());

                return sdf.format(new Date((long)value));
            }
        });

        YAxis yl=chart.getAxisLeft();
        YAxis yr=chart.getAxisRight();
        yl.setTextSize(12f);
        yr.setTextSize(12f);
        yl.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                DecimalFormat df=new DecimalFormat("###,###,###");
                return df.format(value)+"";
            }
        });

        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        Description desc=new Description();
        desc.setText("");
        chart.setDescription(desc);
        chart.getLegend().setEnabled(false);
        chart.animateY(500);
        chart.setExtraBottomOffset(10f);
        chart.setBackgroundColor(Color.rgb(245,245, 245));

        dataset.setCircleColor(Color.parseColor("#008ae6"));
        dataset.setCircleColorHole(Color.parseColor("#ffffff"));
        dataset.setCircleRadius(7f);
        dataset.setCircleHoleRadius(4f);
        dataset.setColor(Color.parseColor("#008ae6"));
        dataset.setLineWidth(4f);
        dataset.setDrawValues(true);
    }

    private List<Entry> getDataPoints()
    {
        Cursor c = database.getLatestTransactions(maxGraphItem,user.getId(),false);
        List<Entry> result=new ArrayList<>();

        if(c.getCount()==0)
        {
            result.add(new Entry(0,0));
            return result;
        }
        if (c.getCount() > 0)
        {
            //user balance alap értékétől induljon a grafikon
            float yvalue = c.getFloat(c.getColumnIndex("value"))+user.getStartingBalance();
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

        initializeDataAndDataSet();
        data.notifyDataChanged();
        //chart.setVisibleXRangeMaximum(transaction.getDate());
        chart.fitScreen();
        chart.invalidate();
    }

}
