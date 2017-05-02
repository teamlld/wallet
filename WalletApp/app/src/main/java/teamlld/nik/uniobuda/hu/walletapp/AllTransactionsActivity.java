package teamlld.nik.uniobuda.hu.walletapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class AllTransactionsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_transactions);

        List<Transaction> items = new ArrayList<>();

        Cursor cursor = BaseActivity.database.getAllTransactions(1000,true);

        while (!cursor.isAfterLast()) {
            boolean income = cursor.getInt(cursor.getColumnIndex("income")) == 0 ? false : true;
            items.add(new Transaction(cursor.getString(cursor.getColumnIndex("name")), cursor.getInt(cursor.getColumnIndex("value")), income, cursor.getInt(cursor.getColumnIndex("_typeId")),cursor.getLong(cursor.getColumnIndex("date"))));

            cursor.moveToNext();
        }

        final AllTransactionsAdapter adapter = new AllTransactionsAdapter(items);
        ListView list = (ListView) findViewById(R.id.all_transactions_list_view);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Transaction selectedTransaction = adapter.getItem(position);

                Intent intent = new Intent(AllTransactionsActivity.this,DetailsActivity.class);
                intent.putExtra("selected_transaction", selectedTransaction);
                startActivity(intent);
            }
        });
    }
}
