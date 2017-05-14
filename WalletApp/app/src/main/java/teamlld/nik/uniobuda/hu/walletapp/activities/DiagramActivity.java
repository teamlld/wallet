package teamlld.nik.uniobuda.hu.walletapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import teamlld.nik.uniobuda.hu.walletapp.R;
import teamlld.nik.uniobuda.hu.walletapp.fragments.CategoryExpenseDiagramFragment;
import teamlld.nik.uniobuda.hu.walletapp.fragments.CategoryIncomeDiagramFragment;

public class DiagramActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagram);

        Bundle args=new Bundle();
        args.putInt("userId",currUserId);

        CategoryIncomeDiagramFragment categoryIncomeFragment=new CategoryIncomeDiagramFragment();
        categoryIncomeFragment.setArguments(args);
        CategoryExpenseDiagramFragment categoryExpenseFragment =new CategoryExpenseDiagramFragment();
        categoryExpenseFragment.setArguments(args);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.categoryIncomeContainer, categoryIncomeFragment);
        transaction.replace(R.id.categoryContainer, categoryExpenseFragment);
        transaction.commit();
    }
}
