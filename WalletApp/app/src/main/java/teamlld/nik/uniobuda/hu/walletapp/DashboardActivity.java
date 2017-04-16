package teamlld.nik.uniobuda.hu.walletapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.login.LoginManager;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Button button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intent);
                LoginManager.getInstance().logOut();
                finish();
            }
        });

        final int userId = 1000; //TODO ezt az ID-t kapjuk majd a MainActivity-től

        final User user = new User("temp", 0, 0);
        Cursor cursor = MainActivity.handler.getUserById(userId);
        if (cursor.getCount() > 0) {
            //van ilyen ID-val user,
            user.setName(cursor.getString(cursor.getColumnIndex("name")));
            user.setBalance(cursor.getInt(cursor.getColumnIndex("balance")));
            user.setId(userId);
        } else {
            //nincs ilyen ID-val user, beszúrjuk az adatbázisba is.
            //TODO egy activity amin ki tudja választani az új User a nevét, kezdő egyenlegét?
            user.setName("Jani");
            user.setBalance(100000);
            user.setId(userId);
            MainActivity.handler.insertUser(user.getName(), user.getBalance(), user.getId());
        }
        MainActivity.handler.generateDemoDataTransactions(userId);


        BalanceFragment balanceFragment = BalanceFragment.newInstance(user);
        DiagramFragment diagramFragment = DiagramFragment.newInstance(userId);
        LatestItemsFragment latestItemsFragment = LatestItemsFragment.newInstance(userId);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.balanceFragmentContainer, balanceFragment);
        transaction.replace(R.id.diagramFragmentContainer, diagramFragment);
        transaction.replace(R.id.latesItemsFragmentContainer, latestItemsFragment);
        transaction.commit();


        Button newTransactionButton = (Button) findViewById(R.id.newTransactionButton);
        newTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, NewTransactionActivity.class);
                intent.putExtra("userid", userId);
                startActivity(intent);
            }
        });
    }
}
