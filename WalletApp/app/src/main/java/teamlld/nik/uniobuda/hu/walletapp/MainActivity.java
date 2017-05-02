package teamlld.nik.uniobuda.hu.walletapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.facebook.CallbackManager;

public class MainActivity extends BaseActivity {

    //public static DatabaseHandler handler;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Button debugLoginButton = (Button) findViewById(R.id.debuglogin);
        debugLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler = new DatabaseHandler(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                //intent.putExtra();
                startActivity(intent);
                finish();
            }
        });
        */


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(LoginFragment.class.getName());
        ft.replace(R.id.login, new LoginFragment());
        ft.commit();

        callbackManager = CallbackManager.Factory.create();
        /*LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });*/

        //TODO insertek mire hívódjanak meg
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
