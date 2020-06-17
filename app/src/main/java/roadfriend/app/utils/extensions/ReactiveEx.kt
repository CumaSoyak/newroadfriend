package roadfriend.app.utils.extensions

import roadfriend.app.utils.rx.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Extension function to subscribe on the background thread and observe on the main thread for a [Completable]
 * */
fun Completable.performOnBackOutOnMain(): Completable {
    return this.subscribeOn(SchedulerProvider().ioScheduler())
        .observeOn(SchedulerProvider().uiScheduler())
}

/**
 * Extension function to subscribe on the background thread and observe on the main thread for a [Flowable]
 * */
fun <T> Flowable<T>.performOnBackOutOnMain(): Flowable<T> {
    return this.subscribeOn(SchedulerProvider().ioScheduler())
        .observeOn(SchedulerProvider().uiScheduler())
}

/**
 * Extension function to subscribe on the background thread and observe on the main thread  for a [Single]
 * */
fun <T> Single<T>.performOnBackOutOnMain(): Single<T> {
    return this.subscribeOn(SchedulerProvider().ioScheduler())
        .observeOn(SchedulerProvider().uiScheduler())
}

/**
 * Extension function to subscribe on the background thread and observe on the main thread for a [Observable]
 * */
fun <T> Observable<T>.performOnBackOutOnMain(): Observable<T> {
    return this.subscribeOn(SchedulerProvider().ioScheduler())
        .observeOn(SchedulerProvider().uiScheduler())
}

/**
 * Extension function to add a Disposable to a CompositeDisposable
 */
fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

/**
 * Extension function to subscribe on the background thread for a Flowable
 * */
fun <T> Flowable<T>.performOnBack(): Flowable<T> {
    return this.subscribeOn(SchedulerProvider().ioScheduler())
}

/**
 * Extension function to subscribe on the background thread for a Completable
 * */
fun Completable.performOnBack(): Completable {
    return this.subscribeOn(SchedulerProvider().ioScheduler())
}

/**
 * Extension function to subscribe on the background thread for a Observable
 * */
fun <T> Observable<T>.performOnBack(): Observable<T> {
    return this.subscribeOn(SchedulerProvider().ioScheduler())
}