package teamlld.nik.uniobuda.hu.walletapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GERGO on 2017.04.08..
 */

public class LatestItemsFragment extends Fragment {

    View rootView;

    public static LatestItemsFragment newInstance(int userId) {

        Bundle args = new Bundle();
        args.putInt("userid",userId);
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

        List<Transaction> items = new ArrayList<>();

        int maxItems = 5;
        Cursor cursor = BaseActivity.database.getLatestTransactions(maxItems,getArguments().getInt("userid"));
        while (!cursor.isAfterLast()) {
            boolean income = cursor.getInt(cursor.getColumnIndex("income")) == 0 ? false : true;
            items.add(new Transaction(cursor.getString(cursor.getColumnIndex("name")), cursor.getInt(cursor.getColumnIndex("value")), income, cursor.getInt(cursor.getColumnIndex("_typeId")),cursor.getLong(cursor.getColumnIndex("date"))));

            cursor.moveToNext();
        }

        final LatestTransactionsAdapter adapter = new LatestTransactionsAdapter(items,maxItems);
        ListView list = (ListView) rootView.findViewById(R.id.transactions_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Transaction selectedTransaction = adapter.getItem(position);

                Intent intent = new Intent(getActivity(),DetailsActivity.class);
                intent.putExtra("selected_transaction", selectedTransaction);
                startActivity(intent);
            }
        });

    }
}
