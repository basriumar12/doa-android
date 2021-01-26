package doa.ai

import android.app.Application
import android.content.Context
import com.androidnetworking.AndroidNetworking
import com.evernote.android.state.StateSaver
import com.facebook.stetho.Stetho
import com.google.firebase.messaging.FirebaseMessaging
import com.orhanobut.hawk.Hawk
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
        AndroidNetworking.initialize(getApplicationContext())
        val okHttpClient = OkHttpClient().newBuilder()
                .addNetworkInterceptor(HttpLoggingInterceptor())
                .build()
        AndroidNetworking.initialize(applicationContext, okHttpClient)
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        Hawk.init(this).build()
        Stetho.initializeWithDefaults(this);
        Stetho.initializeWithDefaults(this)
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build())
    }

    companion object {
        var context: Context? = null
            private set
    }



}