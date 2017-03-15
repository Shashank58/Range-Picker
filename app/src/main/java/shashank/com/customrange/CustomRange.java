package shashank.com.customrange;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by shashankm on 14/03/17.
 */
public class CustomRange extends View implements View.OnTouchListener {
    private static final String TAG = CustomRange.class.getSimpleName();

    private enum DragPosition {
        START, END, NOT_DEFINED
    }

    private int startPosition = 0;
    private int endPosition = 100;
    private DragPosition draggingPosition = DragPosition.NOT_DEFINED;
    private Paint progressPaint;
    private Context context;

    public CustomRange(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public CustomRange(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomRange(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        int halfHeight = getHeight() / 2;
        int startX = getWidth() * startPosition / 100;
        int endX = getWidth() * endPosition / 100;

        // draw the part of the bar that's filled
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setStrokeWidth(getHeight());

        //progressPaint.setColor(ContextCompat.getColor(context, R.color.grey_300));
        canvas.drawLine(0, halfHeight, startX, halfHeight, progressPaint);

        //progressPaint.setColor(ContextCompat.getColor(context, R.color.teal_500));
        canvas.drawLine(startX + 15, halfHeight, endX - 15, halfHeight, progressPaint);

        // draw the unfilled section
        //progressPaint.setColor(ContextCompat.getColor(context, R.color.grey_300));
        canvas.drawLine(endX, halfHeight, getWidth(), halfHeight, progressPaint);

        //progressPaint.setColor(ContextCompat.getColor(context, R.color.white));
        progressPaint.setStrokeWidth(30f);
        canvas.drawLine(startX + 10, height, startX + 10, 0, progressPaint);
        canvas.drawLine(endX - 10, height, endX - 10, 0, progressPaint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int percent = (int) (100 + (((event.getX() - v.getWidth()) / v.getWidth()) * 100));
                if (isCloseToStart(percent)) {
                    startPosition = percent;
                    draggingPosition = DragPosition.START;
                } else {
                    endPosition = percent;
                    draggingPosition = DragPosition.END;
                }
                invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
                if (draggingPosition == DragPosition.NOT_DEFINED) return true;

                int dragPoint = (int) (100 + (((event.getX() - v.getWidth()) / v.getWidth()) * 100));

                if (dragPoint <= 0 || dragPoint >= 100) return true;

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

    private void init(AttributeSet attrs) {
        progressPaint = new Paint();
        progressPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        setOnTouchListener(this);
    }
}

