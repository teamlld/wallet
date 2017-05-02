package teamlld.nik.uniobuda.hu.walletapp;

import android.app.assist.AssistContent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import teamlld.nik.uniobuda.hu.walletapp.DatabaseHandler;

/**
 * Created by admin on 2017. 05. 01..
 */

public class BaseActivity extends AppCompatActivity {

    public static DatabaseHandler database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = DatabaseHandler.getInstance(getApplicationContext());
    }
}
