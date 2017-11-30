package com.example.user.customView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.user.tvmanager.R;


/**
 * Created by user on 2017/11/23.
 */

public class ClearProcessWaitDialog extends Dialog{
    public ClearProcessWaitDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_clear_process_wait);
    }

}
