package com.techmania.notificationprocedures;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

public class ReceiverDismiss extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManagerCompat compat = NotificationManagerCompat.from(context);
        compat.cancel(1);

    }
}
