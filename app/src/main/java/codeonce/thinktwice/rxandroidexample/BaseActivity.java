package codeonce.thinktwice.rxandroidexample;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * -> Created by PCPV on 3/16/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public void showNext(String itemString) {
        Toast.makeText(getApplicationContext(), itemString, Toast.LENGTH_SHORT).show();
    }

    public void showError(Throwable throwable) {
        Toast.makeText(getApplicationContext(), "Error  !", Toast.LENGTH_SHORT).show();
    }

    public void showComplete() {
        Toast.makeText(getApplicationContext(), "Complete !", Toast.LENGTH_SHORT).show();
    }
}


