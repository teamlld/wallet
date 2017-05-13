package teamlld.nik.uniobuda.hu.walletapp.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import java.util.Date;

import teamlld.nik.uniobuda.hu.walletapp.R;

public class NewTransactionActivity extends BaseActivity {

    private int selectedId;
    private boolean isIncome;
    private  Spinner spinner;
    private Cursor cursor_income;
    private Cursor cursor_expense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);

        spinner = (Spinner) findViewById(R.id.newTransactionType);

        cursor_income = database.getTypes(true);
        cursor_expense = database.getTypes(false);
        SetSpinnerValues(false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setIdByIsIncomeFlag(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button addNewTransactionButton = (Button) findViewById(R.id.addNewTransactionButton);
        final EditText nameEditText = (EditText) findViewById(R.id.newTransactionName);
        final EditText valueEditText = (EditText) findViewById(R.id.newTransactionValue);
        //final RadioGroup incomeRadioGroup=(RadioGroup)findViewById(R.id.radioGroup_newTransaction);

        final int userId = getIntent().getExtras().getInt("userid");

        addNewTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nameEditText.getText().length()>0 && valueEditText.getText().length()>0) {

                    Date now=new Date();
                    database.insertTransaction(
                            nameEditText.getText().toString(),
                            isIncome ? Integer.parseInt(valueEditText.getText().toString()) : Integer.parseInt(valueEditText.getText().toString()) * (-1),
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

    private void setIdByIsIncomeFlag(int position)
    {
        //elöl vannak a bevételek
        if(!isIncome)
        {
            int l=getResources().getStringArray(R.array.types_income).length;
            selectedId=l+position+1;
        }
        else
        {
            selectedId=position+1;
        }
    }

    public void onRadioButtonClicked(View view) {
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
