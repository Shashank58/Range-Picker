package shashank.com.customrange;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by shashankm on 14/03/17.
 */
public class CustomRange extends View implements View.OnTouchListener {
    private static final String TAG = CustomRange.class.getSimpleName();
    private static final float DEFAULT_HOLDER_WIDTH = 16f;

    private enum DragPosition {
        START, END, NOT_DEFINED
    }

    private int startPosition = 0;

    private int endPosition = 100;

    private DragPosition draggingPosition = DragPosition.NOT_DEFINED;

    private Paint progressPaint;

    private int holderColor;

    private float holderWidth;

    private int nonSelectedColor;

    private int selectedColor;

    public CustomRange(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, context);
    }

    public CustomRange(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomRange(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        int halfHeight = getHeight() / 2;
        int startX = getWidth() * startPosition / 100;
        int endX = getWidth() * endPosition / 100;

        // draw the part of the bar that's filled
        //noinspection SuspiciousNameCombination
        progressPaint.setStrokeWidth(height);

        progressPaint.setColor(nonSelectedColor);
        canvas.drawLine(0, halfHeight, startX, halfHeight, progressPaint);

        progressPaint.setColor(selectedColor);
        canvas.drawLine(startX + 10, halfHeight, endX - 10, halfHeight, progressPaint);

        // draw the unfilled section
        progressPaint.setColor(nonSelectedColor);
        canvas.drawLine(endX, halfHeight, getWidth(), halfHeight, progressPaint);

        progressPaint.setColor(holderColor);
        progressPaint.setStrokeWidth(holderWidth);
        canvas.drawLine(startX + 10, height, startX + 10, 0, progressPaint);
        canvas.drawLine(endX - 10, height, endX - 10, 0, progressPaint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int dragPoint = (int) (100 + (((event.getX() - v.getWidth()) / v.getWidth()) * 100));

        if (dragPoint <= 0 || dragPoint >= 100) return true;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isCloseToStart(dragPoint)) {
                    startPosition = dragPoint;
                    draggingPosition = DragPosition.START;
                } else {
                    endPosition = dragPoint;
                    draggingPosition = DragPosition.END;
                }
                invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
                if (draggingPosition == DragPosition.NOT_DEFINED) return true;

                if (draggingPosition == DragPosition.START && dragPoint < (endPosition - 10)) {
                    startPosition = dragPoint;
                } else if (draggingPosition == DragPosition.END && dragPoint > (startPosition + 10)) {
                    endPosition = dragPoint;
                }

                invalidate();
                return true;

            case MotionEvent.ACTION_UP:
                draggingPosition = DragPosition.NOT_DEFINED;
                return true;
        }
        return false;
    }

    private boolean isCloseToStart(int percent) {
        return Math.abs(percent - startPosition) < Math.abs(percent - endPosition);
    }

    private void init(AttributeSet attrs, Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomRange);
        try {
            holderColor = typedArray.getColor(R.styleable.CustomRange_holderColor, Color.WHITE);
            holderWidth = typedArray.getDimension(R.styleable.CustomRange_holderWidth, convertDpToPixel(DEFAULT_HOLDER_WIDTH, context));
            nonSelectedColor = typedArray.getColor(R.styleable.CustomRange_nonSelectedColor, Color.GRAY);
            selectedColor = typedArray.getColor(R.styleable.CustomRange_selectedColor, Color.GREEN);
        } finally {
            typedArray.recycle();
        }

        progressPaint = new Paint();
        progressPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        setOnTouchListener(this);
    }

    private float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}

