package teamlld.nik.uniobuda.hu.walletapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import teamlld.nik.uniobuda.hu.walletapp.NewTransactionListener;
import teamlld.nik.uniobuda.hu.walletapp.R;
import teamlld.nik.uniobuda.hu.walletapp.data.DatabaseHandler;
import teamlld.nik.uniobuda.hu.walletapp.models.Transaction;

/**
 * Created by Atee on 2017. 03. 11..
 */

public class LatestTransactionsAdapter extends BaseAdapter implements NewTransactionListener {

    private List<Transaction> items;
    private int maxItems;
    DatabaseHandler database;

    public LatestTransactionsAdapter(List<Transaction> items, int maxItems, Context context) {
        this.items = items;
        this.maxItems = maxItems;
        database=DatabaseHandler.getInstance(context);
        database.addListener(this);
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Transaction getItem(int position) {
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

        String elojel = transaction.getValue() > 0 ? "+" : "";
        if (transaction.getValue() > 0) {
            amountTextView.setTextColor(Color.GREEN);
        }
        else {
            amountTextView.setTextColor(Color.RED);
        }
        amountTextView.setText(elojel + Integer.toString(transaction.getValue()) + " HUF");

        return listItemView;
    }

    @Override
    public void NewTransactionAdded(Transaction transaction) {
        //új tranzakció hozzáadásakor betesszük a 0. helyre (legfelülre) és ha több van már benn, mint kéne, akkor a legrégebbit kivesszük.
        items.add(0,transaction);
        if (items.size() > maxItems && items.get(maxItems) != null)
        {
            items.remove(maxItems);
        }
        notifyDataSetInvalidated();
    }
}
