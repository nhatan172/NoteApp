package nhatan172.noteapp.custom.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class DetailPager extends ViewPager {
    public DetailPager(Context context) {
        super(context);
    }
    public DetailPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item,false);
    }
    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, false);
    }
}
