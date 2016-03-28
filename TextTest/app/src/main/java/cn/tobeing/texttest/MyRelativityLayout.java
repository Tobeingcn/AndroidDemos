package cn.tobeing.texttest;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

/**
 * Created by sunzheng on 16/3/28.
 */
public class MyRelativityLayout extends RelativeLayout{

    public MyRelativityLayout(Context context) {
        super(context);
    }

    public MyRelativityLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRelativityLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("suntest","onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.d("suntest","onLayout");
    }
}
