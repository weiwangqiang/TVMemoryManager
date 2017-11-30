package com.example.user.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.bean.AppInfoBean;
import com.example.user.tvmanager.R;
import com.example.user.utils.SkinManager;

/**
 * Created by user on 2017/11/21.
 */

public class AppRecyclerViewAdapter extends AbsAdapter<AppInfoBean> {
    public void setOnLockListener(onLockClickListener listener) {
        this.mListener = listener;
    }

    onLockClickListener mListener ;
    private Context mCtx ;
    public AppRecyclerViewAdapter(Context mCtx , @LayoutRes int layout) {
        super(layout);
        this.mCtx = mCtx ;
    }

    @NonNull
    @Override
    public ItemViewHolder getItemViewHolder(ViewGroup parent) {
        ItemViewHolder itemViewHolder =
                new BgItemViewHolder(SkinManager.inflater(mCtx,mLayout));
        return itemViewHolder ;
    }

    @Override
    public void bindItemViewHolder(RecyclerView.ViewHolder holder, int position, final AppInfoBean appInfoBean) {
        if(!(holder instanceof BgItemViewHolder)) return;
        ((BgItemViewHolder)holder).packageName.setText(appInfoBean.getPackageName());
        ((BgItemViewHolder)holder).name.setText(appInfoBean.getProcessName() + " \n系统进程? ： "+ appInfoBean.isSystem());
        ((BgItemViewHolder)holder).lock.setText(appInfoBean.isLock() ? "解锁" :"加锁");
        ((BgItemViewHolder)holder).lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mListener)
                    mListener.OnLockClick(appInfoBean);
            }
        });
    }
    private class BgItemViewHolder extends ItemViewHolder{
        public TextView packageName;
        public TextView lock ;
        public TextView name ;

        public BgItemViewHolder(View itemView) {
            super(itemView);
            packageName = (TextView) getView(R.id.package_name);
            lock = (TextView) getView(R.id.lock);
            name = (TextView) getView(R.id.name);
        }

    }
    public interface  onLockClickListener{
        void OnLockClick(AppInfoBean appInfoBean);
    }
}
