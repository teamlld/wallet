package teamlld.nik.uniobuda.hu.walletapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView nameTextView = (TextView) findViewById(R.id.details_name);
        TextView valueTextView = (TextView) findViewById(R.id.details_value);
        TextView incomeTextView = (TextView) findViewById(R.id.details_income);
        TextView typeTextView = (TextView) findViewById(R.id.details_type);

        if (getIntent().getExtras() != null){
            Bundle args = getIntent().getExtras();
            if (args.containsKey("selected_transaction")){

                Transaction selectedTransaction = args.getParcelable("selected_transaction");

                if (selectedTransaction != null){
                    nameTextView.setText(selectedTransaction.getName());
                    valueTextView.setText(Integer.toString(selectedTransaction.getValue()));
                    if (selectedTransaction.isIncome())
                    {
                        incomeTextView.setText("Bevétel");
                    }
                    else
                    {
                        incomeTextView.setText("Kiadás");
                    }
                    typeTextView.setText(selectedTransaction.getType());
                }
            }
        }
    }
}
