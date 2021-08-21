package roadfriend.app.di

import org.koin.dsl.module
import roadfriend.app.data.local.LocalDataManager
import roadfriend.app.data.remote.RemoteDataManager
import roadfriend.app.data.repository.DataManager

val managerModule = module {
    single { RemoteDataManager(get(), get(), get(), get()) }
    single { LocalDataManager() }
    single { DataManager(get(), get()) }
}