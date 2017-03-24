# Range Picker


This can be used to pick a range between a min and max point. This library was originally created to facilitate
in selecting the time range for trimming videos.


![croppedgif](https://cloud.githubusercontent.com/assets/13211441/23983001/5afd6ca2-0a37-11e7-9167-d488ec5b28f0.gif)


## Usage

  * Include library to your app build.gradle

  `compile 'shashank.com.customrange:range-picker:1.0.0'`

  * Include range picker in your xml layout

  ```xml
  <shashank.com.rangepicker.CustomRange
          android:layout_width="match_parent"
          android:layout_height="40dp"
          android:layout_margin="36dp"
          app:nonSelectedColor="@color/grey_300"
          app:holderWidth="16dp"
          app:holderColor="@color/white"
          app:selectedColor="@color/colorPrimary"
          app:minValue="0"
          app:maxValue="100"
          />
  ```

  * You can get or set min and max value programmatically, like so

  ```java
  mCustomRange.getMinValue();
  mCustomRange.setMaxValue(12.5);
  ```

  If you need a callback when range change you need to implement RangeChangeListener

  ```java
  class YourActivity implements RangeChangeListener {
    private CustomRange mCustomRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find view

        mCustomRange.setRangeChangeListener(this);
    }

    @Override
    public void onRangeChanged(float startValue, float endValue) {

    }
  }
  ```