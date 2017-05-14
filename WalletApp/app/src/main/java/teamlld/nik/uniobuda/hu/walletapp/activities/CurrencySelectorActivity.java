package teamlld.nik.uniobuda.hu.walletapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import teamlld.nik.uniobuda.hu.walletapp.JsonDownloader;
import teamlld.nik.uniobuda.hu.walletapp.R;
import teamlld.nik.uniobuda.hu.walletapp.adapters.CurrencyAdapter;
import teamlld.nik.uniobuda.hu.walletapp.models.Currency;

/**
 * Created by admin on 2017. 05. 01..
 */

public class CurrencySelectorActivity extends BaseActivity {

    List<Currency> items = new ArrayList<>();
    public static int selectedFrom;
    int toReturnWith;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currencyselector);
        toReturnWith = getIntent().getExtras().getInt("message", 0);
        startDownload();
        Button button = (Button) findViewById(R.id.asdasd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("name", items.get(selectedFrom).getName());
                intent.putExtra("value", items.get(selectedFrom).getValue());
                setResult(toReturnWith, intent);
                finish();
            }
        });

    }

    private void startDownload() {
        JsonDownloader downloader = new JsonDownloader("http://apilayer.net/api/live?access_key=2d5654ddecf45f8f62c965dfa775d069");
        downloader.setOnResultListener(new JsonDownloader.ResultListener() {
            @Override
            public void onSuccess(String text) {
                String[] all = text.split("\\{");
                String[] currencies = all[2].split(",");
                for (int i = 0; i < currencies.length; i++) {
                    String[] values = currencies[i].split(":");
                    items.add(new Currency(values[0].substring(4, 7).toString(), Double.parseDouble(values[1].replaceAll("[\\}]", ""))));
                }
                CurrencyAdapter adapter = new CurrencyAdapter(items);
                ListView listView = (ListView) findViewById(R.id.listview);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailed(String message) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG);
            }
        });
        downloader.start();
    }

}
