package teamlld.nik.uniobuda.hu.walletapp;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Atee on 2017. 03. 11..
 */

public class TransactionAdapter extends BaseAdapter {

    private List<Transaction> items;

    public TransactionAdapter(List<Transaction> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Object getItem(int position) {
        return items == null ? null : items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null)
            listItemView = View.inflate(parent.getContext(), R.layout.listitem_transactions, null);

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.transaction_name);
        TextView amountTextView = (TextView) listItemView.findViewById(R.id.transaction_amount);

        Transaction transaction = items.get(position);
        nameTextView.setText(transaction.getName());

        //A számok színét is át kéne állítani valahogy piros\zöld-re
        String elojel = transaction.isIncome() ? "+" : "-";
        amountTextView.setText(elojel + Integer.toString(transaction.getAmount()) + " " + transaction.getCurrency());

        return  listItemView;
    }
}
