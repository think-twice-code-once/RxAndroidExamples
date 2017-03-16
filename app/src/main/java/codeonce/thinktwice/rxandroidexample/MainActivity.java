package codeonce.thinktwice.rxandroidexample;

import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.List;

import codeonce.thinktwice.rxandroidexample.fragments.FirstExampleFragment;
import codeonce.thinktwice.rxandroidexample.fragments.FirstExampleFragment_;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subjects.PublishSubject;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {


    @AfterViews
    void init() {


        FirstExampleFragment firstExampleFragment = new FirstExampleFragment_();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_ll_container, firstExampleFragment,
                        FirstExampleFragment.class.getSimpleName())
                .commit();

//        observableCreateExample();\

//        observableFromExample();

//        observableJustExample();

//        publishSubjectExample();

//        publishSubjectExample2();

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

}
