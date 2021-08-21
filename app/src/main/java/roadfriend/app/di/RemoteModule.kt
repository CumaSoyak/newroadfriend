package roadfriend.app.di

import org.koin.dsl.module
import roadfriend.app.data.remote.service.IUserService
import roadfriend.app.data.remote.service.ServiceClient.createWebService
import roadfriend.app.data.remote.service.maps.IMapsService
import roadfriend.app.data.remote.service.notification.INotificationService
import roadfriend.app.data.remote.service.trip.ITripService

val remoteModule = module {
    factory { createWebService<ITripService>() }
    factory { createWebService<IMapsService>() }
    factory { createWebService<IUserService>() }
    factory { createWebService<INotificationService>() }

}