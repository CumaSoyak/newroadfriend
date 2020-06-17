package roadfriend.app.data.repository

import roadfriend.app.data.local.ILocalDataManager
import roadfriend.app.data.remote.IRemoteDataManager

interface IDataManager : IRemoteDataManager, ILocalDataManager {

}