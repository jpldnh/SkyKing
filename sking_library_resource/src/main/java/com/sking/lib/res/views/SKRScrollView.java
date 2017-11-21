package com.sking.lib.res.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

import com.sking.lib.res.interfaces.SKRInterfaces;


/**
 * 具有阻尼效果的ScrollView，可以监听上下滑动
 * @Description TODO
 * @Author xieyue 
 * @Date Create at 2015年1月29日 上午11:11:52
 */
public class SKRScrollView extends ScrollView {
	  /** 阻尼系数,越小阻力就越大. */
    private static final float SCROLL_RATIO = 0.2f;

    /** 触发下拉恢复回调方法的距离. */
    private static final int TURN_DISTANCE = 50;

    /**背景view */
    private View backGroundView;

    /** ScrollView的content view. */
    private View contentView;

    /**scrollView的子控件，可以是GridView、listView等**/
    private View childView;

    /** ScrollView的content view矩形. */
    private Rect mContentRect = new Rect();

    /** 首次点击的Y坐标. */
    private float mTouchDownY;

    /** 是否关闭ScrollView的滑动. */
    private boolean mEnableTouch = false;

    /** 是否开始移动. */
    private boolean isMoving = false;

    /** 是否移动到顶部位置. */
    private boolean isTop = false;

    /** 头部图片初始顶部和底部坐标. */
    private int mInitTop, mInitBottom;

    /** 头部图片拖动时的顶部和底部坐标. */
    private int mCurrentTop, mCurrentBottom;

    /** 子控件初始顶部和底部坐标. */
    private int mChildInitTop, mChildInitBottom;

    /** 子控件拖动时顶部和底部坐标. */
    private int mChildTop, mChildBottom;

    /** 头部下拉恢复的监听器  */
    private OnHeaderRefreshListener mOnHeaderRefreshListener;

    /**
     * 主要是用在用户手指离开MyScrollView，MyScrollView还在继续滑动，我们用来保存Y的距离，然后做比较
     */
    private int lastScrollY;

    /** ScrollView的滑动监听器 */
    private OnScrollListener onScrollListener;

    /**
     * 上下滑动监听
     * */
    private SKRInterfaces.SKROnScrollChangedListener onScrollChangedListener;

    private enum State {
        /**顶部*/
        UP,
        /**底部*/
        DOWN,
        /**正常*/
        NORMAL
    }

    /** 状态. */
    private State mState = State.NORMAL;

    public SKRScrollView(Context context) {
        super(context);
        init(context, null);
    }

    public SKRScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SKRScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @SuppressLint("NewApi")
	private void init(Context context, AttributeSet attrs) {
    	//为视图设置over-scroll模式。有效的over-scroll模式有:
    	//OVER_SCROLL_ALWAYS（缺省值），
    	//OVER_SCROLL_IF_CONTENT_SCROLLS（只允许当视图内容大过容器时，进行over-scrolling）,
    	//OVER_SCROLL_NEVER。只有当视图可以滚动时，此项设置才起作用。
        setOverScrollMode(OVER_SCROLL_NEVER);
    }
    
