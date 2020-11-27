package com.btime.common.widget;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.btime.common.R;
import com.btime.common.widget.recycler.RecyclerAdapter;

import java.util.LinkedList;
import java.util.List;


public class GalleyView extends RecyclerView {
    private static final int LOADER_ID = 0x0100;
    private static final int MAX_IMAGE_COUNT = 3;//最大选中图片数量
    private LoaderCallback mLoaderCallback = new LoaderCallback();
    private Adapter mAdapter = new Adapter();
    private List<Image> mSelectedImages = new LinkedList<>();

    public GalleyView(Context context) {
        super(context);
        init();
    }

    public GalleyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(new GridLayoutManager(getContext(), 4));
        setAdapter(mAdapter);
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Image>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Image image) {
                //Cell点击操作，如果点击是允许的更新对应Cell的状态
                //然后更新界面，如果不允许点击（已经达到最大的选中数量）就不允许刷新
                if (onItemSelectClick(image)) {
                    holder.updateData(image);
                }
            }
        });
    }

    public int setup(LoaderManager loaderManager){
        loaderManager.initLoader(LOADER_ID,null,mLoaderCallback);
        return LOADER_ID;
    }

    /**
     * Cell点击的具体逻辑
     * @param image Image
     * @return True，代表进行了数据更改，需要刷新；反之无需刷新
     */
    private boolean onItemSelectClick(Image image) {
        //是否需要刷新
        boolean notifyRefresh;
        if (mSelectedImages.contains(image)) {
            //如果之前在就进行移除
            mSelectedImages.remove(image);
            image.isSelect = false;
            //状态已经改变需要更新
            notifyRefresh = true;
        } else {
            if (mSelectedImages.size() >= MAX_IMAGE_COUNT) {
                //Toast提示
                notifyRefresh = false;
            } else {
                mSelectedImages.add(image);
                image.isSelect = true;
                notifyRefresh = true;
            }
        }

        //如果数据有更改
        //需要告诉监听者数据选择改变了
        if (notifyRefresh)
            notifySelectChanged();
        return true;
    }

    /**
     * 得到选中的图片的全部地址
     * @return 返回一个数组
     */
    public String[] getSelectedPath() {
        String[] paths = new String[mSelectedImages.size()];
        int index = 0;
        for (Image image : mSelectedImages) {
            paths[index++] = image.path;
        }
        return paths;
    }

    /**
     * 清空选中的图片
     */
    public void clear(){
        for(Image image:mSelectedImages){
            //一定要重置状态
            image.isSelect = false;
        }
        mSelectedImages.clear();
        //通知更新
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 通知选中状态改变
     */
    private void notifySelectChanged() {

    }

    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>{

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            //创建一个Loader
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            //当Loader加载完成式
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            //当Loader销毁或重置时
        }
    }

    private static class Image {
        int id;//数据的id
        String path;//图片的路径
        long date;//图片的创建日期
        boolean isSelect;//是否选中

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Image image = (Image) obj;
            return path != null ? path.equals(image.path) : image.path == null;
        }

        @Override
        public int hashCode() {
            return path != null ? path.hashCode() : 0;
        }
    }

    private class Adapter extends RecyclerAdapter<Image> {

        @Override
        public void update(Image image, ViewHolder<Image> holder) {

        }

        @Override
        protected int getItemViewType(int position, Image image) {
            return R.layout.cell_galley;
        }

        @Override
        protected ViewHolder<Image> onCreateViewHolder(View root, int viewType) {
            return new GalleyView.ViewHolder(root);
        }
    }

    private class ViewHolder extends RecyclerAdapter.ViewHolder<Image> {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Image image) {

        }
    }
}
