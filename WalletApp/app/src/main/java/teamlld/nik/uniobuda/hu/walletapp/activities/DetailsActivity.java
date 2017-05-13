package teamlld.nik.uniobuda.hu.walletapp.activities;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import teamlld.nik.uniobuda.hu.walletapp.R;
import teamlld.nik.uniobuda.hu.walletapp.models.Transaction;

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
                    valueTextView.setText(String.valueOf(selectedTransaction.getValue()) + getResources().getString(R.string.huf));
                    if (selectedTransaction.getValue() > 0)
                    {
                        incomeTextView.setText(getResources().getString(R.string.income));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            incomeTextView.setTextColor(getResources().getColor(R.color.colorGreen,null));
                        }
                        else {
                            incomeTextView.setTextColor(getResources().getColor(R.color.colorGreen));
                        }
                    }
                    else
                    {
                        incomeTextView.setText(getResources().getString(R.string.expense));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            incomeTextView.setTextColor(getResources().getColor(R.color.colorRed,null));
                        }
                        else {
                            incomeTextView.setTextColor(getResources().getColor(R.color.colorRed));
                        }
                    }

                    Cursor cursor = database.getTypeById(selectedTransaction.getTypeId());
                    Log.d("typeid",Integer.toString(selectedTransaction.getTypeId()) );
                    if (cursor.getCount() > 0)
                    {
                        typeTextView.setText(cursor.getString(cursor.getColumnIndex("name")));
                    }
                    else {
                        typeTextView.setText(getResources().getString(R.string.unknown_type));
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                    String date= sdf.format(new Date(selectedTransaction.getDate()));
                    dateTextView.setText(date);
                }
            }
        }
    }
}
