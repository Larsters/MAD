package com.unitest.moad.thecurrencyconverter;

import android.app.Activity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class UpdateCurrenciesRunnable implements Runnable{

    Activity activity;
    InputStream inputStream;

    public UpdateCurrenciesRunnable(Activity activity){
        this.activity = activity;
    }

    @Override
    public void run() {

        String xmlUrl = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";

        // Read the currencies from the link and update the database
        try {

            URL url = new URL(xmlUrl);
            URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            String encoding = connection.getContentEncoding();
            parser.setInput(inputStream, encoding);
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG && parser.getName().equals("Cube")) {

                    String currencyName = parser.getAttributeValue(null, "currency");

                    if (parser.getAttributeValue(null, "rate") != null) {

                        Double rate = Double.parseDouble(parser.getAttributeValue(null, "rate"));
                        MainActivity.database.setExchangeRate(currencyName, rate);

                    }

                }

                eventType = parser.next();
            }


        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            // Try to close the input stream
            if(inputStream != null){

                try{

                    inputStream.close();

                } catch (IOException e){

                    e.printStackTrace();

                }
            }
        }
    }
}