    /** 
     * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中 
     */  
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
            int scrollY = SKRScrollView.this.getScrollY();
            //此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息  
            if(lastScrollY != scrollY){
                lastScrollY = scrollY;  
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
            }  
            if(onScrollListener != null){
                onScrollListener.onScroll(scrollY);
            }  
        };  
  
    }; 

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            contentView = getChildAt(0);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (getScrollY() == 0) {
            isTop = true;//滑动到顶部
        }
        if (this.onScrollChangedListener != null) {
            onScrollChangedListener.onScrollChanged(t, oldt);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	//初次点击的时候获取坐标
        if (backGroundView != null && childView != null && 
        		ev.getAction() == MotionEvent.ACTION_DOWN) {
            mTouchDownY = ev.getY();
            mCurrentTop = mInitTop = backGroundView.getTop();
            mCurrentBottom = mInitBottom = backGroundView.getBottom();
            
            mChildTop = mChildInitTop = childView.getTop();
            mChildBottom = mChildInitBottom = childView.getBottom();
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(onScrollListener != null){
            onScrollListener.onScroll(lastScrollY = this.getScrollY());  
        } 
        if (childView != null) {
            doTouchEvent(ev);
        }

        // 禁止控件本身的滑动.
        return mEnableTouch || super.onTouchEvent(ev);
    }

    /**
     * 触摸事件处理
     *
     * @param event
     */
    private void doTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
        case MotionEvent.ACTION_DOWN:
            break;
        
            case MotionEvent.ACTION_MOVE:
                doActionMove(event);
                break;

            case MotionEvent.ACTION_UP:
            	 handler.sendMessageDelayed(handler.obtainMessage(), 5);    
                // 回滚动画
                if (isNeedAnimation()) {
                    rollBackAnimation();
                }

                if (isTop) {
                    mState = State.NORMAL;
                }

                isMoving = false;
                mEnableTouch = false;
                break;

            default:
                break;
        }
    }

    /**
     * 执行移动动画
     *
     * @param event
     */
    private void doActionMove(MotionEvent event) {
    	
        // 当滚动到顶部时，将状态设置为正常，避免先向上拖动再向下拖动到顶端后首次触摸不响应的问题
            
            // 滑动经过顶部初始位置时，修正Touch down的坐标为当前Touch点的坐标
            if (isTop) {
            	mState = State.DOWN;
                isTop = false;
                mTouchDownY = event.getY();
        }
        float deltaY = event.getY() - mTouchDownY;

        // 对于首次Touch操作要判断方位：UP OR DOWN
        if (deltaY < 0 && mState == State.DOWN) {
            mState = State.UP;
        } else if (deltaY > 0 && mState == State.DOWN) {
            mState = State.DOWN;
        }

        if (mState == State.UP) {
            deltaY = deltaY < 0 ? deltaY : 0;

            isMoving = false;
            mEnableTouch = false;

        } else if (mState == State.DOWN) {
            if (getScrollY() <= deltaY) {
                mEnableTouch = true;
                isMoving = true;
            }
            deltaY = deltaY < 0 ? 0 : deltaY;
        }

        if (isMoving) {
            // 初始化content view矩形
            if (mContentRect.isEmpty()) {
                // 保存正常的布局位置
                mContentRect.set(contentView.getLeft(), contentView.getTop(), contentView.getRight(),
                        contentView.getBottom());
            }

            // 计算header移动距离(手势移动的距离*阻尼系数*0.5)
            float headerMoveHeight = deltaY * 0.5f * SCROLL_RATIO;
            mCurrentTop = (int) (mInitTop + headerMoveHeight);
            mCurrentBottom = (int) (mInitBottom + headerMoveHeight);
            
            // 计算子控件移动距离(手势移动的距离*阻尼系数*0.5)
            float childMoveHeight = deltaY * SCROLL_RATIO;
            mChildTop = (int) (mChildInitTop + childMoveHeight);
            mChildBottom = (int) (mChildInitBottom + childMoveHeight);

            // 计算content移动距离(手势移动的距离*阻尼系数)
            float contentMoveHeight = deltaY * SCROLL_RATIO;

            // 修正content移动的距离，避免超过header的底边缘
            int headerBottom = mCurrentBottom;
            int top = (int) (mContentRect.top + contentMoveHeight);
            int bottom = (int) (mContentRect.bottom + contentMoveHeight);

            if (top <= headerBottom) {
                // 移动content view
            	contentView.layout(mContentRect.left, top, mContentRect.right, bottom);

                // 移动header view
                backGroundView.layout(backGroundView.getLeft(), mCurrentTop, backGroundView.getRight(), mCurrentBottom);
                
                childView.layout(childView.getLeft(), mChildTop, childView.getRight(), mChildBottom);
            }
        }
    }

    private void rollBackAnimation() {
    	if(backGroundView == null) {
    		return;
    	}
        TranslateAnimation tranAnim = new TranslateAnimation(0, 0,Math.abs(mInitTop - mCurrentTop), 0);
        tranAnim.setDuration(200);
        backGroundView.startAnimation(tranAnim);
        backGroundView.layout(backGroundView.getLeft(), mInitTop, backGroundView.getRight(), mInitBottom);

        TranslateAnimation tranAnim_2 = new TranslateAnimation(0, 0,Math.abs(mChildInitTop - mChildTop), 0);
        tranAnim_2.setDuration(200);
        childView.startAnimation(tranAnim_2);
        childView.layout(childView.getLeft(), mChildInitTop, childView.getRight(), mChildInitBottom);
        
        // 开启移动动画
        TranslateAnimation innerAnim = new TranslateAnimation(0, 0, contentView.getTop(), mContentRect.top);
        innerAnim.setDuration(200);
        contentView.startAnimation(innerAnim);
        contentView.layout(mContentRect.left, mContentRect.top, mContentRect.right, mContentRect.bottom);

        mContentRect.setEmpty();
        // 回调监听器
        if (mCurrentTop > mInitTop + TURN_DISTANCE && mOnHeaderRefreshListener != null){
        	new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					mOnHeaderRefreshListener.onHeaderRefresh();
				}
			},200);
             	
        }
    }

    /**
     * 是否需要开启动画
     */
    private boolean isNeedAnimation() {
        return !mContentRect.isEmpty();
    }
    
	public void setchildView(View childView) {
		this.childView = childView;
	}

	public void setbackGroundView(View backGroundView) {
		this.backGroundView = backGroundView;
	}
    
    /**
     * 头部恢复的回调接口
     */
    public interface OnHeaderRefreshListener {
        public void onHeaderRefresh();  
    }  
    
    /**
     * 设置头部恢复接口
     * @param headerRefreshListener
     */
    public void setOnHeaderRefreshListener(OnHeaderRefreshListener headerRefreshListener) {
        mOnHeaderRefreshListener = headerRefreshListener;  
    }  
    
    /** 
     * 设置滚动接口 
     * @param onScrollListener 
     */  
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;  
    }
    
    /** 
     * 滚动的回调接口 
     */  
    public interface OnScrollListener{
        public void onScroll(int scrollY);  
    }
    /**
     * 上下滑动监听接口
     */
    public void setOnScrollChangedListener(SKRInterfaces.SKROnScrollChangedListener onScrollChangedListener) {
        this.onScrollChangedListener = onScrollChangedListener;
    }

}
