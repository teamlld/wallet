package teamlld.nik.uniobuda.hu.walletapp;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.Date;

public class NewTransactionActivity extends BaseActivity{

    int selectedId;
    boolean isIncome;
    Spinner spinner;
    Cursor cursor_income;
    Cursor cursor_expense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);

        spinner = (Spinner) findViewById(R.id.newTransactionType);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.categories, android.R.layout.simple_spinner_item);

        cursor_income = BaseActivity.database.getTypes(true);
        cursor_expense = BaseActivity.database.getTypes(false);
        SetSpinnerValues(false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // selectedSpinnerValue = spinner.getSelectedItem().toString();
                selectedId = position + 1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button addNewTransactionButton = (Button) findViewById(R.id.addNewTransactionButton);
        final EditText nameEditText = (EditText) findViewById(R.id.newTransactionName);
        final EditText valueEditText = (EditText) findViewById(R.id.newTransactionValue);
        final RadioGroup incomeRadioGroup=(RadioGroup)findViewById(R.id.radioGroup_newTransaction);

        final int userId = getIntent().getExtras().getInt("userid");

        addNewTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nameEditText.getText().length()>0 && valueEditText.getText().length()>0) {

                    Date now=new Date();
                    BaseActivity.database.insertTransaction(
                            nameEditText.getText().toString(),
                            Integer.parseInt(valueEditText.getText().toString()),
                            isIncome,
                            selectedId,
                            now.getTime(),
                            userId
                    );

                    finish();
                }
                //TODO exception dobás?
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radio_income:
                if (checked)
                    isIncome=true;
                    SetSpinnerValues(true);
                    break;
            case R.id.radio_expenditure:
                if (checked)
                    isIncome=false;
                    SetSpinnerValues(false);
                    break;
        }
    }

    private void SetSpinnerValues(boolean income)
    {
        String[] adapterCols=new String[]{"name"};
        int[] adapterRowViews=new int[]{android.R.id.text1};

        SimpleCursorAdapter adapter;
        if (income) {
           adapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, cursor_income, adapterCols, adapterRowViews,0);
        }
        else {
            adapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, cursor_expense, adapterCols, adapterRowViews,0);
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
