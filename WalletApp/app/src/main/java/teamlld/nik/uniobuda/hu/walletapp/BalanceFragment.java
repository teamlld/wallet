package teamlld.nik.uniobuda.hu.walletapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by GERGO on 2017.04.08..
 */

public class BalanceFragment extends Fragment {

    View rootView;

    public static BalanceFragment newInstance() {

        Bundle args = new Bundle();
        BalanceFragment fragment = new BalanceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_balance, container, false);
        return rootView;
    }
}
