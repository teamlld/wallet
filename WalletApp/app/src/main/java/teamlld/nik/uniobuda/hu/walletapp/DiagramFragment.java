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

public class DiagramFragment extends Fragment {

    View rootView;

    public static DiagramFragment newInstance(){

        Bundle args=new Bundle();
        DiagramFragment fragment= new DiagramFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_diagram, container, false);
        return  rootView;
    }
}
