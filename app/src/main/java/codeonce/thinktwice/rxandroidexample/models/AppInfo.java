package codeonce.thinktwice.rxandroidexample.models;

import android.support.annotation.NonNull;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * -> Created by think-twice-code-once on Feb 21th, 2017.
 */

@Data
@Accessors(prefix = "m")
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
}
