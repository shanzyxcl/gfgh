package hex.custom.lib.v2ray.core;

import static hex.custom.lib.v2ray.utils.V2rayConstants.V2RAY_SERVICE_CURRENT_CONFIG_DELAY_BROADCAST_EXTRA;
import static hex.custom.lib.v2ray.utils.V2rayConstants.V2RAY_SERVICE_CURRENT_CONFIG_DELAY_BROADCAST_INTENT;
import static hex.custom.lib.v2ray.utils.Utilities.getDeviceIdForXUDPBaseKey;
import static hex.custom.lib.v2ray.utils.Utilities.getUserAssetsPath;

import android.app.Service;
import android.content.Intent;
import android.util.Log;

import com.fenix.sshservice.logger.SkStatus;

import go.Seq;
import hex.custom.lib.v2ray.interfaces.V2rayServicesListener;
import hex.custom.lib.v2ray.model.V2rayConfigModel;
import hex.custom.lib.v2ray.utils.V2rayConstants;
import libv2ray.CoreCallbackHandler;
import libv2ray.CoreController;
import libv2ray.Libv2ray;

public class V2rayCoreExecutor implements CoreCallbackHandler{

    private V2rayConstants.CORE_STATES coreState;
    public V2rayServicesListener v2rayServicesListener;
    private CoreController v2RayPoint;

    @Override
    public long shutdown() {
        try {
            if (v2rayServicesListener == null) {
                Log.d(V2rayCoreExecutor.class.getSimpleName(), "shutdown => can`t find initialed service.");
                return -1;
            }
            v2rayServicesListener.stopService();
            v2rayServicesListener = null;
            return 0;
        } catch (Exception e) {
            Log.d(V2rayCoreExecutor.class.getSimpleName(), "shutdown =>", e);
            return -1;
        }
    }

   /*     @Override
        public boolean protect(long l) {
            if (v2rayServicesListener != null)
                return v2rayServicesListener.onProtect((int) l);
            return true;
        }*/

    @Override
    public long onEmitStatus(long l, String s) {
        return 0;
    }

    @Override
    public long startup() {
        if (v2rayServicesListener != null) {
            try {
                coreState = V2rayConstants.CORE_STATES.RUNNING;
                v2rayServicesListener.startService();
            } catch (Exception e) {
                Log.d(V2rayCoreExecutor.class.getSimpleName(), "setupFailed => ", e);
                return -1;
            }
        }
        return 0;
    }

    private boolean running = false;


    public V2rayCoreExecutor(final Service targetService) {
        this.v2rayServicesListener = (V2rayServicesListener) targetService;
        Seq.setContext(targetService);
        Libv2ray.initCoreEnv(getUserAssetsPath(targetService.getApplicationContext()), getDeviceIdForXUDPBaseKey(targetService.getApplicationContext()));
        v2RayPoint = new CoreController(this);
        coreState = V2rayConstants.CORE_STATES.IDLE;
        running = true;
        Log.d(V2rayCoreExecutor.class.getSimpleName(), "V2rayCoreExecutor -> New initialize from : " + targetService.getClass().getSimpleName());
    }



    public void stopCore(final boolean shouldStopService) {
        try {
            if (v2RayPoint.getIsRunning()) {
                v2RayPoint.stopLoop();
                if (shouldStopService) {
                    v2rayServicesListener.stopService();
                }
                coreState = V2rayConstants.CORE_STATES.STOPPED;
            }
        } catch (Exception e) {
            Log.d(V2rayCoreExecutor.class.getSimpleName(), "stopCore =>", e);
        }
    }

    public long getDownloadSpeed() {
        return v2RayPoint.queryStats("block", "downlink") + v2RayPoint.queryStats("proxy", "downlink");
    }

    public boolean startCore(final V2rayConfigModel v2rayConfig) {
        if (!running) return false;
        try {
            v2RayPoint.startLoop(v2rayConfig.fullJsonConfig, -1);
            return true;
        } catch (Exception e) {
            coreState = V2rayConstants.CORE_STATES.STOPPED;
            stopCore(true);
            Log.e(V2rayCoreExecutor.class.getSimpleName(), "startCore =>", e);
            return false;
        }
    }

    public long getUploadSpeed() {
        return v2RayPoint.queryStats("block", "uplink")  + v2RayPoint.queryStats("proxy", "uplink");
    }

    public V2rayConstants.CORE_STATES getCoreState() {
        if (coreState == V2rayConstants.CORE_STATES.RUNNING) {
            if (!v2RayPoint.getIsRunning()) {
                coreState = V2rayConstants.CORE_STATES.STOPPED;
            }
            return coreState;
        }
        return coreState;
    }

    public void broadCastCurrentServerDelay() {
        try {
            if (v2rayServicesListener != null) {
                int serverDelay = (int) v2RayPoint.measureDelay("");
                Intent serverDelayBroadcast = new Intent(V2RAY_SERVICE_CURRENT_CONFIG_DELAY_BROADCAST_INTENT);
                serverDelayBroadcast.setPackage(v2rayServicesListener.getService().getPackageName());
                serverDelayBroadcast.putExtra(V2RAY_SERVICE_CURRENT_CONFIG_DELAY_BROADCAST_EXTRA, serverDelay);
                v2rayServicesListener.getService().sendBroadcast(serverDelayBroadcast);
            }
        } catch (Exception e) {
            Log.d(V2rayCoreExecutor.class.getSimpleName(), "broadCastCurrentServerDelay => ", e);
            Intent serverDelayBroadcast = new Intent(V2RAY_SERVICE_CURRENT_CONFIG_DELAY_BROADCAST_INTENT);
            serverDelayBroadcast.setPackage(v2rayServicesListener.getService().getPackageName());
            serverDelayBroadcast.putExtra(V2RAY_SERVICE_CURRENT_CONFIG_DELAY_BROADCAST_EXTRA, -1);
            v2rayServicesListener.getService().sendBroadcast(serverDelayBroadcast);
        }
    }

}

