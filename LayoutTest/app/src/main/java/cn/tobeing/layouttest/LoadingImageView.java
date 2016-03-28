
package cn.tobeing.layouttest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * 描述:Loading加载控件
 * 
 * @author zhengsun
 * @since 2014年8月28日 下午4:13:37
 */
public class LoadingImageView extends TestImageView {

    public LoadingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LoadingImageView(Context context) {
        super(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startLoading();
    }
    public void startLoading(){
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getContext(),
                R.anim.comm_loading);
        startAnimation(hyperspaceJumpAnimation);
    }
    public void stopLoading(){
        clearAnimation();
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopLoading();
    }
    
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if(getParent()!=null&&((View)getParent()).getVisibility()==VISIBLE&&getVisibility()==VISIBLE){
            startLoading();
        }else{
            clearAnimation();
        }
    }
}
