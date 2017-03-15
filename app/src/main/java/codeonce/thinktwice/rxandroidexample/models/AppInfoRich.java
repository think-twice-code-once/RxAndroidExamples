package codeonce.thinktwice.rxandroidexample.models;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import java.util.Locale;

import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * -> Created by Think-Twice-Code-Once on March 14th, 2017.
 */

@Accessors(prefix = "m")
public class AppInfoRich implements Comparable<Object> {

    @Setter
    String mName = null;

    private Context mContext;

    private ResolveInfo mResolveInfo;

    private ComponentName mComponentName = null;

    private PackageInfo mPackageInfo = null;

    private Drawable mIcon = null;

    public AppInfoRich(Context context, ResolveInfo resolveInfo) {
        mContext = context;
        mResolveInfo = resolveInfo;

        mComponentName = new ComponentName(resolveInfo.activityInfo.applicationInfo.packageName,
                resolveInfo.activityInfo.name);

        try {
            mPackageInfo = mContext.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        if (mName != null) {
            return mName;
        } else {
            try {
                return getNameFromResolveInfo(mResolveInfo);
            } catch (PackageManager.NameNotFoundException e) {
                return getPackageName();
            }
        }
    }

    public String getPackageName() {
        return mResolveInfo.activityInfo.packageName;
    }

    public String getActivityName() {
        return mResolveInfo.activityInfo.name;
    }

    public Drawable getIcon() {
        if (mIcon == null) {
            mIcon = getResolveInfo().loadIcon(mContext.getPackageManager());
        }
        return mIcon;
    }

    public ComponentName getComponentName() {
        return mComponentName;
    }

    public String getComponentInfo() {
        if (getComponentName() != null) {
            return getComponentName().toString();
        } else {
            return "";
        }
    }

    public ResolveInfo getResolveInfo() {
        return mResolveInfo;
    }

    public PackageInfo getPackageInfo() {
        return mPackageInfo;
    }

    public String getVersionName() {
        PackageInfo packageInfo = getPackageInfo();
        if (packageInfo != null) {
            return packageInfo.versionName;
        } else {
            return "";
        }
    }

    public int getVersionCode() {
        PackageInfo packageInfo = getPackageInfo();
        if (packageInfo != null) {
            return packageInfo.versionCode;
        } else {
            return 0;
        }
    }

    @SuppressLint("NewApi")
    public long getFirstInstallTime() {
        PackageInfo pi = getPackageInfo();
        if (pi != null
                && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            return pi.firstInstallTime;
        } else {
            return 0;
        }
    }

    @SuppressLint("NewApi")
    public long getLastUpdateTime() {
        PackageInfo pi = getPackageInfo();
        if (pi != null
                && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            return pi.lastUpdateTime;
        } else {
            return 0;
        }
    }

    public String getNameFromResolveInfo(ResolveInfo resolveInfo)
            throws PackageManager.NameNotFoundException {
        String name = mResolveInfo.resolvePackageName;
        if (mResolveInfo.activityInfo != null) {
            Resources resources = mContext.getPackageManager()
                    .getResourcesForApplication(resolveInfo.activityInfo.applicationInfo);
            Resources engResources = getEnglishResources(resources);

            if (resolveInfo.activityInfo.labelRes != 0) {
                name = engResources.getString(mResolveInfo.activityInfo.labelRes);

                if (name == null || name.equals("")) {
                    name = resources.getString(resolveInfo.activityInfo.labelRes);
                }
            } else {
                name = resolveInfo.activityInfo.applicationInfo
                        .loadLabel(mContext.getPackageManager()).toString();
            }
        }
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    private Resources getEnglishResources(Resources standardResources) {
        AssetManager assetManager = standardResources.getAssets();
        DisplayMetrics displayMetrics = standardResources.getDisplayMetrics();
        Configuration configuration = new Configuration(standardResources.getConfiguration());
        configuration.setLocale(Locale.US);
        return new Resources(assetManager, displayMetrics, configuration);
    }


    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
