package teamlld.nik.uniobuda.hu.walletapp;

import android.content.Intent;
import android.hardware.camera2.params.Face;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {

    public static DatabaseHandler handler;
    LoginButton loginButton;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        if (AccessToken.getCurrentAccessToken() == null) {

            loginButton = (LoginButton) findViewById(R.id.login_button);
            callbackManager = CallbackManager.Factory.create();
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    handler = new DatabaseHandler(MainActivity.this);
                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    //intent.putExtra();
                    startActivity(intent);
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
            handler = new DatabaseHandler(MainActivity.this);
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            //intent.putExtra();
            startActivity(intent);
        }

        //TODO insertek mire hívódjanak meg
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
