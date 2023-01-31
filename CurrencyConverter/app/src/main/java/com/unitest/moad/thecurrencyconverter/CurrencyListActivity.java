package com.unitest.moad.thecurrencyconverter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class CurrencyListActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String> adapter;
    String[] currencies;
    ExchangeRateDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_list);

        database = new ExchangeRateDatabase();

        listView = findViewById(R.id.listView);

        currencies = database.getCurrencies();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currencies);
        listView.setAdapter(adapter);

        // To show the capital on the map
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String capitalName = MainActivity.database.getCapital(
                        adapter.getItem(position).toString());

                Uri googleMaps = Uri.parse("geo:0,0?q="+capitalName);
                Intent gmapsIntent = new Intent(Intent.ACTION_VIEW, googleMaps);
                gmapsIntent.setPackage("com.google.android.apps.maps");

                startActivity(gmapsIntent);
            }
        });

    }
}
