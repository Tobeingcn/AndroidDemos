package cn.tobeing.layouttest;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

/**
 * Created by sunzheng on 16/1/22.
 */
public class StaticFrameLayout extends FrameLayout{
    private static final String TAG="StaticFrameLayout";
    public StaticFrameLayout(Context context) {
        super(context);
    }

    public StaticFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public StaticFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw");
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Log.d(TAG, "draw");
    }

    @Override
    public void invalidate() {
        super.invalidate();
        Log.d(TAG, "invalidate");

    }

    @Override
    public void requestLayout() {
        super.requestLayout();
        Log.d(TAG,"requestLayout");
    }
}
