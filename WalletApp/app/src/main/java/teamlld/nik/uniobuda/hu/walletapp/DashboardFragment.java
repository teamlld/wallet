package teamlld.nik.uniobuda.hu.walletapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by GERGO on 2017.03.26..
 */

public class DashboardFragment extends Fragment {

    View rootView;

    public static DashboardFragment newInstance(){

        Bundle args=new Bundle();
        DashboardFragment fragment= new DashboardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return  rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Random rand = new Random();

        List<Transaction> items = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            items.add(new Transaction("Tranzakció " + i, 5000 + rand.nextInt(5000), "HUF", rand.nextBoolean()));
        }

        TransactionAdapter adapter = new TransactionAdapter(items);
        ListView list = (ListView) rootView.findViewById(R.id.transactions_list);
        list.setAdapter(adapter);

    }
}
