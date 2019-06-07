package ru.lagarnikov.easyenglish13.domain.usecase

import io.reactivex.Observable
import io.reactivex.Scheduler
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription


abstract class UseCaseRxAbstract<T>(val threadExecutor: Scheduler, val postExecutionThread: Scheduler) {




    protected abstract fun buildUseCaseObservable(): Observable<T>

    fun execute(useCaseSubscriber: Subscriber<*>) {
         this.buildUseCaseObservable()
            .subscribeOn(threadExecutor)
            .observeOn(postExecutionThread)
            .subscribe()
    }


}