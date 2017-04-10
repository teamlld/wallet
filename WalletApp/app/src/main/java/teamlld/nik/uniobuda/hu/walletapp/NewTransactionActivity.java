package teamlld.nik.uniobuda.hu.walletapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class NewTransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);

        Button addNewTransactionButton = (Button) findViewById(R.id.addNewTransactionButton);
        final EditText nameEditText = (EditText) findViewById(R.id.newTransactionName);
        final EditText valueEditText = (EditText) findViewById(R.id.newTransactionValue);
        final EditText typeEditText = (EditText) findViewById(R.id.newTransactionType);
        final CheckBox incomeCheckBox = (CheckBox) findViewById(R.id.newTransactionIncome);


        addNewTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO mielőtt beszúrúnk egy ellenőrzést kéne végezni, hogy numberbe csak number van-e, nem e üres, (vagy ezt dbhandlernél)
                //TODO ha beszúrtuk visszatérünk a dashboardra
                //TODO adatbázisba bekerül, de a listview-ban nem frissül, azt frissíteni, vagy visszaadni valahogy egy Transaction objektumot és azt belerakni a listview itemjeibe

                MainActivity.handler.insertTransaction(nameEditText.getText().toString(), Integer.parseInt(valueEditText.getText().toString()), incomeCheckBox.isChecked(), typeEditText.getText().toString(), 0);

            }
        });
    }
}
