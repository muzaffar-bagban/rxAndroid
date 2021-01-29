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

        val tasks = DataSource.createTasksList()
        val task = tasks[0]

        val taskObservable: Observable<Task> = Observable
                .create(object : ObservableOnSubscribe<Task> {
                    override fun subscribe(emitter: ObservableEmitter<Task>?) {
                        emitter?.let {
                            if (!it.isDisposed) {
                                it.onNext(task)
                            }
                        } ?: return

                    }
                })
                .subscribeOn(Schedulers.io())
                .map {
                    Log.d(TAG, "inMap ${Thread.currentThread().name}")
                    Log.d(TAG, "inMap ${it?.description}")
                    Thread.sleep(1000)
                    it
                }
                .observeOn(AndroidSchedulers.mainThread())

        disposable.add(taskObservable.subscribe { t -> Log.d(TAG, "onNext: ${t?.description}") })

    }


    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}