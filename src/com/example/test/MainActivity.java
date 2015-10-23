package com.example.test;

import java.util.ArrayList;
import java.util.List;
import me.leolin.shortcutbadger.ShortcutBadger;
import org.vnp.androidvirtualkeypad.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ShortcutBadger.with(this).count(1);
		
		startService(new Intent(this,MyService.class));
		homes();
		
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
			Log.e("TAGSSS", resolveInfo.activityInfo.packageName + " " + (intent == null));
			intentList.add(intent);
		}

		return intentList;
	}

}
