package teamlld.nik.uniobuda.hu.walletapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

public class NavDrawerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        if (AccessToken.getCurrentAccessToken() == null) {
            goLoginScreen();
        }

        if (getIntent().getExtras() != null){
            Bundle args = getIntent().getExtras();
            if (args.containsKey("name")){
                Toast.makeText(NavDrawerActivity.this, args.getString("name"), Toast.LENGTH_LONG).show();
            }
            if (args.containsKey("id")){
                Toast.makeText(NavDrawerActivity.this, args.getString("id"), Toast.LENGTH_LONG).show();
            }
        }

       /* Button button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        Button convertButton = (Button) findViewById(R.id.convertbtn);
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavDrawerActivity.this, CurrencySelectorActivity.class);
                startActivity(intent);
            }
        });*/

        final int userId = 1000; //TODO ezt az ID-t kapjuk majd a MainActivity-től

        user = new User("temp", 0, 0);
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


        /*Button newTransactionButton = (Button) findViewById(R.id.newTransactionButton);
        newTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavDrawerActivity.this, NewTransactionActivity.class);
                intent.putExtra("userid", userId);
                startActivity(intent);
            }
        });*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavDrawerActivity.this, NewTransactionActivity.class);
                intent.putExtra("userid", userId);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_convert) {
            //TODO converter meghívása
        } else if (id == R.id.nav_logout) {
            logout();
        } else if (id == R.id.nav_all_transactions) {
            //TODO alltransactions meghívása
            Intent intent = new Intent(NavDrawerActivity.this, AllTransactionsActivity.class);
            intent.putExtra("userid", user.getId());
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_graphs) {
            Intent intent = new Intent(this, DiagramActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goLoginScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void logout(){
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
