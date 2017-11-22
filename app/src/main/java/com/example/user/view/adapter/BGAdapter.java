package com.example.user.view.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.bean.AppInfo;
import com.example.user.tvmanager.R;
import com.example.user.utils.SkinManager;

/**
 * Created by user on 2017/11/21.
 */

public class BgAdapter extends AbsAdapter<AppInfo> {
    public void setListener(onItemClickListener listener) {
        this.listener = listener;
    }

    onItemClickListener listener ;
    private Context mCtx ;
    public BgAdapter(Context mCtx , @LayoutRes int layout) {
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
    public void bindItemViewHolder(RecyclerView.ViewHolder holder, int position, final AppInfo appInfo) {
        if(!(holder instanceof BgItemViewHolder)) return;
        ((BgItemViewHolder)holder).packageName.setText(appInfo.getPackageName());
        ((BgItemViewHolder)holder).name.setText(appInfo.getName() + " \n系统进程? ： "+appInfo.getSystem());
        ((BgItemViewHolder)holder).lock.setText(appInfo.isLock() ? "解锁" :"加锁");
        ((BgItemViewHolder)holder).lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != listener)
                listener.OnItemClick(appInfo);
            }
        });
    }
    private class BgItemViewHolder extends ItemViewHolder{
        public TextView packageName;
        public TextView lock ;
        public TextView name ;

        public BgItemViewHolder(View itemView) {
            super(itemView);
            packageName = (TextView) itemView.findViewById(R.id.package_name);
            lock = (TextView) itemView.findViewById(R.id.lock);
            name = (TextView) itemView.findViewById(R.id.name);
        }

    }
    public interface  onItemClickListener{
        void OnItemClick(AppInfo appInfo);
    }
}
