package teamlld.nik.uniobuda.hu.walletapp;

import android.database.Cursor;
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
 * Created by GERGO on 2017.04.08..
 */

public class LatestItemsFragment extends Fragment {

    View rootView;

    public static LatestItemsFragment newInstance() {

        Bundle args = new Bundle();
        LatestItemsFragment fragment = new LatestItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_latestitems, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Random rand = new Random();

        List<Transaction> items = new ArrayList<>();


        MainActivity.handler.insertUser("Jani", 1000);
        for (int i = 0; i < 5; i++) {
            MainActivity.handler.insertTransaction("Tranzakció " + i, rand.nextInt(50000), rand.nextBoolean(), "típus(szórakozás,étel)", 0);
        }

        Cursor cursor = MainActivity.handler.getAllTransactions();
        while (!cursor.isAfterLast()) {
            boolean income = cursor.getInt(cursor.getColumnIndex("income")) == 0 ? false : true;
            items.add(new Transaction(cursor.getString(cursor.getColumnIndex("name")), cursor.getInt(cursor.getColumnIndex("value")), income, cursor.getString(cursor.getColumnIndex("type"))));

            cursor.moveToNext();
        }


        TransactionAdapter adapter = new TransactionAdapter(items);
        ListView list = (ListView) rootView.findViewById(R.id.transactions_list);
        list.setAdapter(adapter);

    }
}
