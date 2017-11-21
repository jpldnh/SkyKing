package com.skingapp.commen.umen;

import android.app.Activity;
import android.widget.Toast;

import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.ShareBoardlistener;

/**
 * Created by Im_jingwei on 2017/9/20.
 *
 * 使用时将本类复制，不做成依赖包，每个项目分享的平台可能不一样
 * 每个项目中也可能不止一个地方有分享
 * 步骤：先初始化分享initSHare,需要自定义回调结果调用setShareResultListener反之不则需要，最后调用show四种样式自己选
 */

public class UmenShare {

    private static Activity mContext;
    private static ShareAction mShareAction;
    private static UMShareListener umShareResultListener;

    /**
     * @param
     * @time 2017/9/22  9:35
     * @author Im_jingwei
     * @des 初始化分享
     */
    public static void initShare(Activity context) {
        Config.DEBUG = true;
        mContext = context;
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(mContext).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.SINA,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.TENCENT, SHARE_MEDIA.MORE);
    }

    /**
     *  @time 2017/9/22  10:08
     *  @author Im_jingwei
     *  @param shareTitle  分享标题
     *  @param shareContent  分享正文
     *  @param shareUrl   分享连接
     *  @param shareIcon  分享图标
     *  @des 显示弹窗
     */
    public static void show(String shareTitle, String shareContent, String shareUrl, int shareIcon) {
        if (mShareAction == null) {
            Toast.makeText(mContext, "请先初始化UmenShare", Toast.LENGTH_LONG).show();
            return;
        }
        UMWeb web = new UMWeb(shareUrl);//分享的连接
        web.setTitle(shareTitle);//标题
        web.setDescription(shareContent);//内容
        web.setThumb(new UMImage(mContext, shareIcon));//图标
        setShareResultListener(null);
        mShareAction.setShareboardclickCallback(new ShareResultListener(web));
//        mShareAction.addButton("showword", "keyword", "icon", "Grayicon");//添加自定义按钮
        ShareBoardConfig config = new ShareBoardConfig();
        config.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_CENTER);//显示方式SHAREBOARD_POSITION_CENTER中心弹窗，SHAREBOARD_POSITION_BOTTOM底部弹窗
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE); // 圆角背景，BG_SHAPE_NONE无背景
        mShareAction.open(config);//b不传config为默认样式
    }

    /**
     * @param listener
     * @time 2017/9/22  9:39
     * @author Im_jingwei
     * @des 默认本类中的分享回调，若activity中需要继承UMShareListener接口即可
     */
    public static void setShareResultListener(UMShareListener listener) {
        if (listener == null)
            umShareResultListener = umshareListener;
        else
            umShareResultListener = listener;
    }

    /**
     *  @time 2017/9/22  10:07
     *  @author Im_jingwei
     *  @des 分享按钮点击事件
     */
    private static class ShareResultListener implements ShareBoardlistener {

        private UMWeb umWeb;
        ShareResultListener(UMWeb web) {
            this.umWeb = web;
        }

        @Override
        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {

            if (umWeb == null) {
                Toast.makeText(mContext, "请配置分享信息！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (snsPlatform.mShowWord.equals("showword")) {//自定义按钮
                Toast.makeText(mContext, "拓展按钮", Toast.LENGTH_LONG).show();
            } else if (share_media == SHARE_MEDIA.SMS) {//短信
                new ShareAction(mContext).withText(umWeb.getTitle()).setPlatform(share_media).setCallback(umShareResultListener).share();
            } else {
                new ShareAction(mContext).withMedia(umWeb).setPlatform(share_media).setCallback(umShareResultListener).share();
            }
        }
    }

    /**
     *  @time 2017/9/22  10:07
     *  @author Im_jingwei
     *  @param
     *  @des 分享结果回调
     */
    private static UMShareListener umshareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            if (share_media.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mContext, share_media + " 收藏成功！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, share_media + " 分享成功！", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Toast.makeText(mContext, share_media + " 分享失败！", Toast.LENGTH_SHORT).show();
            if (throwable != null) {
                Log.d("throw", "throw:" + throwable.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Toast.makeText(mContext, share_media + " 用户取消分享", Toast.LENGTH_SHORT).show();
        }
    };

}
