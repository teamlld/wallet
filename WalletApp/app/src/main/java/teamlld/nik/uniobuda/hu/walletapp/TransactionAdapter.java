package teamlld.nik.uniobuda.hu.walletapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Atee on 2017. 03. 11..
 */

public class TransactionAdapter extends BaseAdapter implements NewTransactionListener {

    private List<Transaction> items;
    private int maxItems;

    public TransactionAdapter(List<Transaction> items, int maxItems) {
        this.items = items;
        this.maxItems = maxItems;
        BaseActivity.database.addListener(this);
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

        String elojel = transaction.isIncome() ? "+" : "-";
        if (transaction.isIncome()) {
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
