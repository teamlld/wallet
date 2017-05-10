package teamlld.nik.uniobuda.hu.walletapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import teamlld.nik.uniobuda.hu.walletapp.R;
import teamlld.nik.uniobuda.hu.walletapp.adapters.AllTransactionsAdapter;
import teamlld.nik.uniobuda.hu.walletapp.models.Transaction;

public class AllTransactionsActivity extends BaseActivity {

    private final int ORDER_TYPE_DATE_DESC = 0;
    private final int ORDER_TYPE_DATE_ASC = 1;
    private final int ORDER_TYPE_VALUE_DESC = 2;
    private final int ORDER_TYPE_VALUE_ASC = 3;
    private final int FILTER_TYPE_INCOME = 0;
    private final int FILTER_TYPE_EXPENSE = 1;
    private final int FILTER_TYPE_BOTH = 2;

    private int latestOrderType;
    private int latestFilterType;
    private int userId;
    AllTransactionsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_transactions);

        userId = getIntent().getExtras().getInt("userid");
        latestOrderType = 0;
        latestFilterType = 2;

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
                AllTransactionsActivity.this.SetOrder(latestFilterType,position);
                latestOrderType = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.all_transactions_radio_income:
                if (checked){
                    SetOrder(FILTER_TYPE_INCOME,latestOrderType);
                    latestFilterType = FILTER_TYPE_INCOME;
                }
                break;
            case R.id.all_transactions_radio_expenditure:
                if (checked){
                    SetOrder(FILTER_TYPE_EXPENSE,latestOrderType);
                    latestFilterType = FILTER_TYPE_EXPENSE;
                }
                break;
            case R.id.all_transactions_radio_both:
                if (checked){
                    SetOrder(FILTER_TYPE_BOTH,latestOrderType);
                    latestFilterType = FILTER_TYPE_BOTH;
                }
                break;
        }
    }


    public void SetOrder(int filterType, int orderType)
    {
        if (filterType != latestFilterType) //ha a radio buttont klikkeltük át, akkor fut le, ilyenkor mindenképp újból lekérjük a tranzakciókat
        {
            boolean income;
            switch (filterType){ // 4 állapotú a spinner, 3 a radibogroup, így 12 lehetséges esetet kell megvizsgálni, és ez alapján lekérni az adatbázistól a tranzakciókat.
                case FILTER_TYPE_BOTH:
                    switch (orderType){
                        case ORDER_TYPE_DATE_DESC:
                            RefreshAdapter(database.getAllTransactionsOrderByDate(userId,true));
                            break;
                        case ORDER_TYPE_DATE_ASC:
                            RefreshAdapter(database.getAllTransactionsOrderByDate(userId,false));
                            break;
                        case ORDER_TYPE_VALUE_DESC:
                            RefreshAdapter(database.getAllTransactionsOrderByValue(userId,true));
                            break;
                        case ORDER_TYPE_VALUE_ASC:
                            RefreshAdapter(database.getAllTransactionsOrderByValue(userId,false));
                            break;
                    }
                    break;
                case FILTER_TYPE_INCOME:
                    income = true;
                    switch (orderType){
                        case ORDER_TYPE_DATE_DESC:
                            RefreshAdapter(database.getTransactionsOrderByDate(userId,true,income));
                            break;
                        case ORDER_TYPE_DATE_ASC:
                            RefreshAdapter(database.getTransactionsOrderByDate(userId,false,income));
                            break;
                        case ORDER_TYPE_VALUE_DESC:
                            RefreshAdapter(database.getTransactionsOrderByValue(userId,true,income));
                            break;
                        case ORDER_TYPE_VALUE_ASC:
                            RefreshAdapter(database.getTransactionsOrderByValue(userId,false,income));
                            break;
                    }
                    break;
                case FILTER_TYPE_EXPENSE:
                    income = false;
                    switch (orderType){
                        case ORDER_TYPE_DATE_DESC:
                            RefreshAdapter(database.getTransactionsOrderByDate(userId,true,income));
                            break;
                        case ORDER_TYPE_DATE_ASC:
                            RefreshAdapter(database.getTransactionsOrderByDate(userId,false,income));
                            break;
                        case ORDER_TYPE_VALUE_DESC:
                            RefreshAdapter(database.getTransactionsOrderByValue(userId,true,income));
                            break;
                        case ORDER_TYPE_VALUE_ASC:
                            RefreshAdapter(database.getTransactionsOrderByValue(userId,false,income));
                            break;
                    }
                    break;
            }

        }
        else if (latestOrderType != orderType){  //ha a spinnerben választottunk más értéket akkor fut le, itt lehet kicsit optimalizálni.
            boolean income;
            switch (filterType) {
                case FILTER_TYPE_BOTH:
                    switch (orderType){
                        case ORDER_TYPE_DATE_DESC:
                            if (latestOrderType == ORDER_TYPE_DATE_ASC) // ha csak a sorrendet fordítjuk meg, nem kell új kérés, egyszerűen megfordítjuk az elemek sorrendjét
                            {
                                adapter.ReverseItems();
                            }
                            else {
                                RefreshAdapter(database.getAllTransactionsOrderByDate(userId,true));
                            }
                            break;
                        case ORDER_TYPE_DATE_ASC:
                            if (latestOrderType == ORDER_TYPE_DATE_DESC)
                            {
                                adapter.ReverseItems();
                            }
                            else {
                                RefreshAdapter(database.getAllTransactionsOrderByDate(userId,false));
                            }
                            break;
                        case ORDER_TYPE_VALUE_DESC:
                            if (latestOrderType == ORDER_TYPE_VALUE_ASC)
                            {
                                adapter.ReverseItems();
                            }
                            else {
                                RefreshAdapter(database.getAllTransactionsOrderByValue(userId,true));
                            }
                            break;
                        case ORDER_TYPE_VALUE_ASC:
                            if (latestOrderType == ORDER_TYPE_VALUE_DESC)
                            {
                                adapter.ReverseItems();
                            }
                            else {
                                RefreshAdapter(database.getAllTransactionsOrderByValue(userId,false));
                            }
                            break;
                    }
                    break;
                case FILTER_TYPE_INCOME:
                    income = true;
                    switch (orderType){
                        case ORDER_TYPE_DATE_DESC:
                            if (latestOrderType == ORDER_TYPE_DATE_ASC)
                            {
                                adapter.ReverseItems();
                            }
                            else {
                                RefreshAdapter(database.getTransactionsOrderByDate(userId,true,income));
                            }
                            break;
                        case ORDER_TYPE_DATE_ASC:
                            if (latestOrderType == ORDER_TYPE_DATE_DESC)
                            {
                                adapter.ReverseItems();
                            }
                            else {
                                RefreshAdapter(database.getTransactionsOrderByDate(userId,false,income));
                            }
                            break;
                        case ORDER_TYPE_VALUE_DESC:
                            if (latestOrderType == ORDER_TYPE_VALUE_ASC)
                            {
                                adapter.ReverseItems();
                            }
                            else {
                                RefreshAdapter(database.getTransactionsOrderByValue(userId,true,income));
                            }
                            break;
                        case ORDER_TYPE_VALUE_ASC:
                            if (latestOrderType == ORDER_TYPE_VALUE_DESC)
                            {
                                adapter.ReverseItems();
                            }
                            else {
                                RefreshAdapter(database.getTransactionsOrderByValue(userId,false,income));
                            }
                            break;
                    }
                    break;
                case FILTER_TYPE_EXPENSE:
                    income = false;
                    switch (orderType){
                        case ORDER_TYPE_DATE_DESC:
                            if (latestOrderType == ORDER_TYPE_DATE_ASC)
                            {
                                adapter.ReverseItems();
                            }
                            else {
                                RefreshAdapter(database.getTransactionsOrderByDate(userId,true,income));
                            }
                            break;
                        case ORDER_TYPE_DATE_ASC:
                            if (latestOrderType == ORDER_TYPE_DATE_DESC)
                            {
                                adapter.ReverseItems();
                            }
                            else {
                                RefreshAdapter(database.getTransactionsOrderByDate(userId,false,income));
                            }
                            break;
                        case ORDER_TYPE_VALUE_DESC:
                            if (latestOrderType == ORDER_TYPE_VALUE_ASC)
                            {
                                adapter.ReverseItems();
                            }
                            else {
                                RefreshAdapter(database.getTransactionsOrderByValue(userId,true,income));
                            }
                            break;
                        case ORDER_TYPE_VALUE_ASC:
                            if (latestOrderType == ORDER_TYPE_VALUE_DESC)
                            {
                                adapter.ReverseItems();
                            }
                            else {
                                RefreshAdapter(database.getTransactionsOrderByValue(userId,false,income));
                            }
                            break;
                    }
                    break;
            }
        }
    }


    private  void InitAdapter()
    {
        Cursor cursor = database.getAllTransactionsOrderByDate(userId,true);

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
