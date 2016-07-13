package net.catzie.getjson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView jsonResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jsonResult = (TextView) findViewById(R.id.jsonResult);

        FetchWeatherTask fetchWeatherTask = new FetchWeatherTask("Manila,PH", MainActivity.this);
        fetchWeatherTask.execute();
    }
}
