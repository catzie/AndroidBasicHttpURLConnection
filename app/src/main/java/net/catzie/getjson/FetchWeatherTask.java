package net.catzie.getjson;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Catzie on 13/07/2016.
 */
public class FetchWeatherTask extends AsyncTask<Void, Void, Boolean>{

    private String TAG = FetchWeatherTask.class.getSimpleName();
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String openWeatherApiKey = "72859d55acf56189d9df6945c87daf84";
    String forecastJsonStr = null;
    String location = null;
    Activity activity = null;
    TextView jsonResult;

    public FetchWeatherTask(String location, Activity activity){
        this.location = location;
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Void... params){
        Uri.Builder apiUrl = new Uri.Builder();
        apiUrl.scheme("http");
        apiUrl.authority("api.openweathermap.org");
        apiUrl.path("/data/2.5/forecast/daily");
        apiUrl.appendQueryParameter("q", location);
        apiUrl.appendQueryParameter("appid", openWeatherApiKey);
        apiUrl.appendQueryParameter("mode", "json");
        apiUrl.appendQueryParameter("units", "metrics");
        apiUrl.appendQueryParameter("cnt", "7");

        try {

            URL url = new URL(apiUrl.toString());

            // Create request to OpenWeatherMap
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            // Connect the HTTPURLConnection
            urlConnection.connect();

            // Read input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            // Nothing to do if inputStream is null
            if(inputStream == null){
                return null;
            }

            // BufferedReader

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while((line = reader.readLine()) != null){
                buffer.append(line + "\n");
            }

            // Nothing to do if inputStream is empty
            if(buffer.length() == 0){
                return null;
            }

            forecastJsonStr = buffer.toString();
            Log.v(TAG, "forecastJsonStr=" + forecastJsonStr);


        } catch (MalformedURLException e){
            Log.e(TAG, "MalformedURLException. " + e.getMessage());
            return null;
        } catch (IOException e){
            Log.e(TAG, "IOException. " + e.toString());
            return null;
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean result){
        super.onPostExecute(result);
        jsonResult = (TextView) activity.findViewById(R.id.jsonResult);
        jsonResult.setText(forecastJsonStr);
    }
}
