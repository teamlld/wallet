package teamlld.nik.uniobuda.hu.walletapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class AllTransactionsActivity extends BaseActivity {

    private final int ORDER_TYPE_DATE_DESC = 0;
    private final int ORDER_TYPE_DATE_ASC = 1;
    private final int ORDER_TYPE_VALUE_DESC = 2;
    private final int ORDER_TYPE_VALUE_ASC = 3;

    private int latestOrderType;
    private int userId;
    AllTransactionsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_transactions);

        userId = getIntent().getExtras().getInt("userid");
        latestOrderType = 0;

        InitAdapter();

        Spinner spinner = (Spinner) findViewById(R.id.transaction_order_by_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
              R.array.transaction_orderby_types, android.R.layout.simple_spinner_item);
        //TODO simple_spinner_item lecserélése sajátra, ahol nagyobb a betű méret
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case ORDER_TYPE_DATE_DESC:
                        if (latestOrderType == ORDER_TYPE_DATE_ASC) // és radibutton nem változott akkor...
                        {
                            adapter.ReverseItems();
                        }
                        else if (latestOrderType != ORDER_TYPE_DATE_DESC){
                            RefreshAdapter(BaseActivity.database.getAllTransactionsOrderByDate(userId,true));
                        }
                        latestOrderType = position;
                        break;
                    case ORDER_TYPE_DATE_ASC:
                        if (latestOrderType == ORDER_TYPE_DATE_DESC)
                        {
                            adapter.ReverseItems();
                        }
                        else if (latestOrderType != ORDER_TYPE_DATE_ASC){
                            RefreshAdapter(BaseActivity.database.getAllTransactionsOrderByDate(userId,false));
                        }
                        latestOrderType = position;
                        break;
                    case ORDER_TYPE_VALUE_DESC:
                        if (latestOrderType == ORDER_TYPE_VALUE_ASC)
                        {
                            adapter.ReverseItems();
                        }
                        else if (latestOrderType != ORDER_TYPE_VALUE_DESC){
                            RefreshAdapter(BaseActivity.database.getAllTransactionsOrderByValue(userId,true));
                        }
                        latestOrderType = position;
                        break;
                    case ORDER_TYPE_VALUE_ASC:
                        if (latestOrderType == ORDER_TYPE_VALUE_DESC)
                        {
                            adapter.ReverseItems();
                        }
                        else if (latestOrderType != ORDER_TYPE_VALUE_ASC){
                            RefreshAdapter(BaseActivity.database.getAllTransactionsOrderByValue(userId,false));
                        }
                        latestOrderType = position;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    private  void InitAdapter()
    {
        Cursor cursor = BaseActivity.database.getAllTransactionsOrderByDate(userId,true);

        List<Transaction> items = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            boolean income = cursor.getInt(cursor.getColumnIndex("income")) == 0 ? false : true;
            items.add(new Transaction(cursor.getString(cursor.getColumnIndex("name")), cursor.getInt(cursor.getColumnIndex("value")), income, cursor.getInt(cursor.getColumnIndex("_typeId")),cursor.getLong(cursor.getColumnIndex("date"))));

            cursor.moveToNext();
        }

        adapter = new AllTransactionsAdapter(items);
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

    private void RefreshAdapter(Cursor cursor)
    {
        List<Transaction> items = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            boolean income = cursor.getInt(cursor.getColumnIndex("income")) == 0 ? false : true;
            items.add(new Transaction(cursor.getString(cursor.getColumnIndex("name")), cursor.getInt(cursor.getColumnIndex("value")), income, cursor.getInt(cursor.getColumnIndex("_typeId")),cursor.getLong(cursor.getColumnIndex("date"))));

            cursor.moveToNext();
        }

        adapter.setItems(items);
    }
}
