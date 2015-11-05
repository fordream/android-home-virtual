package com.example.test;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.vnp.androidvirtualkeypad.R;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
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

	public enum MyServiceType {//
		create, //
		delete, //
	}//

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		create();
		return super.onStartCommand(intent, flags, startId);
	}

	private WindowManager windowManager;
	private LinearLayout layout;
	private WindowManager.LayoutParams params;

	@Override
	public void onCreate() {
		super.onCreate();
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.btn_home) {

				} else if (v.getId() == R.id.back) {
					startBack();
				} else if (v.getId() == R.id.home) {
					startHomeLuncher();
				}
			}
		};

		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		layout = (LinearLayout) inflater
				.inflate(R.layout.main_notif_mail, null);

		layout.findViewById(R.id.back).setOnClickListener(clickListener);
		layout.findViewById(R.id.home).setOnClickListener(clickListener);
		layout.findViewById(R.id.btn_home).setOnClickListener(clickListener);

		params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.TOP | Gravity.LEFT;
		params.x = (windowManager.getDefaultDisplay().getWidth() - layout
				.getWidth()) / 2;
		params.x = getResources().getDimensionPixelSize(R.dimen.dimen_100dp);
		params.y = getResources().getDimensionPixelSize(R.dimen.dimen_0dp);
	}

	private void create() {
		try {
			windowManager.addView(layout, params);
		} catch (Exception exception) {
			windowManager.removeView(layout);
		}
	}

	private void startBack() {

		Activity activity = getActivity();
		if (activity != null) {
			Log.e("AAAAAAAAAAAAAAAAAS", "activity : "
					+ activity.getClass().getSimpleName());
		}
	}

	public static Activity getActivity() {
		try {
			Class activityThreadClass = Class
					.forName("android.app.ActivityThread");
			Object activityThread = activityThreadClass.getMethod(
					"currentActivityThread").invoke(null);
			Field activitiesField = activityThreadClass
					.getDeclaredField("mActivities");
			activitiesField.setAccessible(true);
			HashMap activities = (HashMap) activitiesField.get(activityThread);
			for (Object activityRecord : activities.values()) {
				Class activityRecordClass = activityRecord.getClass();
				Field pausedField = activityRecordClass
						.getDeclaredField("paused");
				pausedField.setAccessible(true);
				if (!pausedField.getBoolean(activityRecord)) {
					Field activityField = activityRecordClass
							.getDeclaredField("activity");
					activityField.setAccessible(true);
					Activity activity = (Activity) activityField
							.get(activityRecord);
					return activity;
				}
			}
		} catch (Exception ex) {
			return null;
		}
		return null;
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
			if (resolveInfo.activityInfo.packageName.contains("launcher")
					|| resolveInfo.activityInfo.packageName.contains("android")) {
				launcher = resolveInfo.activityInfo.packageName;
			}
		}
		return launcher;
	}

	// public List<Intent> homes() {
	// ArrayList<Intent> intentList = new ArrayList<Intent>();
	// Intent intent = null;
	// final PackageManager packageManager = getPackageManager();
	// for (final ResolveInfo resolveInfo : packageManager
	// .queryIntentActivities(new Intent(Intent.ACTION_MAIN)
	// .addCategory(Intent.CATEGORY_HOME),
	// PackageManager.MATCH_DEFAULT_ONLY)) {
	// intent = packageManager
	// .getLaunchIntentForPackage(resolveInfo.activityInfo.packageName);
	// Log.e("TAGSSS", resolveInfo.activityInfo.packageName + " "
	// + (intent == null));
	// intentList.add(intent);
	// }
	//
	// return intentList;
	// }

}
