package teamlld.nik.uniobuda.hu.walletapp.activities;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import teamlld.nik.uniobuda.hu.walletapp.R;
import teamlld.nik.uniobuda.hu.walletapp.fragments.BalanceDiagramFragment;
import teamlld.nik.uniobuda.hu.walletapp.fragments.BalanceFragment;
import teamlld.nik.uniobuda.hu.walletapp.fragments.LatestItemsFragment;
import teamlld.nik.uniobuda.hu.walletapp.models.User;

public class NavDrawerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private User user;
    public static final String SETTINGS_MESSAGE="isNewUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        if (AccessToken.getCurrentAccessToken() == null) {
            goLoginScreen();
        }
        else {
            if (getIntent().getExtras() != null){
                Bundle args = getIntent().getExtras();
                if (args.containsKey("name")){
                    Toast.makeText(NavDrawerActivity.this, args.getString("name"), Toast.LENGTH_LONG).show();
                }
                if (args.containsKey("id")){
                    Toast.makeText(NavDrawerActivity.this, args.getString("id"), Toast.LENGTH_LONG).show();
                    super.currUserId=args.getInt("id");
                }
            }

            user = new User("temp", 0, 0);
            Cursor cursor = database.getUserById(currUserId);
            if (cursor.getCount() > 0) {
                //van ilyen ID-val user,
                user.setName(cursor.getString(cursor.getColumnIndex("name")));
                user.setBalance(cursor.getInt(cursor.getColumnIndex("balance")));
                user.setStartingBalance(cursor.getInt(cursor.getColumnIndex("startingBalance")));
                user.setId(cursor.getInt(cursor.getColumnIndex("_userId")));
                SetFragments();
            } else {
                //nincs ilyen ID-val user, indítunk egy activity-t ahol kezdőértékeket állíthat
                Intent intent=new Intent(this,SettingsActivity.class);
                intent.putExtra(SETTINGS_MESSAGE, true);
                intent.putExtra("userid",currUserId);
                startActivityForResult(intent,1);
            }
        }
    }

    private void SetFragments()
    {
        BalanceFragment balanceFragment = BalanceFragment.newInstance(user);
        BalanceDiagramFragment balanceDiagramFragment = BalanceDiagramFragment.newInstance(user);
        LatestItemsFragment latestItemsFragment = LatestItemsFragment.newInstance(currUserId);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.balanceFragmentContainer, balanceFragment);
        transaction.replace(R.id.diagramFragmentContainer, balanceDiagramFragment);
        transaction.replace(R.id.latesItemsFragmentContainer, latestItemsFragment);
        transaction.commit();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavDrawerActivity.this, NewTransactionActivity.class);
                intent.putExtra("userid", currUserId);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        user.setId(currUserId);
        user.setName(data.getExtras().getString("username"));
        user.setBalance(data.getExtras().getInt("balance"));
        user.setStartingBalance(data.getExtras().getInt("balance"));
        SetFragments();
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
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_convert) {
            Intent intent = new Intent(NavDrawerActivity.this, ConverterActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            logout();
        } else if (id == R.id.nav_all_transactions) {
            Intent intent = new Intent(NavDrawerActivity.this, AllTransactionsActivity.class);
            intent.putExtra("userid", user.getId());
            startActivity(intent);

        } else if (id == R.id.nav_graphs) {
            Intent intent = new Intent(this, DiagramActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra(SETTINGS_MESSAGE,false);
            startActivity(intent);
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
