package com.muzaffar.rxandroid

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.FlowableSubscriber
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.reactivestreams.Subscription

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

        val flowable = taskObservable.toFlowable(BackpressureStrategy.BUFFER)

        flowable.subscribe(object : FlowableSubscriber<Task> {
            override fun onComplete() {
                Log.d(TAG, "onComplete: called")
            }

            override fun onSubscribe(s: Subscription?) {
                Log.d(TAG, "onSubscribe: called --- $s")
            }

            override fun onNext(t: Task?) {
                Log.d(TAG, "onNext: ${t?.description}")
            }

            override fun onError(t: Throwable?) {
                Log.d(TAG, "onError: $t")
            }

        })

    }
}