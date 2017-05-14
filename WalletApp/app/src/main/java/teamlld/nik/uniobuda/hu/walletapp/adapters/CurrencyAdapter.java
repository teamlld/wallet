package teamlld.nik.uniobuda.hu.walletapp.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import teamlld.nik.uniobuda.hu.walletapp.R;
import teamlld.nik.uniobuda.hu.walletapp.activities.CurrencySelectorActivity;
import teamlld.nik.uniobuda.hu.walletapp.models.Currency;

/**
 * Created by admin on 2017. 05. 01..
 */

public class CurrencyAdapter extends BaseAdapter {

    private List<Currency> items;
    public static int mSelectedCurrency;

    public CurrencyAdapter(List<Currency> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Currency getItem(int position) {
        return items == null ? null : items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null){
            view = View.inflate(parent.getContext(), R.layout.listitem_currency, null);
        }

        Currency currency = items.get(position);

        //TextView name = (TextView)view.findViewById(R.id.name);
        RadioButton radio = (RadioButton)view.findViewById(R.id.radio);
        radio.setText(currency.getName());

        //name.setText(currency.getName());
        if (position == CurrencySelectorActivity.selectedFrom) radio.setChecked(true);
        else radio.setChecked(false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrencySelectorActivity.selectedFrom = position;
                CurrencyAdapter.this.notifyDataSetChanged();
            }
        });

        return view;
    }
}
