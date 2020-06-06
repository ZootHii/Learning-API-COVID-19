package com.zoothii.api;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.textclassifier.ConversationActions;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
    }

    public void getData(View view){
        DownloadData downloadData = new DownloadData();
        try{
            Request request = new Request.Builder()
                    .url("https://covid-193.p.rapidapi.com/statistics?country=Turkey")
                    .get()
                    .addHeader("x-rapidapi-host", "covid-193.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", "139c8de81bmsh5a28aebc56b0931p1e090bjsn9da2c25ba7a7")
                    .build();

            downloadData.execute(request);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private class DownloadData extends AsyncTask<Request, Void, String>{

        @Override
        protected String doInBackground(Request... requests) {
            String myResponse = "";
            Response response;
            OkHttpClient client = new OkHttpClient();
            try {
                response = client.newCall(requests[0]).execute();
                if (response.isSuccessful()){
                    myResponse = response.body().string();
                    return myResponse;
                } else {
                    return null;
                }
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //System.out.println("DATA: " + s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String response = jsonObject.getString("response");
                //System.out.println("DATA: " + response);

                JSONArray jsonArray = new JSONArray(response);
                String response0 = jsonArray.getString(0);
                //System.out.println("DATA: " + response0);

                JSONObject responses = new JSONObject(response0);
                String country = responses.getString("country");
                //System.out.println("DATA: " + country);

                String cases = responses.getString("cases");
                JSONObject casesJason = new JSONObject(cases);
                String new_cases = casesJason.getString("new");
                String active_cases = casesJason.getString("active");
                String critical_cases = casesJason.getString("critical");
                String recovered_cases = casesJason.getString("recovered");
                String total_cases = casesJason.getString("total");
                //System.out.println("DATA: " + new_cases+" "+active_cases+" "+critical_cases+" "+recovered_cases+" "+total_cases);

                String deaths = responses.getString("deaths");
                JSONObject deathsJason = new JSONObject(deaths);
                String new_deaths = deathsJason.getString("new");
                String total_deaths = deathsJason.getString("total");
                //System.out.println("DATA: " + new_deaths+" "+total_deaths);

                String day = responses.getString("day");
                //System.out.println("DATA: " + day);

                textView.setText("COVID-19 TÜRKİYE" +
                        "\nBUGÜN: " + day +
                        "\nBugünki vaka: " + new_cases +
                        "\nBugünki ölüm: " + new_deaths +
                        "\nAktif vaka: " + active_cases +
                        "\nKritik vaka: " + critical_cases +
                        "\nToplam iyileşen: " + recovered_cases +
                        "\nToplam vaka: " + total_cases +
                        "\nToplam ölüm: " + total_deaths);
                textView.setEnabled(false);
            } catch (Exception e){

            }


        }
    }
}

