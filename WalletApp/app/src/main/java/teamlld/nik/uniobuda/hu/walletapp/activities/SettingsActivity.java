package teamlld.nik.uniobuda.hu.walletapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import teamlld.nik.uniobuda.hu.walletapp.R;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();
        final Boolean messageIsNewUser = intent.getExtras().getBoolean(NavDrawerActivity.SETTINGS_MESSAGE, false);
        currUserId = intent.getExtras().getInt("userid");

        Button saveButton=(Button)findViewById(R.id.saveSettings);
        final EditText name=(EditText)findViewById(R.id.name);
        final EditText newBalanceValue=(EditText)findViewById(R.id.startingBalance);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().length()>0 && newBalanceValue.getText().length()>0)
                {
                    int balance = Integer.parseInt(newBalanceValue.getText().toString());
                    String nameValue=name.getText().toString();
                    if(messageIsNewUser)
                    {
                        database.insertUser(nameValue,balance,currUserId);
                    }
                    else
                    {

                        database.updateUserData(nameValue, balance, currUserId);
                    }
                    Intent intent = new Intent();
                    intent.putExtra("username",nameValue);
                    intent.putExtra("balance",balance);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });


    }
}
