package roadfriend.app.utils.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SchedulerProvider : ISchedulerProvider {
    override fun ioNewScheduler(): Scheduler = Schedulers.newThread()

    override fun ioScheduler() = Schedulers.io()

    override fun uiScheduler(): Scheduler = AndroidSchedulers.mainThread()
}