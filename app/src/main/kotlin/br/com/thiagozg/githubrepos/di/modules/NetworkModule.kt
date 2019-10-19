package br.com.thiagozg.githubrepos.di.modules

import br.com.thiagozg.githubrepos.CustomApplication
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

    companion object {
        private const val TIMEOUT_SECONDS = 60
    }

    @Provides
    @Singleton
    fun provideHttpCache(app: CustomApplication): Cache {
        val cacheSize = 10L * 1024 * 1024
        return Cache(app.cacheDir, cacheSize)
    }

    @Provides
    @Singleton
    fun provideGson() = GsonBuilder().create()

    @Provides
    @Singleton
    fun providesOkHttpClient(cache: Cache): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC

        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .addNetworkInterceptor(logging)
            .addInterceptor(logging)
            .cache(cache)
            .build()
    }

}
