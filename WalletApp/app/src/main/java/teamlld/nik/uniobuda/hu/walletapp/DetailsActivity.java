package teamlld.nik.uniobuda.hu.walletapp;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import teamlld.nik.uniobuda.hu.walletapp.BaseActivity;
import teamlld.nik.uniobuda.hu.walletapp.R;
import teamlld.nik.uniobuda.hu.walletapp.Transaction;

public class DetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView nameTextView = (TextView) findViewById(R.id.details_name);
        TextView valueTextView = (TextView) findViewById(R.id.details_value);
        TextView incomeTextView = (TextView) findViewById(R.id.details_income);
        TextView typeTextView = (TextView) findViewById(R.id.details_type);
        TextView dateTextView = (TextView) findViewById(R.id.details_date);

        if (getIntent().getExtras() != null){
            Bundle args = getIntent().getExtras();
            if (args.containsKey("selected_transaction")){

                Transaction selectedTransaction = args.getParcelable("selected_transaction");

                if (selectedTransaction != null){
                    nameTextView.setText(selectedTransaction.getName());
                    valueTextView.setText(Integer.toString(selectedTransaction.getValue()));
                    if (selectedTransaction.getValue() > 0)
                    {
                        incomeTextView.setText("Bevétel");
                    }
                    else
                    {
                        incomeTextView.setText("Kiadás");
                    }

                    Cursor cursor = BaseActivity.database.getTypeById(selectedTransaction.getTypeId());
                    Log.d("typeid",Integer.toString(selectedTransaction.getTypeId()) );
                    if (cursor.getCount() > 0)
                    {
                        typeTextView.setText(cursor.getString(cursor.getColumnIndex("name")));
                    }
                    else {
                        typeTextView.setText("Ismeretlen típus");
                    }

                    dateTextView.setText(Integer.toString((int)selectedTransaction.getDate()));
                }
            }
        }
    }
}
