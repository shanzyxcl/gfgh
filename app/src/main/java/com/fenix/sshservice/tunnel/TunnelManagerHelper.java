package com.fenix.sshservice.tunnel;

import static com.fenix.sshservice.tunnel.V2Tunnel.startOrStopTunnel;
import static ooOoO.oOoOoOO.oOoOOOOo;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.content.Context;

import com.fenix.sshservice.FenixVpnService;
import com.fenix.sshservice.config.Settings;
import com.fenix.vpn.FenixApp;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class TunnelManagerHelper
{
	public static void startSocketClay(Context context) {
        Intent startVPN = new Intent(context, FenixVpnService.class);
		
		if (startVPN != null) {
			TunnelUtils.restartRotateAndRandom();
			
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
			//noinspection NewApi
                context.startForegroundService(startVPN);
            else
                context.startService(startVPN);
        }
    }
	
	public static void stopSocketClay(Context context) {
		Intent stopTunnel = new Intent(FenixVpnService.TUNNEL_SSH_STOP_SERVICE);
		LocalBroadcastManager.getInstance(context).sendBroadcast(stopTunnel);
        Settings mConfig = new Settings(context);
        if (!mConfig.getPremium()) {
            oOoOOOOo();
        }
	}

    public static void ooOooOoOO(Activity activity) {
        if (FenixApp.oOOoOOo()) {
            stopSocketClay(activity);
        } else {
            startOrStopTunnel(activity);
        }
    }
}
