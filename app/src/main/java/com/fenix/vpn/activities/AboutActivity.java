package com.fenix.vpn.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.fenix.vpn.R;
import ooOoO.ConfigUtil;
import com.google.android.gms.ads.AdView;

public class AboutActivity extends BaseActivity
{
	private AdView adsBannerView;
	private TextView versionID;
	private ConfigUtil configUtil;

	@SuppressLint("SetTextI18n")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		configUtil = new ConfigUtil(this);

		// toolbar
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_main);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		versionID = findViewById(R.id.version_id);
		versionID.setText("v." + configUtil.Version());
		
		/*adsBannerView = (AdView) findViewById(R.id.adBannerAbout);
		
		// carrega anúncio
		if (TunnelUtils.isNetworkOnline(this)) {
			
			adsBannerView.setAdListener(new AdListener() {
				@Override
				public void onAdLoaded() {
					if (adsBannerView != null) {
						adsBannerView.setVisibility(View.VISIBLE);
					}
				}
			});

			adsBannerView.loadAd(new AdRequest.Builder()
				.build());
		}*/
	}

	@Override
	public boolean onSupportNavigateUp()
	{
		onBackPressed();
		return true;
	}

	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		super.onResume();

		
		if (adsBannerView != null) {
			adsBannerView.resume();
		}
	}


	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
		
		if (adsBannerView != null) {
			adsBannerView.pause();
		}
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		
		if (adsBannerView != null) {
			adsBannerView.destroy();
		}
	}
	
}

