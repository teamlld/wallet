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
        final int userId = getIntent().getExtras().getInt("userid");


        addNewTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO type lehetne enum és a felületen dropdownlist
                if(nameEditText.getText().length()>0 && valueEditText.getText().length()>0) {
                    MainActivity.handler.insertTransaction(
                            nameEditText.getText().toString(),
                            Integer.parseInt(valueEditText.getText().toString()),
                            incomeCheckBox.isChecked(),
                            typeEditText.getText().toString(),
                            MainActivity.handler.currentTimeToLong(),
                            userId
                    );

                    finish();
                }
                //TODO exception dobás?
            }
        });
    }
}
