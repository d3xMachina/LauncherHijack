package com.whiteraven777.launcherreplacer;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by andy on 26/07/2017.
 */

public class HomePress {
    private static long LastActivate = 0;

    public static Intent GetDesiredIntent(Context c)
    {
        SharedPreferences settings = c.getSharedPreferences("LauncherReplacer", MODE_PRIVATE);
        String s = settings.getString("ChosenLauncher", "com.whiteraven777.launcherreplacer");
        String name = settings.getString("ChosenLauncherName", "com.whiteraven777.launcherreplacer.MainActivity");

        ComponentName componentName = new ComponentName(s, name);
        Intent i = new Intent(Intent.ACTION_MAIN);

        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        i.setComponent(componentName);

        return i;
    }

    public static void Perform(Context c)
    {
        // Simple debounce
        long time = System.currentTimeMillis();
        if (time - LastActivate < 200)
            return;
        LastActivate = time;

        Intent i = GetDesiredIntent(c);
        PendingIntent pendingIntent = PendingIntent.getActivity(c, 0, i, PendingIntent.FLAG_IMMUTABLE);
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
}
