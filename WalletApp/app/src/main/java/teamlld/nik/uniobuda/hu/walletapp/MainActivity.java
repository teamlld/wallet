package teamlld.nik.uniobuda.hu.walletapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Random rand = new Random();

        List<Transaction> items = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            items.add(new Transaction("Tranzakció " + i, 5000 + rand.nextInt(5000), "HUF", rand.nextBoolean()));
        }

        TransactionAdapter adapter = new TransactionAdapter(items);
        ListView lista = (ListView) findViewById(R.id.transactions_list);
        lista.setAdapter(adapter);


    }
}
