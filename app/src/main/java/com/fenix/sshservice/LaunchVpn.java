package com.fenix.sshservice;

import static ooOoO.oOoOoOO.oOoOOOOo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.VpnService;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.fenix.sshservice.config.PasswordCache;
import com.fenix.sshservice.config.Settings;
import com.fenix.sshservice.logger.ConnectionStatus;
import com.fenix.sshservice.logger.SkStatus;
import com.fenix.sshservice.tunnel.TunnelManagerHelper;
import com.fenix.sshservice.tunnel.TunnelUtils;
import com.fenix.sshservice.util.ToastUtil;
import com.fenix.vpn.R;

public class LaunchVpn extends AppCompatActivity
        implements DialogInterface.OnCancelListener {
    public static final String EXTRA_HIDELOG = (new Object() {
        int MDevz;
        public String toString() {
            byte[] buf = new byte[29];
            MDevz = -1704747522;
            buf[0] = (byte) (MDevz >>> 16);
            MDevz = 1734799672;
            buf[1] = (byte) (MDevz >>> 12);
            MDevz = -1417038232;
            buf[2] = (byte) (MDevz >>> 10);
            MDevz = -1825696100;
            buf[3] = (byte) (MDevz >>> 16);
            MDevz = -345605654;
            buf[4] = (byte) (MDevz >>> 16);
            MDevz = -291050326;
            buf[5] = (byte) (MDevz >>> 5);
            MDevz = -1777381949;
            buf[6] = (byte) (MDevz >>> 5);
            MDevz = -1231125238;
            buf[7] = (byte) (MDevz >>> 20);
            MDevz = -253881019;
            buf[8] = (byte) (MDevz >>> 14);
            MDevz = 1429303775;
            buf[9] = (byte) (MDevz >>> 11);
            MDevz = 1463491257;
            buf[10] = (byte) (MDevz >>> 15);
            MDevz = -421296690;
            buf[11] = (byte) (MDevz >>> 11);
            MDevz = 1617107903;
            buf[12] = (byte) (MDevz >>> 6);
            MDevz = 1388697983;
            buf[13] = (byte) (MDevz >>> 13);
            MDevz = 710735225;
            buf[14] = (byte) (MDevz >>> 14);
            MDevz = -321244955;
            buf[15] = (byte) (MDevz >>> 14);
            MDevz = -75999318;
            buf[16] = (byte) (MDevz >>> 19);
            MDevz = 76474236;
            buf[17] = (byte) (MDevz >>> 13);
            MDevz = -1406867431;
            buf[18] = (byte) (MDevz >>> 12);
            MDevz = 1808655569;
            buf[19] = (byte) (MDevz >>> 13);
            MDevz = -2142686469;
            buf[20] = (byte) (MDevz >>> 10);
            MDevz = -1820476089;
            buf[21] = (byte) (MDevz >>> 19);
            MDevz = -323274224;
            buf[22] = (byte) (MDevz >>> 11);
            MDevz = 367821591;
            buf[23] = (byte) (MDevz >>> 22);
            MDevz = -213059764;
            buf[24] = (byte) (MDevz >>> 19);
            MDevz = 1397605272;
            buf[25] = (byte) (MDevz >>> 13);
            MDevz = 241499578;
            buf[26] = (byte) (MDevz >>> 16);
            MDevz = 182392000;
            buf[27] = (byte) (MDevz >>> 17);
            MDevz = 1969155741;
            buf[28] = (byte) (MDevz >>> 9);
            return new String(buf);
        }
    }.toString());
    public static final String CLEARLOG = "clearlogconnect";

    private static final int START_VPN_PROFILE = 70;

    private static Settings mConfig;
    private String mTransientAuthPW;
    private boolean mhideLog = false;
    private boolean isMostrarSenha = false;

    private ToastUtil toastutil;
    static SharedPreferences prefs;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.launchvpn);

        mConfig = new Settings(this);
        prefs = mConfig.getPrefsPrivate();
        toastutil = new ToastUtil(this);

        startVpnFromIntent();
        //throw new RuntimeException();
    }

    protected void startVpnFromIntent() {
        // Resolve the intent

        final Intent intent = getIntent();
        final String action = intent.getAction();

        // If the intent is a request to create a shortcut, we'll do that and exit


        if (Intent.ACTION_MAIN.equals(action)) {
            // Check if we need to clear the log
            if (mConfig.getAutoClearLog())
                SkStatus.clearLog();

            mhideLog = intent.getBooleanExtra(EXTRA_HIDELOG, false);

            launchVPN();
        }
    }

    private void askForPW(final int type) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.title_auth);
        dialog.setMessage(R.string.auth_message);
        dialog.setOnCancelListener(this);

        @SuppressLint("InflateParams") final View userpwlayout = getLayoutInflater()
                .inflate(R.layout.userpass, null, false);

        ((EditText) userpwlayout.findViewById(R.id.username)).setText(mConfig.getPrivString(Settings.USUARIO_KEY));
        ((EditText) userpwlayout.findViewById(R.id.password)).setText(mConfig.getPrivString(Settings.SENHA_KEY));
        ((CheckBox) userpwlayout.findViewById(R.id.save_password)).setChecked(true);
        ((ImageButton) userpwlayout.findViewById(R.id.show_password)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMostrarSenha = !isMostrarSenha;
                if (isMostrarSenha) {
                    ((EditText) userpwlayout.findViewById(R.id.password)).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ((ImageButton) userpwlayout.findViewById(R.id.show_password)).setImageDrawable(ContextCompat.getDrawable(LaunchVpn.this, R.drawable.ic_visibility_off_black_24dp));
                } else {
                    ((EditText) userpwlayout.findViewById(R.id.password)).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ((ImageButton) userpwlayout.findViewById(R.id.show_password)).setImageDrawable(ContextCompat.getDrawable(LaunchVpn.this, R.drawable.ic_visibility_black_24dp));
                }
            }
        });

        dialog.setView(userpwlayout);

        dialog.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (type == R.string.password) {
                            SharedPreferences.Editor edit = mConfig.getPrefsPrivate().edit();

                            String mUsername = ((EditText) userpwlayout.findViewById(R.id.username)).getText().toString();

                            edit.putString(Settings.USUARIO_KEY, mUsername);

                            String pw = ((EditText) userpwlayout.findViewById(R.id.password)).getText().toString();
                            if (((CheckBox) userpwlayout.findViewById(R.id.save_password)).isChecked()) {
                                edit.putString(Settings.SENHA_KEY, pw);
                            } else {
                                edit.remove(Settings.SENHA_KEY);
                                mTransientAuthPW = pw;
                            }

                            edit.apply();
                        }

                        if (mTransientAuthPW != null)
                            PasswordCache.setCachedPassword(null, PasswordCache.AUTHPASSWORD, mTransientAuthPW);
                        onActivityResult(START_VPN_PROFILE, Activity.RESULT_OK, null);
                    }

                });
        dialog.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SkStatus.updateStateString("USER_VPN_PASSWORD_CANCELLED", "", R.string.state_user_vpn_password_cancelled,
                                ConnectionStatus.LEVEL_NOTCONNECTED);
                        finish();
                    }
                });

        dialog.create().show();
    }

    @Override
    public void onCancel(DialogInterface p1) {
        SkStatus.updateStateString("USER_VPN_PASSWORD_CANCELLED", "", R.string.state_user_vpn_password_cancelled,
                ConnectionStatus.LEVEL_NOTCONNECTED);
        finish();
    }

    private void showLogWindow() {
        Intent updateView = new Intent("com.fenix.vpn:openLogs");
        LocalBroadcastManager.getInstance(this)
                .sendBroadcast(updateView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == START_VPN_PROFILE) {
            if (resultCode == Activity.RESULT_OK) {
                SharedPreferences prefs = mConfig.getPrefsPrivate();

                if (!TunnelUtils.isNetworkOnline(this)) {
                    SkStatus.updateStateString("USER_VPN_PASSWORD_CANCELLED", "", R.string.state_user_vpn_password_cancelled,
                            ConnectionStatus.LEVEL_NOTCONNECTED);

                    toastutil.showConfusingToast("No Hay Conexión de Internet");
                    if (!mConfig.getPremium()) {
                        oOoOOOOo();
                    }
                    finish();
                }/* else if (prefs.getInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT) == Settings.bTUNNEL_TYPE_SSH_PROXY &&
                        (mConfig.getPrivString(Settings.PROXY_IP_KEY).isEmpty() && !ismodeudp() && !ismodev2ray() || mConfig.getPrivString(Settings.PROXY_PORTA_KEY).isEmpty()) && !ismodeudp() && !ismodev2ray()) {
                    SkStatus.updateStateString("USER_VPN_PASSWORD_CANCELLED", "", R.string.state_user_vpn_password_cancelled,
                            ConnectionStatus.LEVEL_NOTCONNECTED);

                    Toast.makeText(this, R.string.error_proxy_invalid,
                            Toast.LENGTH_SHORT).show();

                    finish();
                }
				else if (!prefs.getBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true) && mConfig.getPrivString(Settings.CUSTOM_PAYLOAD_KEY).isEmpty()) {
					SkStatus.updateStateString("USER_VPN_PASSWORD_CANCELLED", "", R.string.state_user_vpn_password_cancelled,
						ConnectionStatus.LEVEL_NOTCONNECTED);
					
					Toast.makeText(this, R.string.error_empty_payload,
						Toast.LENGTH_SHORT).show();

					finish();
				}
                else if (mConfig.getPrivString(Settings.SERVIDOR_KEY).isEmpty() && !ismodeudp() && !ismodev2ray() || mConfig.getPrivString(Settings.SERVIDOR_PORTA_KEY).isEmpty() && !ismodeudp() && !ismodev2ray()) {
                    SkStatus.updateStateString("USER_VPN_PASSWORD_CANCELLED", "", R.string.state_user_vpn_password_cancelled,
                            ConnectionStatus.LEVEL_NOTCONNECTED);

                    Toast.makeText(this, R.string.error_empty_settings,
                            Toast.LENGTH_SHORT).show();

                    /**Intent startLW = new Intent();
                     startLW.setComponent(new ComponentName(this, getPackageName() + ".activities.ConfigGeralActivity"));
                     startLW.setAction("openSSHScreen");
                     startLW.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                     startActivity(startLW);
                    finish();
                } else if (mConfig.getPrivString(Settings.USUARIO_KEY).isEmpty() && !ismodeudp() && !ismodev2ray() || (mConfig.getPrivString(Settings.SENHA_KEY).isEmpty() &&
                        (mTransientAuthPW == null || mTransientAuthPW.isEmpty())) && !ismodeudp() && !ismodev2ray()) {
                    SkStatus.updateStateString("USER_VPN_PASSWORD", "", R.string.state_user_vpn_password,
                            ConnectionStatus.LEVEL_WAITING_FOR_USER_INPUT);

                    askForPW(R.string.password);
                }*/ else {
                    if (!mhideLog) {
                        showLogWindow();
                    }

                    TunnelManagerHelper.startSocketClay(this);

                    finish();
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User does not want us to start, so we just vanish
                SkStatus.updateStateString("USER_VPN_PERMISSION_CANCELLED", "", R.string.state_user_vpn_permission_cancelled,
                        ConnectionStatus.LEVEL_NOTCONNECTED);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    SkStatus.logError(R.string.nought_alwayson_warning);
                if (!mConfig.getPremium()) {
                    oOoOOOOo();
                }
                finish();
            }
        }
    }

    private void launchVPN() {
        Intent intent = VpnService.prepare(this);

        if (intent != null) {
            SkStatus.updateStateString("USER_VPN_PERMISSION", "", R.string.state_user_vpn_permission,
                    ConnectionStatus.LEVEL_WAITING_FOR_USER_INPUT);
            // Start the query
            try {
                startActivityForResult(intent, START_VPN_PROFILE);
            } catch (ActivityNotFoundException ane) {
                // Shame on you Sony! At least one user reported that
                // an official Sony Xperia Arc S image triggers this exception
                SkStatus.logError(R.string.no_vpn_support_image);
                showLogWindow();
            }
        } else {
            onActivityResult(START_VPN_PROFILE, Activity.RESULT_OK, null);
        }
    }

    private boolean ismodeudp() {
        return prefs.getInt(Settings.TUNNELTYPE_KEY, 0) == Settings.bTUNNEL_TYPE_UDP;
    }

    public static boolean ismodev2ray() {
        return prefs.getInt(Settings.TUNNELTYPE_KEY, 0) == Settings.bTUNNEL_TYPE_V2RAY;
    }

    public static String getIPDomain(){
        return mConfig.getPrivString(Settings.PROXY_IP_KEY);
    }

    public static String getPayloadKey(){
        return mConfig.getPrivString(Settings.CUSTOM_PAYLOAD_KEY);
    }

    public static String getPort(){
        return mConfig.getPrivString(Settings.PROXY_PORTA_KEY);
    }


}
