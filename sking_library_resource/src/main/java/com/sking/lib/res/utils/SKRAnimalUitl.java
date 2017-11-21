package com.sking.lib.res.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;

import com.sking.lib.res.R;


/**
 * 自定义动画工具类
 * Created by 谁说青春不能错 on 2016/4/26.
 */
public class SKRAnimalUitl {

    private static SKRAnimalUitl animalUitl = new SKRAnimalUitl();

    synchronized public static SKRAnimalUitl getInstance() {
        return animalUitl;
    }

    /**
     * activity 淡入淡出
     *
     * @param context
     */
    public static void activityFadeInFadeOut(Context context) {
        ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(R.anim.skr_animal_fade_in, R.anim.skr_animal_fade_out);
    }

    /**
     * activity 淡入
     *
     * @param context
     */
    public static void activityFadeIn(Context context) {
        ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(R.anim.skr_animal_fade_in, 0);
    }

    /**
     * activity 淡出
     *
     * @param context
     */
    public static void activityFadeOut(Context context) {
        ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(0, R.anim.skr_animal_fade_out);
    }

    /**
     * activity 底部进出
     *
     * @param context
     */
    public static void activityBottonInOut(Context context) {
        ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(R.anim.skr_animal_bottom_in, R.anim.skr_animal_bottom_out);
    }

    /**
     * activity 底部进入
     *
     * @param context
     */
    public static void activityBottonIn(Context context) {
        ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(R.anim.skr_animal_bottom_in, 0);
    }

    /**
     * activity 底部退出
     *
     * @param context
     */
    public static void activityBottonOut(Context context) {
        ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(0, R.anim.skr_animal_bottom_out);
    }

    /**
     * activity 左边进入退出
     *
     * @param context
     */
    public static void activityLeftInOut(Context context) {

    }

    /**
     * activity 左边进入右边退出
     *
     * @param context
     */
    public static void activityLeftIn(Context context,boolean isFinish) {
        if(isFinish)
            ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(0, R.anim.skr_animal_right_in);
    }

    /**
     * activity 左边退出
     *
     * @param context
     */
    public static void activityLeftOut(Context context) {

    }

    /**
     * activity 右边进入退出
     *
     * @param context
     */
    public static void activityRightInOut(Context context) {

    }

    /**
     * activity 右边进入
     *
     * @param context
     */
    public static void activityRightIn(Context context) {

    }

    /**
     * activity 右边退出
     *
     * @param context
     */
    public static void activityRightOut(Context context) {

    }

    /**
     * activity 顶部进入退出
     *
     * @param context
     */
    public static void activityTopInOut(Context context) {
    }

    /**
     * activity 顶部进入
     *
     * @param context
     */
    public static void activityTopIn(Context context) {
    }

    /**
     * activity 顶部退出
     *
     * @param context
     */
    public static void activityTopOut(Context context) {
    }

    /**
     * View 底部进入
     *
     * @param context
     */
    public void viewBottomIn(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.skr_animal_center_dismiss);
        animation.setAnimationListener(animationListener);
        view.startAnimation(animation);
    }

    /**
     * View 淡入
     *
     * @param context
     */
    public void viewBottomOut(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.skr_animal_center_dismiss);
        animation.setAnimationListener(animationListener);
        view.startAnimation(animation);
    }

    /**
     * View 淡入
     *
     * @param context
     */
    public void viewFadeIn(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.skr_animal_center_dismiss);
        animation.setAnimationListener(animationListener);
        view.startAnimation(animation);
    }

    /**
     * View 淡出
     *
     * @param context
     */
    public void viewFadeOut(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.skr_animal_center_dismiss);
        animation.setAnimationListener(animationListener);
        view.startAnimation(animation);
    }

    /**
     * View 中心进入
     *
     * @param context
     */
    public void viewCenterIn(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.skr_animal_center_dismiss);
        animation.setAnimationListener(animationListener);
        view.startAnimation(animation);
    }

    /**
     * View 中心退出
     *
     * @param context
     */
    public void viewCenterOut(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.skr_animal_center_dismiss);
        animation.setAnimationListener(animationListener);
        view.startAnimation(animation);
    }

    /**
     * listview加载的效果
     */
    public static LayoutAnimationController getListAnimTranslate() {
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        set.addAnimation(animation);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(800);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        return controller;
    }


    private Animation.AnimationListener animationListener = new Animation.AnimationListener()
    {

        @Override
        public void onAnimationStart(Animation animation) {
            animalStart();
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            animalEnd();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            animalRepeat();
        }
    };

    /**
     * 动画开始回调
     * */
    private void animalStart()
    {

    }
    /**
     * 动画结束回调
     * */
    private void animalEnd()
    {

    }
    /**
     * 动画重复回调
     * */
    private void animalRepeat()
    {

    }
}
