package roadfriend.app.di

import roadfriend.app.ui.add.AddViewModel
import roadfriend.app.ui.add.detail.AddDetailViewModel
import roadfriend.app.ui.message.chat.ChatViewModel
import roadfriend.app.ui.add.direction.AddDirectionViewModel
import roadfriend.app.ui.searchcity.SearchCityViewModel
import roadfriend.app.ui.auth.AuthViewModel
import roadfriend.app.ui.auth.login.LoginViewModel
import roadfriend.app.ui.auth.register.RegisterViewMoel
import roadfriend.app.ui.message.MessageViewModel
import roadfriend.app.ui.biddetail.BidDetailViewModel
import roadfriend.app.ui.dashboard.DashBoardViewModel
import roadfriend.app.ui.home.HomeViewModel
import roadfriend.app.ui.intro.IntroViewModel
import roadfriend.app.ui.main.MainViewModel
import roadfriend.app.ui.maps.MapViewModel
import roadfriend.app.ui.maps.MapsDialogViewModel
import roadfriend.app.ui.notification.NotificationViewModel
import roadfriend.app.ui.profile.ProfilViewModel
import roadfriend.app.ui.profile.help.HelpViewModel
import roadfriend.app.ui.profile.myaboutcomment.MyAboutCommentsViewModel
import roadfriend.app.ui.profile.mytrip.MyTripsViewModel
import roadfriend.app.ui.profile.savedtrip.SavedTripViewModel
import roadfriend.app.ui.profile.setting.ChangePasswordViewModel
import roadfriend.app.ui.profile.setting.ProfilSettingsViewModel
import roadfriend.app.ui.sales.SalesViewModel
import roadfriend.app.ui.splash.SplashViewModel
import roadfriend.app.ui.tripdetail.TripDetailViewModel
import roadfriend.app.ui.userdetail.UserDetailViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import roadfriend.app.ui.search.SearchViewModel

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { AddViewModel(get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { ChatViewModel(get()) }
    viewModel { AddDirectionViewModel(get()) }
    viewModel { SearchCityViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { AddDetailViewModel(get()) }
    viewModel { BidDetailViewModel(get()) }
    viewModel { MessageViewModel(get()) }
    viewModel { MapsDialogViewModel(get()) }
    viewModel { NotificationViewModel(get()) }
    viewModel { ProfilViewModel(get()) }
    viewModel { TripDetailViewModel(get()) }
    viewModel { UserDetailViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewMoel(get()) }
    viewModel { ProfilSettingsViewModel(get()) }
    viewModel { ChangePasswordViewModel(get()) }
    viewModel { HelpViewModel(get()) }
    viewModel { MyAboutCommentsViewModel(get()) }
    viewModel { MyTripsViewModel(get()) }
    viewModel { SavedTripViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { MapViewModel(get()) }
    viewModel { SalesViewModel(get()) }
    viewModel { IntroViewModel(get()) }
    viewModel { DashBoardViewModel(get()) }
    viewModel { SearchViewModel(get()) }



}