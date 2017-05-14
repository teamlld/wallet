package teamlld.nik.uniobuda.hu.walletapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;

import teamlld.nik.uniobuda.hu.walletapp.R;
import teamlld.nik.uniobuda.hu.walletapp.models.Currency;

/**
 * Created by admin on 2017. 05. 13..
 */

public class ConverterActivity extends BaseActivity {

    public static double selectedFrom;
    public static double selectedTo;
    Button fromButton;
    Button toButton;
    private static final int FROM_REQUEST_CODE = 1;
    private static final int TO_REQUEST_CODE = 2;
    Button convertButton;
    AppCompatEditText text;
    AppCompatTextView resultText;
    AppCompatTextView toValuta;
    AppCompatTextView fromValuta;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);

        toValuta = (AppCompatTextView) findViewById(R.id.to_valuta);
        fromValuta = (AppCompatTextView) findViewById(R.id.from_valuta);

        fromButton = (Button) findViewById(R.id.conv_from);
        toButton = (Button) findViewById(R.id.conv_to);

        fromButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CurrencySelectorActivity.class);
                intent.putExtra("message", 1);
                startActivityForResult(intent, FROM_REQUEST_CODE);
            }
        });

        toButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CurrencySelectorActivity.class);
                intent.putExtra("message", 2);
                startActivityForResult(intent, FROM_REQUEST_CODE);
            }
        });

        resultText = (AppCompatTextView) findViewById(R.id.result);

        convertButton = (Button) findViewById(R.id.conv_todo);
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double res = Convert();
                resultText.setText(String.format("%.2f", res));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == FROM_REQUEST_CODE) {
            fromButton.setText(data.getExtras().getString("name"));
            selectedFrom = data.getExtras().getDouble("value");
            fromValuta.setText(data.getExtras().getString("name"));
        } else if (resultCode == TO_REQUEST_CODE) {
            toButton.setText(data.getExtras().getString("name"));
            selectedTo = data.getExtras().getDouble("value");
            toValuta.setText(data.getExtras().getString("name"));
        }
    }

    private double Convert() {
        text = (AppCompatEditText) findViewById(R.id.conv_sum);
        try {
            double source = Double.parseDouble(text.getText().toString());
            double sum = source / selectedFrom * selectedTo;
            return sum;
        } catch (NumberFormatException ex) {
            Toast.makeText(ConverterActivity.this, "Rossz formátum!", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException ex) {
            Toast.makeText(ConverterActivity.this, "Adjon meg értéket!", Toast.LENGTH_SHORT).show();
        }
        return 0.0;
    }
}
