package com.sking.lib.res.views;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.sking.lib.res.R;
import com.sking.lib.utils.SKScreenUtil;

/**
 * @Created by Im_Jingwei
 * @time 2017/11/15  14:39
 * @Describe: 仿ios侧滑关闭FrameLayout,
 * 与viewpager使用时在OnPageChangeListener，posiiton==0是设置swipeAnyWhere为true，postion>0设置为false，解决冲突
 */
public class SKRFrameLayout extends FrameLayout {

    private String TAG = getClass().getName();

    /**
     * 是否可以滑动关闭页面,true启用，false不起用
     */
    public boolean swipeEnabled = true;

    //是否可以在页面任意位置右滑关闭页面，如果是false则从左边滑才可以关闭。
    public boolean swipeAnyWhere = true;


    /**********************以下不用修改**************************/
    //是否使用关闭动画
    private boolean swipeFinished = false;

    // private View backgroundLayer;用来设置滑动时的背景色
    private Drawable leftShadow;

    // 能否滑动
    private boolean canSwipe = false;

    //超过了touchslop仍然没有达到没有条件，则忽略以后的动作
    private boolean ignoreSwipe = false;

    private int sideWidthInDP = 16;
    private int sideWidth = 72;
    private int screenWidth = 1080;

    private float downX;
    private float downY;
    private float lastX;
    private float currentX;
    private float currentY;

    private int touchSlopDP = 15;
    private int touchSlop = 60;
    private View content;
    private Activity mActivity;
    private VelocityTracker tracker;


    public SKRFrameLayout(Context context) {
        super(context);
    }

    public SKRFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SKRFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void replaceLayer(Activity activity) {
        leftShadow = activity.getResources().getDrawable(R.mipmap.skr_img_left_shadow);
        touchSlop = (int) (touchSlopDP * activity.getResources().getDisplayMetrics().density);
        sideWidth = (int) (sideWidthInDP * activity.getResources().getDisplayMetrics().density);
        mActivity = activity;
        screenWidth = SKScreenUtil.getWidth(mActivity);
        setClickable(true);
        final ViewGroup root = (ViewGroup) activity.getWindow().getDecorView();
        content = root.getChildAt(0);
        ViewGroup.LayoutParams params = content.getLayoutParams();
        ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(-1, -1);
        root.removeView(content);
        this.addView(content, params2);
        root.addView(this, params);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected boolean drawChild(@NonNull Canvas canvas, @NonNull View child, long drawingTime) {
        boolean result = super.drawChild(canvas, child, drawingTime);
        final int shadowWidth = leftShadow.getIntrinsicWidth();
        int left = (int) (getContentX()) - shadowWidth;
        leftShadow.setBounds(left, child.getTop(), left + shadowWidth, child.getBottom());
        leftShadow.draw(canvas);
        return result;
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        if (swipeEnabled && !canSwipe && !ignoreSwipe) {
            if (swipeAnyWhere) {
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = ev.getX();
                        downY = ev.getY();
                        currentX = downX;
                        currentY = downY;
                        lastX = downX;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dx = ev.getX() - downX;
                        float dy = ev.getY() - downY;
                        if (dx * dx + dy * dy > touchSlop * touchSlop && dx > 0f) {
                            if (dy == 0f || Math.abs(dx / dy) > 1) {
                                downX = ev.getX();
                                downY = ev.getY();
                                currentX = downX;
                                currentY = downY;
                                lastX = downX;
                                canSwipe = true;
                                tracker = VelocityTracker.obtain();
                                return true;
                            } else {
                                ignoreSwipe = true;
                            }
                        } else
                            canSwipe = false;
                        break;
                }
            } else if (ev.getAction() == MotionEvent.ACTION_DOWN && ev.getX() < sideWidth) {
                canSwipe = true;
                tracker = VelocityTracker.obtain();
                return true;
            }
        }
        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            ignoreSwipe = false;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return canSwipe || super.onInterceptTouchEvent(ev);// ||左边为true返回左侧，否则返回右侧，&&左侧为true返回右侧，否则左侧
    }

