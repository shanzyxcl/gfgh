package com.fenix.vpn;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;

import com.fenix.vpn.activities.BaseActivity;


/**
 * @author anuragdhunna
 */
public class LauncherActivity extends BaseActivity {
    private static final long COUNTER_TIME = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash_screen);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);


        getWindow().setStatusBarColor(ContextCompat.getColor(this, socketclay.compatstyle.R.color.gray));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, socketclay.compatstyle.R.color.gray));

        createTimer();

    }

    private void createTimer() {
        CountDownTimer countDownTimer =
                new CountDownTimer(LauncherActivity.COUNTER_TIME * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        Application application = getApplication();

                        if (!(application instanceof FenixApp)) {

                            new Handler().postDelayed(() -> {
                                Intent intent = new Intent(LauncherActivity.this, FenixMainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                                finish();
                            }, 1000);
                            return;
                        }

                        ((FenixApp) application)
                                .showAdIfAvailabel(LauncherActivity.this, () -> new Handler().postDelayed(() -> {
                                    Intent intent = new Intent(LauncherActivity.this, FenixMainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);
                                    finish();
                                }, 1000));
                    }
                };
        countDownTimer.start();
    }

}
