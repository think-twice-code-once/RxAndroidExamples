package codeonce.thinktwice.rxandroidexample;

import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import codeonce.thinktwice.rxandroidexample.fragments.FirstExampleFragment;
import codeonce.thinktwice.rxandroidexample.fragments.FirstExampleFragment_;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @AfterViews
    void init() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_ll_container, new FirstExampleFragment_(),
                        FirstExampleFragment.class.getSimpleName())
                .commit();
    }
}
