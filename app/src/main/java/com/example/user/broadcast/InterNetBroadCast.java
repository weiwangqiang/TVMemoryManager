package com.example.user.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.user.service.FloatBallService;

public class InterNetBroadCast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Intent intent1;
        intent1 = new Intent(context,FloatBallService.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(intent1);
    }
}
