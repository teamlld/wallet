package teamlld.nik.uniobuda.hu.walletapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import teamlld.nik.uniobuda.hu.walletapp.R;
import teamlld.nik.uniobuda.hu.walletapp.adapters.CurrencyAdapter;
import teamlld.nik.uniobuda.hu.walletapp.models.Currency;

/**
 * Created by admin on 2017. 05. 01..
 */

public class CurrencySelectorActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currencyselector);

        List<Currency> items = new ArrayList<>();
        for (int i = 0; i <25; i++) {
            items.add(new Currency("asd " + String.valueOf(i), ((double) i)));
        }

        CurrencyAdapter adapter = new CurrencyAdapter(items);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
    }
}
