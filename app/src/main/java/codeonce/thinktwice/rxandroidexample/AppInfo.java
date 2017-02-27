package codeonce.thinktwice.rxandroidexample;

import android.support.annotation.NonNull;

/**
 * -> Created by think-twice-code-once on Feb 21th, 2017.
 */

public class AppInfo implements Comparable <Object> {

    private long mLastUpdateTime;
    private String mName;
    private String mIcon;

    public AppInfo(String name, String icon, long lastUpdateTime) {
        this.mName = name;
        this.mIcon = icon;
        this.mLastUpdateTime = lastUpdateTime;
    }

    @Override
    public int compareTo(@NonNull Object another) {
        AppInfo appInfo =  (AppInfo) another;
        return getName().compareTo(appInfo.getName());
    }

    public String getName() {
        return this.mName;
    }
}
