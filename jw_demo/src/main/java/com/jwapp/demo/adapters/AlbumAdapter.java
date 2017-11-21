package com.jwapp.demo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.jwapp.demo.R;
import com.sking.lib.res.models.SKRImageModel;
import com.sking.lib.classes.SKSize;
import com.sking.lib.imageload.SKImageTask;
import com.sking.lib.imageload.SKImageWorker;
import com.sking.lib.utils.SKBaseUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ADD = 0;
    private static final int TYPE_IMAGE = 1;
    private Context mContext;
    private int stateType;//adapter显示状态，0不可编辑，1可编辑
    private LayoutInflater mLayoutInflater;
    private ArrayList<SKRImageModel> list;
    private SKImageWorker imageWorker;

    public AlbumAdapter(Context mContext, int stateType, ArrayList<SKRImageModel> list) {
        this.mContext = mContext;
        this.stateType = stateType;
        this.list = list;
        imageWorker = new SKImageWorker(mContext);
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount() {
        return list.size() == 0 ? 1 : list.size()+1;
    }


    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_ADD : TYPE_IMAGE;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        int type = getItemViewType(position);
        RecyclerView.ViewHolder holader = null;
        switch (type) {
            case TYPE_ADD:
                holader = new AddButtHolder(mLayoutInflater.inflate(R.layout.adapter_album_add_butt, viewGroup, false));
                break;
            case TYPE_IMAGE:
                holader = new ItemHolder(mLayoutInflater.inflate(R.layout.adapter_album_image, viewGroup, false));
                break;
        }
        return holader;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof AddButtHolder) {
            setDataAdd((AddButtHolder) viewHolder, position);
        } else {
            setDataImage(position, (ItemHolder) viewHolder);
        }
    }

    /**
     * ItemClick的回调接口
     */
    public interface OnItemClickLitener {
        void onItemClick(int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private void setDataAdd(AddButtHolder holder, final int position) {
        if (stateType == 0) {
            holder.addButton.setVisibility(View.GONE);
        } else {
            holder.addButton.setVisibility(View.VISIBLE);
        }
        holder.addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickLitener.onItemClick(position);
            }
        });
    }

    private void setDataImage(final int itemPosition, ItemHolder holder) {

        int position = itemPosition - 1;
        holder.imageView.setBackgroundResource(R.mipmap.img_white_swan);
        if (SKBaseUtil.isNull(list.get(position).getId())) {
            SKImageTask imageTask = new SKImageTask(holder.imageView, list.get(position).getImgurl(), mContext, new SKSize(200, 200));
            imageWorker.loadImage(imageTask);
        } else {
            URL url;
            try {
                url = new URL(list.get(position).getImgurl());
                SKImageTask loadtask = new SKImageTask(holder.imageView, url, mContext, new SKSize(200, 200));
                imageWorker.loadImage(loadtask);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        holder.imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickLitener.onItemClick(itemPosition);
            }
        });
    }

    /**
     * addbuttview
     */
    public class AddButtHolder extends RecyclerView.ViewHolder {
        private Button addButton;

        public AddButtHolder(View itemView) {
            super(itemView);
            addButton = (Button) itemView.findViewById(R.id.album_add_button);
        }
    }

    /**
     * imgView
     */
    public class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ItemHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.album_item_image_view);
        }
    }
}