    boolean hasIgnoreFirstMove;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (canSwipe) {
            tracker.addMovement(event);
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getX();
                    downY = event.getY();
                    currentX = downX;
                    currentY = downY;
                    lastX = downX;
                    break;
                case MotionEvent.ACTION_MOVE:
                    currentX = event.getX();
                    currentY = event.getY();
                    float dx = currentX - lastX;
                    if (dx != 0f && !hasIgnoreFirstMove) {
                        hasIgnoreFirstMove = true;
                        dx = dx / dx;
                    }
                    if (getContentX() + dx < 0) {
                        setContentX(0);
                    } else {
                        setContentX(getContentX() + dx);
                    }
                    lastX = currentX;
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    tracker.computeCurrentVelocity(10000);
                    tracker.computeCurrentVelocity(1000, 20000);
                    canSwipe = false;
                    hasIgnoreFirstMove = false;
                    int mv = screenWidth * 3;
                    if (Math.abs(tracker.getXVelocity()) > mv) {
                        animateFromVelocity(tracker.getXVelocity());
                    } else {
                        if (getContentX() > screenWidth / 3) {
                            animateFinish(false);
                        } else {
                            animateBack(false);
                        }
                    }
                    tracker.recycle();
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    ObjectAnimator animator;

    public void cancelPotentialAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (animator != null) {
                animator.removeAllListeners();
                animator.cancel();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public void setContentX(float x) {
        int ix = (int) x;
        content.setX(ix);
        invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public float getContentX() {
        return content.getX();
    }

    /**
     * 弹回，不关闭，因为left是0，所以setX和setTranslationX效果是一样的
     *
     * @param withVel 使用计算出来的时间
     */
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void animateBack(boolean withVel) {
        cancelPotentialAnimation();
        animator = ObjectAnimator.ofFloat(this, "contentX", getContentX(), 0);
        int tmpDuration = withVel ? ((int) (duration * getContentX() / screenWidth)) : duration;
        if (tmpDuration < 100) {
            tmpDuration = 100;
        }
        animator.setDuration(tmpDuration);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void animateFinish(boolean withVel) {
        cancelPotentialAnimation();
        animator = ObjectAnimator.ofFloat(this, "contentX", getContentX(), screenWidth);
        int tmpDuration = withVel ? ((int) (duration * (screenWidth - getContentX()) / screenWidth)) : duration;
        if (tmpDuration < 100) {
            tmpDuration = 100;
        }
        animator.setDuration(tmpDuration);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!mActivity.isFinishing()) {
                    swipeFinished = true;
                    mActivity.finish();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        animator.start();
    }

    private final int duration = 200;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void animateFromVelocity(float v) {
        if (v > 0) {
            if (getContentX() < screenWidth / 3 && v * duration / 1000 + getContentX() < screenWidth / 3) {
                animateBack(false);
            } else {
                animateFinish(true);
            }
        } else {
            if (getContentX() > screenWidth / 3 && v * duration / 1000 + getContentX() > screenWidth / 3) {
                animateFinish(false);
            } else {
                animateBack(true);
            }
        }

    }

    //是否启用滑动关闭
    public boolean isSwipeEnabled() {
        return swipeEnabled;
    }

    //设置滑动关闭
    public void setSwipeEnabled(boolean swipeEnabled) {
        this.swipeEnabled = swipeEnabled;
    }

    //是否任意位置滑动关闭
    public boolean isSwipeAnyWhere() {
        return swipeAnyWhere;
    }

    //设置
    public void setSwipeAnyWhere(boolean swipeAnyWhere) {
        this.swipeAnyWhere = swipeAnyWhere;
    }

    //是否使用swipe关闭
    public boolean isSwipeFinished() {
        return swipeFinished;
    }

}
