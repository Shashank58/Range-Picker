package shashank.com.customrange;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import shashank.com.rangepicker.CustomRange;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomRange customRange = (CustomRange) findViewById(R.id.custom_range);
        customRange.setRangeChangeListener(new CustomRange.RangeChangeListener() {
            @Override
            public void onRangeChanged(float startValue, float endValue) {
                Log.d("TAG", "onRangeChanged: Start value - " + startValue + "; End value - " + endValue);
            }
        });
    }
}
