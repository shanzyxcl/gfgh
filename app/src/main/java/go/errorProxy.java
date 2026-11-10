package go;

import static com.fenix.sshservice.tunnel.TunnelManagerHelper.ooOooOoOO;

import android.app.Activity;
import android.content.Context;

import com.fenix.sshservice.tunnel.V2Tunnel;

public class errorProxy {

    private static Context appContext;

    public static void init(Context context) {
        if (appContext == null) {
            appContext = context.getApplicationContext();
        }
    }

    public static void oOOoOOOo() {
        if (appContext == null) return;
        V2Tunnel v2Tunnel = new V2Tunnel(appContext);
        v2Tunnel.getLocalProxy();
    }

    public static void ooOooOo(Activity activity) {
        ooOooOoOO(activity);
    }
}