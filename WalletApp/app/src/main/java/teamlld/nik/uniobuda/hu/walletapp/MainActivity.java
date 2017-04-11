package teamlld.nik.uniobuda.hu.walletapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity{

    public static DatabaseHandler handler;
    LoginButton loginButton;
    CallbackManager callbackManager;

    Button demoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        //demo-login
        demoButton=(Button)findViewById(R.id.demoButton);
        demoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startDashboardActivity();
            }
        });



        //facebok-login
        if (AccessToken.getCurrentAccessToken() == null) {
            loginButton = (LoginButton) findViewById(R.id.login_button);
            callbackManager = CallbackManager.Factory.create();
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    handler = new DatabaseHandler(MainActivity.this);
                    startDashboardActivity();
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });
        }
        else {
            callbackManager = CallbackManager.Factory.create();
            startDashboardActivity();
        }

        //TODO insertek mire hívódjanak meg
    }

    void startDashboardActivity()
    {
        handler = new DatabaseHandler(MainActivity.this);
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        //intent.putExtra();
        startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}



