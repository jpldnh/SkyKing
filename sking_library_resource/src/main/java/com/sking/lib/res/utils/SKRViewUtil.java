package com.sking.lib.res.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sking.lib.res.interfaces.SKRInterfaces;
import com.sking.lib.utils.SKTransfromUtil;

/**
 * 时间相关工具类
 * Created by 谁说青春不能错 on 2016/4/25.
 */
public class SKRViewUtil {

    //当前触摸点相对于屏幕的坐标
    private int mCurrentInScreenX;
    private int mCurrentInScreenY;
    private int press_X, press_Y, move_X, move_Y;
    private View mDownView;
    private int mDownPosition;
    private static SKRViewUtil viewUtil = new SKRViewUtil();

    synchronized public static SKRViewUtil getInstance() {
        return viewUtil;
    }

    /*-----------------TextView start-------------------------*/

    /**
     * 设置 TextView 设置下划线
     * param textView
     **/
    public static void setTextViewUnderLine(TextView textView)
    {
        textView.getPaint().setFlags(Paint.DEV_KERN_TEXT_FLAG); //下划线
        textView.getPaint().setAntiAlias(true);//抗锯齿
    }

    /**
     * 设置 TextView 设置中划线
     * param textView
     **/
    public static void setTextViewMiddleLine(TextView textView)
    {
        textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        textView.getPaint().setAntiAlias(true);//抗锯齿
    }

    /**
     * 设置 TextView 的drawable方向，如textview drawleft,drawright
     * param textView
     * param index 资源
     * param oretation 方向
     **/
    public static void setTextViewOreationDrawable(Context mContext, TextView textView, int res_index, int oretation) {
        Drawable o_Drawable = mContext.getResources().getDrawable(res_index);
        o_Drawable.setBounds(0, 0, o_Drawable.getMinimumWidth(), o_Drawable.getMinimumHeight());
        switch (oretation) {
            case Gravity.TOP:
                textView.setCompoundDrawables(o_Drawable, null, null, null);
                break;
            case Gravity.BOTTOM:
                textView.setCompoundDrawables(o_Drawable, null, null, null);
                break;
            case Gravity.LEFT:
                textView.setCompoundDrawables(o_Drawable, null, null, null);
                break;
            case Gravity.RIGHT:
                textView.setCompoundDrawables(null, null, o_Drawable, null);
                break;

        }
    }

    /**
     * 设置textView 部分文字大小颜色，根据回车换行
     * param strMsg
     * param textCorlor
     * param textSize
     **/
    public static SpannableString getSpannableString(Context context, String strMsg, int textCorlor, int textSize) {
        int postion = strMsg.indexOf("\n");
        SpannableString spanString = new SpannableString(strMsg);
        spanString.setSpan(new ForegroundColorSpan(context.getResources().getColor(textCorlor)), postion, strMsg.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanString.setSpan(new AbsoluteSizeSpan(SKTransfromUtil.dipTopx(context, textSize)), postion, strMsg.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    /**
     * 设置textView 部分文字大小颜色
     * param strMsg
     * param textCorlor
     * param textSize
     * params startposition 开始位置
     * paarms endposition 结束位置
     **/
    public static SpannableString getSpannableString(Context context, String strMsg, int startPosition, int endPosition, int textCorlor, int textSize) {
        SpannableString spanString = new SpannableString(strMsg);
        spanString.setSpan(new ForegroundColorSpan(context.getResources().getColor(textCorlor)), startPosition, endPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanString.setSpan(new AbsoluteSizeSpan(SKTransfromUtil.dipTopx(context, textSize)), startPosition, endPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }
 /*-----------------TextView end-------------------------*/

  /*-----------------ImageView start-------------------------*/

    /**
     * 将缓存图片转化为bitmap
     * <p>
     * kind可以为Thumbnails.MINI_KIND或Humbnails.MICRO_KIND,也可以为-
     */
    public static Bitmap getBitMap(String filePath, int kind) {
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(filePath, kind);
        return bitmap;
    }
 /*-----------------ImageView end-------------------------*/

  /*-----------------ListView start-------------------------*/

    /**
     * 设置scrollview中listview高度
     */
    public void setListViewChildrenHeight(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    //监听listview 左右滑动
    public void setListViewGustrueListener(final ListView listView, final SKRInterfaces.SKROnListViewGestureListener listViewItemListener) {
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                // 获取相对屏幕的坐标，即以屏幕左上角为原点
                mCurrentInScreenX = (int) event.getRawX();
                mCurrentInScreenY = (int) event.getRawY();
                if (listView.getCount() == 0)
                    return false;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        press_X = (int) event.getRawX();
                        press_Y = (int) event.getRawY();

                        try {
                            Rect rect = new Rect();
                            int childCount = listView.getChildCount();
                            int[] listViewCoords = new int[2];
                            listView.getLocationOnScreen(listViewCoords);
                            int x = (int) event.getRawX() - listViewCoords[0];
                            int y = (int) event.getRawY() - listViewCoords[1];
                            View child;
                            for (int i = 0; i < childCount; i++) {
                                child = listView.getChildAt(i);
                                child.getHitRect(rect);
                                if (rect.contains(x, y)) {
                                    mDownView = child; // This is your down
                                    break;
                                }
                            }
                            if (mDownView != null) {
                                mDownPosition = listView.getPositionForView(mDownView);
                            }
                        } catch (NullPointerException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        move_X = (int) event.getRawX() - press_X;
                        move_Y = (int) event.getRawY() - press_Y;
                        break;
                    case MotionEvent.ACTION_UP:

                        if (isMoved()) {
                            if (move_X > 200) {
                                listViewItemListener.onRightScrollListener(mDownView, mDownPosition);
                            } else if (move_X < -200) {
                                listViewItemListener.onLeftScrollListener(mDownView, mDownPosition);
                            }
                        } else {
                            listViewItemListener.onItemDownListener(mDownView, mDownPosition);
                        }
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 判断是否移动
     */
    private boolean isMoved() {
        // 允许有5的偏差 在判断是否移动的时候
        if (Math.abs(press_X - mCurrentInScreenX) <= 10 && Math.abs(press_Y - mCurrentInScreenY) <= 10)
            return false;
        else
            return true;
    }
    /*-----------------ListView end-------------------------*/

}
