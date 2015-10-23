package com.example.test;

import java.util.ArrayList;
import java.util.List;

import org.vnp.androidvirtualkeypad.R;

import android.app.Instrumentation;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class MyService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		create();
		return super.onStartCommand(intent, flags, startId);
	}

	private void create() {
		WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.main_notif_mail, null);

		layout.findViewById(R.id.back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						startBack();
					}
				});

		layout.findViewById(R.id.home).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						startHomeLuncher();
					}
				});

		WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		params.gravity = Gravity.TOP | Gravity.LEFT;
		params.x = (windowManager.getDefaultDisplay().getWidth() - layout
				.getWidth()) / 2;
		params.x = getResources().getDimensionPixelSize(R.dimen.dimen_135dp);
		params.y = getResources().getDimensionPixelSize(R.dimen.dimen_0dp);

		windowManager.addView(layout, params);
	}

	protected void startBack() {
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				Instrumentation m_Instrumentation = new Instrumentation();
				m_Instrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
			}
		});

	}

	protected void startHomeLuncher() {
		Intent mIntent = new Intent();
		mIntent.setPackage(getLauncher());
		mIntent.addCategory(Intent.CATEGORY_HOME);
		mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(mIntent);
	}

	private String getLauncher() {
		String launcher = "com.android.launcher";
		final PackageManager packageManager = getPackageManager();
		for (final ResolveInfo resolveInfo : packageManager
				.queryIntentActivities(new Intent(Intent.ACTION_MAIN)
						.addCategory(Intent.CATEGORY_HOME),
						PackageManager.MATCH_DEFAULT_ONLY)) {
			if (resolveInfo.activityInfo.packageName.contains("launcher") || resolveInfo.activityInfo.packageName.contains("android")) {
				launcher = resolveInfo.activityInfo.packageName;
			}
		}
		return launcher;
	}

	public List<Intent> homes() {
		ArrayList<Intent> intentList = new ArrayList<Intent>();
		Intent intent = null;
		final PackageManager packageManager = getPackageManager();
		for (final ResolveInfo resolveInfo : packageManager
				.queryIntentActivities(new Intent(Intent.ACTION_MAIN)
						.addCategory(Intent.CATEGORY_HOME),
						PackageManager.MATCH_DEFAULT_ONLY)) {
			intent = packageManager
					.getLaunchIntentForPackage(resolveInfo.activityInfo.packageName);
			Log.e("TAGSSS", resolveInfo.activityInfo.packageName + " "
					+ (intent == null));
			intentList.add(intent);
		}

		return intentList;
	}

}
