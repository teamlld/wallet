package teamlld.nik.uniobuda.hu.walletapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by GERGO on 2017.04.08..
 */

public class BalanceFragment extends Fragment implements NewTransactionListener {

    View rootView;

    public static BalanceFragment newInstance(User user) {

        Bundle args = new Bundle();
        args.putParcelable("user" , user);
        return newInstance(args);
    }

    public static BalanceFragment newInstance(Bundle args) {

        BalanceFragment fragment = new BalanceFragment();
        fragment.setArguments(args);
        MainActivity.handler.addListener(fragment);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_balance, container, false);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //beállítja az argumentumokban lévő user alapján a címet és az egyenleg textViewkat.
        TextView titleTextview = (TextView) rootView.findViewById(R.id.title);
        TextView balanceTextView = (TextView) rootView.findViewById(R.id.balance);

        if (getArguments() != null){
            Bundle args = getArguments();
            if (args.containsKey("user")){

                User user = args.getParcelable("user");

                if (user != null){
                    titleTextview.setText(user.getName() + " egyenlege:");
                    balanceTextView.setText(Integer.toString(user.getBalance()));
                }
            }
        }
    }

    @Override
    public void NewTransactionAdded(Transaction transaction) {
        //Ha új tranzakciót vettünk fel frissítjük a user objektum balance-át, a textView-ot, és az adatbázist.
        User currentUser = getArguments().getParcelable("user");
        int currentBalance = currentUser.getBalance();

        int newBalance = currentBalance + (transaction.isIncome() ? transaction.getValue() : transaction.getValue() * (-1));

        currentUser.setBalance(newBalance);

        TextView balanceTextView = (TextView) rootView.findViewById(R.id.balance);
        balanceTextView.setText(Integer.toString(newBalance));

        MainActivity.handler.updateUserBalance(currentUser.getId(),newBalance);
    }
}