package com.muzaffar.rxandroid

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.math.log

private const val TAG = "MainActivity_"

class MainActivity : AppCompatActivity() {

    val disposable by lazy { CompositeDisposable() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val taskObservable: Observable<Int> = Observable
                .range(0, 9)
                .subscribeOn(Schedulers.io())
                .repeat(3)
                .map {
                    Log.d(TAG, "inMap ${Thread.currentThread().name}")
                    Log.d(TAG, "inMap $it")
                    Thread.sleep(1000)
                    it * it
                }
                .observeOn(AndroidSchedulers.mainThread())

        disposable.add(taskObservable.subscribe { t -> Log.d(TAG, "onNext: $t") })

    }


    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}