package teamlld.nik.uniobuda.hu.walletapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class DiagramActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagram);

        //FIXME userID-t honnan kap
        CategoryDiagramFragment categoryFragment = CategoryDiagramFragment.newInstance();
        CategoryIncomeDiagramFragment categoryIncomeFragment=CategoryIncomeDiagramFragment.newInstance();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.categoryContainer, categoryFragment);
        transaction.replace(R.id.categoryIncomeContainer, categoryIncomeFragment);
        transaction.commit();
    }
}
