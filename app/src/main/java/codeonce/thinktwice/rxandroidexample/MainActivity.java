package codeonce.thinktwice.rxandroidexample;

import android.util.Log;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import codeonce.thinktwice.rxandroidexample.fragments.FirstExampleFragment;
import codeonce.thinktwice.rxandroidexample.fragments.FirstExampleFragment_;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.subjects.PublishSubject;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    Subscription subscription;

    @AfterViews
    void init() {


        FirstExampleFragment firstExampleFragment = new FirstExampleFragment_();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_ll_container, firstExampleFragment,
                        FirstExampleFragment.class.getSimpleName())
                .commit();

//        observableCreateExample();

//        observableFromExample();

//        observableJustExample();

//        publishSubjectExample();

//        publishSubjectExample2();

//        Observable.defer(this::getStringObservable);

//        rangeObservableExample();

//        timerObservable();

//        flatMapExample();

        debounceExample();
    }

    private Observable<String> getStringObservable() {
        return Observable.create(subscriber -> {

        });
    }

    private void observableCreateExample() {
        Observable<String> stringObservable = Observable.create(subscriber -> {
            subscriber.onNext("Good job !");
        });

        stringObservable.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void observableFromExample() {
        List<String> listData = new ArrayList<>();
        listData.add("Item  1");
        listData.add("Item  2");
        listData.add("Item  3");
        listData.add("Item  4");
        listData.add("Item  5");

        Observable<String> observableString = Observable.from(listData);
        observableString.subscribe(this::showNext, this::showError, this::showComplete);

    }

    private void observableJustExample() {

        List<String> listData = new ArrayList<>();
        listData.add("Item  1");
        listData.add("Item  2");
        listData.add("Item  3");
        listData.add("Item  4");
        listData.add("Item  5");

        Observable<String> observableString = Observable.just(hello(), "Good job");
        observableString.subscribe(this::showNext, this::showError);
    }

    private String hello() {
        return "Hello I love you !";
    }

    private void publishSubjectExample() {
        PublishSubject<String> publishSubject = PublishSubject.create();
        publishSubject.subscribe(this::showNext);
        publishSubject.onNext("Hey 1");
        publishSubject.onNext("Hey 2");
        publishSubject.onNext("Hey 3");
        publishSubject.onCompleted();
    }

    private void publishSubjectExample2() {
        PublishSubject<Boolean> publishSubject = PublishSubject.create();
        publishSubject.subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                Toast.makeText(MainActivity.this, "Complete OK 123 !", Toast.LENGTH_SHORT).show();
            }
        });

        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(123);
                subscriber.onCompleted();
            }
        }).doOnCompleted(new Action0() {
            @Override
            public void call() {
                publishSubject.onNext(true);
            }
        }).subscribe();
    }

    private void rangeObservableExample() {
        Observable.range(10, 3)
                .subscribe(x -> {
                    Toast.makeText(getApplicationContext(), x + "", Toast.LENGTH_SHORT).show();
                });
    }

    private void timerObservable() {
        List<String> listData = new ArrayList<>();
        listData.add("Item  1");
        listData.add("Item  2");
        listData.add("Item  3");
        listData.add("Item  4");
        listData.add("Item  5");

        subscription = Observable.interval(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                            Toast.makeText(getApplicationContext(), "OK !", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), aLong + " : ---> ", Toast.LENGTH_SHORT).show();
                            Log.d("TestRxAndroid", "What the hell !");
                            if (aLong > 10) {
                                ok();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Error !", Toast.LENGTH_SHORT).show();
                        },
                        () -> {
                            Toast.makeText(getApplicationContext(), "Complete roi em !", Toast.LENGTH_SHORT).show();
                        });
    }

    private void ok() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    private void flatMapExample() {
        Observable.defer(() -> Observable.just(9, 9, 9))
                .flatMap(integer -> Observable.range(4,2))
                .subscribe(integer -> Log.d("TestRxAndroid", integer + " : OK"));
    }

    private void debounceExample() {
        Observable.defer(() -> Observable.just(1, 2, 3))
                .debounce(1, TimeUnit.SECONDS)
                .subscribe(integer -> Log.d("TestRxAndroid", integer + " : OK"));
    }

}
