package codeonce.thinktwice.rxandroidexample.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import codeonce.thinktwice.rxandroidexample.CoreApplication;
import codeonce.thinktwice.rxandroidexample.R;
import codeonce.thinktwice.rxandroidexample.adapters.ApplicationAdapter;
import codeonce.thinktwice.rxandroidexample.models.AppInfo;
import codeonce.thinktwice.rxandroidexample.models.AppInfoRich;
import codeonce.thinktwice.rxandroidexample.models.ApplicationsList;
import codeonce.thinktwice.rxandroidexample.util.Utils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * -> Created by Think-Twice-Code-Once on March 14th, 2017.
 */

@EFragment(R.layout.fragment_example)
public class FirstExampleFragment extends Fragment {

    @ViewById(R.id.fragment_first_example_list)
    RecyclerView mRecyclerView;

    @ViewById(R.id.fragment_first_example_swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private ApplicationAdapter mAdapter;
    private File mFilesDir;

    @AfterViews
    void init() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(
                getActivity().getApplicationContext()));

        mAdapter = new ApplicationAdapter(new ArrayList<>(), R.layout.applications_list_item);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity()
                .getApplicationContext(), R.color.myPrimaryColor));
        mSwipeRefreshLayout.setProgressViewOffset(false, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,
                        getResources().getDisplayMetrics()));

        // Progress
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setRefreshing(true);
        mRecyclerView.setVisibility(View.GONE);

        getFileDir().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(file -> {
                    mFilesDir = file;
                    refreshTheList();
                });
    }

    private Observable<File> getFileDir() {
        return Observable.create(new Observable.OnSubscribe<File>() {
            @Override
            public void call(Subscriber<? super File> subscriber) {
                subscriber.onNext(CoreApplication.instance.getFilesDir());
                subscriber.onCompleted();
            }
        });
    }

    private void refreshTheList() {
        getApps()
                .filter(appInfo -> appInfo != null && appInfo.getName().contains("M"))
//                .map(appInfo -> {
//                    appInfo.setName("Handsome guy");
//                    return appInfo;
//                })
                .toSortedList()
                .subscribe(appInfos -> {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mAdapter.addApplications(appInfos);
                    mSwipeRefreshLayout.setRefreshing(false);
                    storeList(appInfos);
                }, throwable -> {
                    Toast.makeText(getActivity(), "Something went wrong!",
                            Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                }, () -> Toast.makeText(getActivity(), "Here is the list!",
                        Toast.LENGTH_LONG).show());
    }

    private void storeList(List<AppInfo> appInfos) {
        ApplicationsList.getInstance().setList(appInfos);

        Schedulers.io().createWorker().schedule(() -> {
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            Type appInfoType = new TypeToken<List<AppInfo>>() {
            }.getType();
            sharedPref.edit().putString("APPS", new Gson().toJson(appInfos, appInfoType)).apply();
        });
    }

    private Observable<AppInfo> getApps() {
        return Observable.create(subscriber -> {
            List<AppInfoRich> apps = new ArrayList<>();

            final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

            List<ResolveInfo> infos =
                    getActivity().getPackageManager().queryIntentActivities(mainIntent, 0);
            for (ResolveInfo info : infos) {
                apps.add(new AppInfoRich(getActivity(), info));
            }

            for (AppInfoRich appInfo : apps) {
                Bitmap icon = Utils.drawableToBitmap(appInfo.getIcon());
                String name = appInfo.getName();
                String iconPath = mFilesDir + "/" + name;
                Utils.storeBitmap(CoreApplication.instance, icon, name);

                if (subscriber.isUnsubscribed()) {
                    return;
                }
                subscriber.onNext(new AppInfo(name, iconPath, appInfo.getLastUpdateTime()));
            }
            if (!subscriber.isUnsubscribed()) {
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
