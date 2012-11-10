package com.vs2.QRme;

import android.content.Context;

public class GCMBroadcastReceiver extends
        com.google.android.gcm.GCMBroadcastReceiver {

    @Override
    protected String getGCMIntentServiceClassName(Context context) {

        return "com.vs2.QRme.GCMIntentService";
    }
}
