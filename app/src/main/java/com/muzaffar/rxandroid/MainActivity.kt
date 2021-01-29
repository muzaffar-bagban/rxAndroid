package com.muzaffar.rxandroid

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.math.log

private const val TAG = "MainActivity_"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val taskObservable: Observable<Task> = Observable
                .fromIterable(DataSource.createTasksList())
                .subscribeOn(Schedulers.io())
                .map {
                    Log.d(TAG, "inMap ${Thread.currentThread().name}")
                    Log.d(TAG, "inMap ${it?.description}")
                    Thread.sleep(1000)
                    it
                }
                .observeOn(AndroidSchedulers.mainThread())

        taskObservable.subscribe(object : Observer<Task> {
            override fun onComplete() {
                Log.d(TAG, "onComplete: called")
            }

            override fun onSubscribe(d: Disposable?) {
                Log.d(TAG, "onSubscribe: called")
            }

            override fun onNext(t: Task?) {
                Log.d(TAG, "onNext: ${t?.description}")
            }

            override fun onError(e: Throwable?) {
                Log.d(TAG, "onError: $e")
            }

        })

    }
}