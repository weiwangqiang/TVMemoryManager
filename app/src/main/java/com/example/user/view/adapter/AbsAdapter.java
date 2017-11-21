package com.example.user.view.adapter;

/**
 * Created by user on 2017/11/21.
 */

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbsAdapter<T> extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String TAG = "AbsAdapter";
    protected int mLayout;
    protected final int TYPE_ITEM = 1;

    public int getDataSize() {
      return (data == null ? 0 : data.size()) ;
    }

    private List<T> data;

    public AbsAdapter(@LayoutRes int layout) {
        mLayout = layout;
    }

    public void clearAll() {
        if (data == null) return;
        data.clear();
    }

    public void upDate(List<T> list) {
        data = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    public void appendDate(List<T> list) {
        appendDate(list, list.size());

    }

    public void appendDate(List<T> list, int position) {
        if (data == null) {
            data = new ArrayList<>(list);
        } else {
            data.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ITEM:
                return getItemViewHolder(parent);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            bindItemViewHolder(holder, position, data.get(position));
    }

    @Override
    public int getItemViewType(int position) {
            return TYPE_ITEM;
    }


    @Override
    public int getItemCount() {
        return data.size() ;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public View root;
        public Map<Integer, View> views;

        public ItemViewHolder(View itemView) {
            super(itemView);
            root = itemView;
            views = new HashMap<>();
        }

        protected View getView(int id) {
            View v = views.get(id);
            if (v == null)
                v = root.findViewById(id);
            return v;
        }
    }
    /**
     * 获取ItemViewHolder
     *
     * @return
     */

    public abstract
    @NonNull
    ItemViewHolder getItemViewHolder(ViewGroup parent);


    /**
     * 绑定Item View
     * 可以在这里设置点击事件之类的
     *
     * @param holder
     * @param position
     */
    public abstract void bindItemViewHolder(RecyclerView.ViewHolder holder, int position, T t);

}
