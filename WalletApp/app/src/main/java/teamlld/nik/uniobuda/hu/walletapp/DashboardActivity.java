package teamlld.nik.uniobuda.hu.walletapp;

import android.os.Bundle;

import teamlld.nik.uniobuda.hu.walletapp.BaseActivity;
import teamlld.nik.uniobuda.hu.walletapp.R;

public class DashboardActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

       /* if (AccessToken.getCurrentAccessToken() == null) {
            goLoginScreen();
        }

            if (getIntent().getExtras() != null){
                Bundle args = getIntent().getExtras();
                if (args.containsKey("name")){
                    Toast.makeText(DashboardActivity.this, args.getString("name"), Toast.LENGTH_LONG).show();
                }
                if (args.containsKey("id")){
                    Toast.makeText(DashboardActivity.this, args.getString("id"), Toast.LENGTH_LONG).show();
                }
        }

        Button button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });*/



/*
        final int userId = 1000; //TODO ezt az ID-t kapjuk majd a MainActivity-től

        final User user = new User("temp", 0, 0);
        Cursor cursor = BaseActivity.database.getUserById(userId);
        if (cursor.getCount() > 0) {
            //van ilyen ID-val user,
            user.setName(cursor.getString(cursor.getColumnIndex("name")));
            user.setBalance(cursor.getInt(cursor.getColumnIndex("balance")));
            user.setId(userId);
        } else {
            //nincs ilyen ID-val user, beszúrjuk az adatbázisba is.
            //TODO egy activity amin ki tudja választani az új User a nevét, kezdő egyenlegét?
            user.setName("Béla");
            user.setBalance(100000);
            user.setId(userId);
            BaseActivity.database.insertUser(user.getName(), user.getBalance(), user.getId());
        }

        //BaseActivity.database.loadDatabaseWithDemoData();


        BalanceFragment balanceFragment = BalanceFragment.newInstance(user);
        DiagramFragment diagramFragment = DiagramFragment.newInstance(user);
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
        });*/
    }

    /*private void goLoginScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void logout(){
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }*/
}
