package com.unitest.moad.thecurrencyconverter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private List<String> currencies;

    public CustomAdapter(List<String> currencies) {
        this.currencies = currencies;
    }

    @Override
    public int getCount() {
        return currencies.size();
    }

    @Override
    public Object getItem(int position) {
        return currencies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        String currencyNames = currencies.get(position);


    if(convertView == null){
        LayoutInflater lf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = lf.inflate(R.layout.custom_adapter, null, false);
    }

    // Flags for currency list

    ImageView flag = convertView.findViewById(R.id.flag);
    int flagId = context.getResources().getIdentifier("flag_" + currencyNames.toLowerCase(), "drawable", context.getPackageName());
    flag.setImageResource(flagId);

    // Currency names for currency list
    TextView currencyName = convertView.findViewById(R.id.currency_name);
    currencyName.setText(currencyNames);


    // Currency rates for currency list
    TextView currencyRate = convertView.findViewById(R.id.currency_rate);
    currencyRate.setText(Double.toString(MainActivity.database.getExchangeRate(currencyNames)));

        return convertView;
    }


}
