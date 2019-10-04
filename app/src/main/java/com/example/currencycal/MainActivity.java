package com.example.currencycal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    static Double resultVal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Init
        final Button button =  findViewById(R.id.convBtn);
        final EditText text= findViewById(R.id.edt2);
        final EditText moneyText =  findViewById(R.id.edt1);
        final Spinner fromSpinner =  findViewById(R.id.fromSpinner);
        final Spinner toSpinner =  findViewById(R.id.toSpinner);
        resultVal = 0.0;
        text.setText(resultVal.toString());


        final Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                try {
                    try {
                        String mainUrl = "http://data.fixer.io/api/latest?access_key=b554955059fa172e9d437dc05adee0e3";
                        String updatedUrl = mainUrl + "?base=" + fromSpinner.getSelectedItem();
                        URL url = new URL(updatedUrl);

                        urlConnection = (HttpURLConnection) url.openConnection();

                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        BufferedReader inReader = new BufferedReader(new InputStreamReader(in));
                        String inputLine = "";
                        String fullStr = "";
                        while ((inputLine = inReader.readLine()) != null) {
                            fullStr += inputLine;
                        }

                        JSONObject jsonObj = new JSONObject(fullStr);
                        JSONObject result = jsonObj.getJSONObject("rates");


                        Double moneyValue = Double.valueOf(moneyText.getText().toString());

                        if (fromSpinner.getSelectedItem().equals(toSpinner.getSelectedItem())) {
                            resultVal = moneyValue;
                        } else {
                            Double rateValue = Double.valueOf(result.getString((String) toSpinner.getSelectedItem()));
                            Double resultValue = moneyValue * rateValue;
                            resultVal = resultValue;
                        }
                    } finally {
                        if (urlConnection != null)
                            urlConnection.disconnect();
                    }


                } catch (NumberFormatException e) {
                    //TODO: Alertbox ekle

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                thread.start();
                try {
                    thread.join();
                    text.setText(resultVal.toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });


    }

}
