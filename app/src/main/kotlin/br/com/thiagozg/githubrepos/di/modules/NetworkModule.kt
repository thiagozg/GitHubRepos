package br.com.thiagozg.githubrepos.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import br.com.thiagozg.githubrepos.CustomApplication
import br.com.thiagozg.githubrepos.di.modules.cache.CacheInterceptor.requestInterceptor
import br.com.thiagozg.githubrepos.di.modules.cache.CacheInterceptor.responseInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpCache(context: Context): Cache {
        val cacheSize = 10L * 1024 * 1024
        val app = context as Application
        return Cache(app.cacheDir, cacheSize)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun providesOkHttpClient(
        context: Context,
        cache: Cache,
        encryptedSharedPreferences: SharedPreferences
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC

        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .cache(cache)
            .addNetworkInterceptor(logging)
            .addInterceptor(logging)
            .addNetworkInterceptor(responseInterceptor())
            .addInterceptor(requestInterceptor(context, encryptedSharedPreferences))
            .build()
    }

    companion object {
        private const val TIMEOUT_SECONDS = 60L
    }

}
