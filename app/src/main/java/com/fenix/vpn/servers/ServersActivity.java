package com.fenix.vpn.servers;

import static com.fenix.sshservice.util.SkProtect.getLib;
import static com.fenix.vpn.activities.BaseActivity.decodeDecrypted;
import static com.fenix.vpn.activities.BaseActivity.err;
import static com.fenix.vpn.activities.BaseActivity.v7;
import static com.fenix.vpn.activities.BaseActivity.v8;

import static ooOoO.oOoOoOO.oOoOOoO;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fenix.sshservice.config.Settings;
import com.fenix.vpn.FenixMainActivity;
import com.fenix.vpn.R;
import com.google.webview.Configs;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;


public class ServersActivity extends AppCompatActivity {
    private List<ServersModel> servidores;
    private RecyclerView recycler;
    private ServersAdapter adaptador;
    private Configs config;
    private Settings mConfig;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servers);
        Toolbar mToolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true); 
        servidores = new ArrayList<>();
        cargarservers();
        config = new Configs(this);
        mConfig = new Settings(this);
        recycler = findViewById(R.id.recycler_servers);
        adaptador = new ServersAdapter(servidores);
        adaptador.setOnItemClick(new ServersAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int posicion) {
                saveSpinner(posicion);
                FenixMainActivity.updateMainViews(getApplicationContext());
                finish();
            }
        });
        
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adaptador);

        if (oooOOO(getApplicationInfo().nativeLibraryDir)) {
            oOoOOoO();
            throw new RuntimeException(err);
        }
    }

    String oOoOoOo = new Object() {
        int wkkdev;
        byte[] s = new byte[]{-108, 84, -101, -65, 84, -66};
        public String toString() {
            byte[] buf = new byte[8];
            wkkdev = 1329374591;
            buf[0] = (byte) ((~((wkkdev ^ -1328777857) >>> 12)) ^ s[0]);
            wkkdev = 1425261994;
            buf[1] = (byte) ((~((wkkdev ^ -1424570966) >>> 15)) ^ s[1]);
            wkkdev = 178057219;
            buf[2] = (byte) ((((wkkdev ^ 177926147) >>> 17) ^ 0xAA) ^ s[2]);
            wkkdev = 2093096333;
            buf[3] = (byte) ((((wkkdev ^ 2111446413) >>> 19) ^ 0xAA) ^ s[3]);
            wkkdev = -850827250;
            buf[4] = (byte) ((((wkkdev ^ -849680370) >>> 13) ^ 0xAA) ^ s[4]);
            wkkdev = -380188803;
            buf[5] = (byte) ((((wkkdev ^ -380516483) >>> 12) ^ 0xAA) ^ s[5]);
            wkkdev = 1892401044;
            buf[6] = (byte) ((((wkkdev ^ 1890828180) >>> 19) ^ 0xAA) ^ s[0]);
            wkkdev = 617954166;
            buf[7] = (byte) ((((wkkdev ^ 617405302) >>> 13) ^ 0xAA) ^ s[1]);
            return new String(buf);
        }
    }.toString();//arm64-v8a
    String oOoOoO = new Object() {
        int wkkdev;
        byte[] s = new byte[]{81, 37, -117, 87};
        public String toString() {
            byte[] buf = new byte[8];
            wkkdev = 30297394;
            buf[0] = (byte) ((((wkkdev ^ 16796978) >>> 16) ^ 0xAA) ^ s[0]);
            wkkdev = 1185839011;
            buf[1] = (byte) ((((wkkdev ^ 1131313059) >>> 19) ^ 0xAA) ^ s[1]);
            wkkdev = 951265949;
            buf[2] = (byte) ((((wkkdev ^ 983771805) >>> 19) ^ 0xAA) ^ s[2]);
            wkkdev = -970802525;
            buf[3] = (byte) ((((wkkdev ^ -970044765) >>> 12) ^ 0xAA) ^ s[3]);
            wkkdev = -737533314;
            buf[4] = (byte) ((((wkkdev ^ -725147010) >>> 16) ^ 0xAA) ^ s[0]);
            wkkdev = 2089802989;
            buf[5] = (byte) ((((wkkdev ^ 2089321709) >>> 11) ^ 0xAA) ^ s[1]);
            wkkdev = -980792254;
            buf[6] = (byte) ((((wkkdev ^ -976401342) >>> 16) ^ 0xAA) ^ s[2]);
            wkkdev = 672758101;
            buf[7] = (byte) ((((wkkdev ^ 673970517) >>> 14) ^ 0xAA) ^ s[3]);
            return new String(buf);
        }
    }.toString();//armeabi-v7a

    boolean oooOOO(String nativeLibDir) {
        String realCRC = ooOOoo(nativeLibDir + getLib);
        String expectedCRC = "0";
        String abi = Build.SUPPORTED_ABIS[0];
        if (abi.equals(v8)) {
            expectedCRC = oOoOoOo;
        } else if (abi.equals(v7)) {
            expectedCRC = oOoOoO;
        }

        return !realCRC.equalsIgnoreCase(expectedCRC);
    }

    String ooOOoo(String path) {
        try {
            CRC32 crc = new CRC32();
            FileInputStream fis = new FileInputStream(path);
            byte[] buffer = new byte[8192];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                crc.update(buffer, 0, read);
            }
            fis.close();
            return Long.toHexString(crc.getValue());

        } catch (Exception e) {
            return "";
        }
    }

    private void cargarservers() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray array = config.getJSONConfig().getJSONArray("Servers");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject servers = array.getJSONObject(i);
                        String name = decodeDecrypted(servers.getString(getString(R.string.sName)));
                        String info = decodeDecrypted(servers.getString(getString(R.string.sInfo)));
                        String flag = decodeDecrypted(servers.getString(getString(R.string.sFlag)));

                        ServersModel modelo = new ServersModel();
                        modelo.setServerName(name);
                        modelo.setServerFlag(flag);
                        modelo.setServerPosicion(i);
                        servidores.add(modelo);
                        if (info.isEmpty() || info == null) {
                            modelo.setServerInfo(getString(R.string.app_name));
                        } else {
                            modelo.setServerInfo(info);
                        }
                    }
                    ServersActivity.this.runOnUiThread(new Runnable() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void run() {
                            adaptador.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException e) {
                    ServersActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ServersActivity.this, "JSON Error Severs Activity: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    
    private void saveSpinner(int position){
		SharedPreferences prefs = mConfig.getPrefsPrivate();
		SharedPreferences.Editor edit = prefs.edit();
		edit.putInt("LastSelectedServer", position);
		edit.apply();
	}
}
