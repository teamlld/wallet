package teamlld.nik.uniobuda.hu.walletapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    DatabaseHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler=new DatabaseHandler(this);

        Intent intent = new Intent(this, DashboardActivity.class);
        //intent.putExtra();
        startActivity(intent);


        //TODO insertek mire hívódjanak meg
    }
}
