package teamlld.nik.uniobuda.hu.walletapp;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Random;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        BalanceFragment balanceFragment=BalanceFragment.newInstance();
        DiagramFragment diagramFragment= DiagramFragment.newInstance();
        LatestItemsFragment latestItemsFragment=LatestItemsFragment.newInstance();

        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.balanceFragmentContainer, balanceFragment);
        transaction.replace(R.id.diagramFragmentContainer, diagramFragment);
        transaction.replace(R.id.latesItemsFragmentContainer, latestItemsFragment);
        transaction.commit();

    }
}
