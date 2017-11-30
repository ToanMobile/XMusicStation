package com.toan_itc.core.base

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry

import com.toan_itc.core.base.event.Event
import com.toan_itc.core.base.event.LiveBus

/**
 * Created by Toan.IT on 11/30/17.
 * Email:Huynhvantoan.itc@gmail.com
 */
abstract class BaseViewModel : ViewModel(), Observable {
    @Transient
    private var mObservableCallbacks: PropertyChangeRegistry? = null
    private val mLiveBus = LiveBus()


    @Synchronized override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        if (mObservableCallbacks == null) {
            mObservableCallbacks = PropertyChangeRegistry()
        }
        mObservableCallbacks?.add(callback)
    }


    @Synchronized override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        mObservableCallbacks?.let {
            mObservableCallbacks?.remove(callback)
        }
    }


    @Synchronized
    fun notifyChange() {
        mObservableCallbacks?.let {
            mObservableCallbacks?.notifyCallbacks(this, 0, null)
        }
    }


    fun notifyPropertyChanged(fieldId: Int) {
        mObservableCallbacks?.let {
            mObservableCallbacks?.notifyCallbacks(this, fieldId, null)
        }
    }


    fun <T : Event> observeEvent(lifecycleOwner: LifecycleOwner, eventClass: Class<T>, observer: Observer<T>) {
        mLiveBus.observe(lifecycleOwner, eventClass, observer)
    }


    fun <T : Event> sendEvent(event: T) {
        mLiveBus.send(event)
    }
}
