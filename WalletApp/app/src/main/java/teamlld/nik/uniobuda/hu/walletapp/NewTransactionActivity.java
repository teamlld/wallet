package teamlld.nik.uniobuda.hu.walletapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.Date;

public class NewTransactionActivity extends AppCompatActivity{

    String selectedSpinnerValue;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);

        spinner = (Spinner) findViewById(R.id.newTransactionType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSpinnerValue = spinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button addNewTransactionButton = (Button) findViewById(R.id.addNewTransactionButton);
        final EditText nameEditText = (EditText) findViewById(R.id.newTransactionName);
        final EditText valueEditText = (EditText) findViewById(R.id.newTransactionValue);
       // final EditText typeEditText = (EditText) findViewById(R.id.newTransactionType);


        final CheckBox incomeCheckBox = (CheckBox) findViewById(R.id.newTransactionIncome);
        final int userId = getIntent().getExtras().getInt("userid");


        addNewTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nameEditText.getText().length()>0 && valueEditText.getText().length()>0) {

                    Date now=new Date();
                    MainActivity.handler.insertTransaction(
                            nameEditText.getText().toString(),
                            Integer.parseInt(valueEditText.getText().toString()),
                            incomeCheckBox.isChecked(),
                            //typeEditText.getText().toString(),
                            selectedSpinnerValue,
                            now.getTime(),
                            userId
                    );

                    finish();
                }
                //TODO exception dobás?
            }
        });
    }
}
