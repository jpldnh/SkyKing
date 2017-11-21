package com.jwapp.demo;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.jwapp.demo.adapters.AlbumAdapter;
import com.jwapp.demo.adapters.CycleViewPaperAdapter;
import com.sking.lib.base.SKActivity;
import com.sking.lib.interfaces.SKInterfaceListtener;
import com.sking.lib.net.SKNetTask;
import com.sking.lib.res.activitys.SKRShowLargePicActivity;
import com.sking.lib.res.models.SKRImageModel;
import com.sking.lib.res.utils.SKRPhotoSelectUtil;
import com.sking.lib.res.views.SKRCycleViewPager;
import com.sking.lib.res.views.SKRScrollViewSinglePicker;

import java.util.ArrayList;


/**
 * 轮播图，横向album
 * bg jw,2016-04-20
 **/
public class CycleViewActivity extends SKActivity {

    private SKRCycleViewPager cycleViewPaper;
    private CycleViewPaperAdapter cycleAdapter;
    private RecyclerView mRecyclerView;
    private AlbumAdapter mAdapter;
    private SKRScrollViewSinglePicker bootomPupuWindow;
    private SKRPhotoSelectUtil photoUtil;
    private ArrayList<SKRImageModel> list = new ArrayList<SKRImageModel>();
    private int cameraTyoe = 0, albumTyoe = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cycle_view);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected boolean onKeyBack() {
        return false;
    }

    @Override
    protected boolean onKeyMenu() {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        Bundle bundle = null;
        if (data != null) {
            bundle = data.getExtras();
        }
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case 1:// 相册选择图片回调
                if (albumTyoe == 0) {
                    photoUtil.dealAlbum(data);
                } else {
                    photoUtil.dealAlbumArray(data);
                }
                break;
            case 2:// 拍照回调
                if (cameraTyoe == 0) {
                    photoUtil.dealCamera();//拍照可编辑不压缩
                } else {
                    photoUtil.dealCameraCompress();//不编辑直接压缩图片
                }
                break;
            case 3:// 裁剪回调
                SKRImageModel albumModel = new SKRImageModel();
                albumModel.setImgurl(photoUtil.getTempPath());
                albumModel.setImgurlbig(photoUtil.getTempPath());
                list.add(0, albumModel);
                mAdapter.notifyDataSetChanged();
                Log.i("picpath_photUtile_getTempPath",photoUtil.getTempPath());
                break;
            default:
                break;
        }
    }

    @Override
    protected void getExras() {
        photoUtil = SKRPhotoSelectUtil.getInstance(mContext);
    }

    @Override
    protected void findView() {
        cycleViewPaper = (SKRCycleViewPager) findViewById(R.id.cycle_view_pager);
        mRecyclerView = (RecyclerView) findViewById(R.id.horizontal_recyclerview);
    }

    @Override
    protected void setListener() {
        cycleAdapter = new CycleViewPaperAdapter(mContext);
        cycleViewPaper.setAdapter(cycleAdapter);

        mRecyclerView.setHasFixedSize(true);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new AlbumAdapter(mContext, 1, list);
        mAdapter.setOnItemClickLitener(new AlbumAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(int position) {
//                Toast.makeText(CycleViewActivity.this, "点击了"+position, Toast.LENGTH_SHORT).show();
                if (position == 0) {
                    showBottomPopu();
//                    showBottomPopuTakePhoto();
                } else {
                    Intent sIt = new Intent(mContext, SKRShowLargePicActivity.class);
                    sIt.putExtra("position", position - 1);
                    sIt.putExtra("images", list);
                    mContext.startActivity(sIt);
                }
            }
        });
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(0));
        mRecyclerView.setAdapter(mAdapter);

        photoUtil.setOnBackClick(onBackClickForString);

    }

    @Override
    protected void networkRequestBefore(SKNetTask netTask) {

    }

    @Override
    protected void networkRequestAfter(SKNetTask netTask) {

    }

    @Override
    protected void networkRequestSuccess(SKNetTask netTask, Object result) {

    }


    //设置item间距
    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (parent.getChildPosition(view) != 0)
                outRect.top = space;
        }
    }

    //show bottompopuwindow button动态增加
    public void showBottomPopu() {
        bootomPupuWindow = new SKRScrollViewSinglePicker(CycleViewActivity.this, popuwidnwavatarOnClick, new String[]{"拍照编辑", "拍照压缩", "从相册选择可编辑", "相册多选"});
        //显示窗口
        bootomPupuWindow.showRightAnglelayoutStyle(cycleViewPaper, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private View.OnClickListener popuwidnwavatarOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            bootomPupuWindow.dismiss();
            switch (v.getId()) {
                case 0:
                    cameraTyoe = 0;
                    photoUtil.camera();
                    break;
                case 1:
                    cameraTyoe = 1;
                    photoUtil.camera();
                    break;
                case 2:
                    albumTyoe = 0;
                    photoUtil.album();
                    break;
                case 3:
                    albumTyoe = 1;
                    photoUtil.albumArray();
                    break;
            }
//            Toast.makeText(CycleViewActivity.this, v.getId() + "", Toast.LENGTH_SHORT).show();
        }
    };

    public SKInterfaceListtener.SKOnStringBackClickListener onBackClickForString = new SKInterfaceListtener.SKOnStringBackClickListener() {
        @Override
        public void onBackResult(String string) {
            SKRImageModel albumModel = new SKRImageModel();
            albumModel.setImgurl(string);
            albumModel.setImgurlbig(string);
            list.add(0, albumModel);
            mAdapter.notifyDataSetChanged();
        }
    };


}
