package cn.tobeing.kugousdcard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by sunzheng on 16/4/27.
 */
public class MyTestView extends View{
    public MyTestView(Context context) {
        super(context);
    }

    public MyTestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec((int) (MeasureSpec.getSize(heightMeasureSpec)*0.5),MeasureSpec.EXACTLY), heightMeasureSpec);

    }
}
