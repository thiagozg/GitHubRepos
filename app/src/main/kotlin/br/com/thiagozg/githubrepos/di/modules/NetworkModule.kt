package br.com.thiagozg.githubrepos.di.modules

import android.content.Context
import br.com.thiagozg.githubrepos.CustomApplication
import br.com.thiagozg.githubrepos.base.isOnline
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import android.content.SharedPreferences
import android.icu.util.MeasureUnit.WEEK
import androidx.core.content.edit
import com.bumptech.glide.Glide


@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpCache(app: CustomApplication): Cache {
        val cacheSize = 10L * 1024 * 1024
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
            .addNetworkInterceptor(responseInterceptor)
            .addInterceptor(requestInterceptor(context, encryptedSharedPreferences))
            .build()
    }

    private fun requestInterceptor(
        context: Context,
        encryptedSharedPreferences: SharedPreferences
    ) = Interceptor { chain ->
        var request = chain.request()
        request = if (context.isOnline()) {
            if (request.isPreviousCacheExpired(3 * MINUTE, encryptedSharedPreferences)) {
                request.also {
                    it.storeCacheTime(encryptedSharedPreferences)
                }.run {
                    newBuilder()
                        .header("Cache-Control", "public, must-revalidate, max-age=" + 2 * MINUTE)
                        .removeHeader("Pragma")
                        .build()
                }
            } else {
                request.also {
                    Glide.get(context).clearDiskCache()
                }
            }
        } else {
            request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=$WEEK")
                .removeHeader("Pragma")
                .build()
        }
        chain.proceed(request)
    }

    private val responseInterceptor = Interceptor { chain ->
        val originalResponse = chain.proceed(chain.request())
        val cacheControl = originalResponse.header("Cache-Control")
        if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache")
            || cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
            originalResponse.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, max-age=$WEEK")
                .build()
        } else {
            originalResponse
        }
    }

    private fun Request.isPreviousCacheExpired(
        maxTimeMillis: Long,
        encryptedSharedPreferences: SharedPreferences
    ): Boolean {
        val previousRequestMaxAgeTimeMillis = encryptedSharedPreferences.getLong(generateKey(), 0)
        return if (previousRequestMaxAgeTimeMillis > 0) {
            val timeExpended = System.currentTimeMillis() - previousRequestMaxAgeTimeMillis
            timeExpended > maxTimeMillis
        } else true
    }

    private fun Request.storeCacheTime(encryptedSharedPreferences: SharedPreferences) {
        encryptedSharedPreferences.edit {
            putLong(generateKey(), System.currentTimeMillis())
        }
    }

    private fun Request.generateKey() = "${url()}${body()}${headers()}"

    companion object {
        private const val TIMEOUT_SECONDS = 60L
        private const val SECOND = 1000L
        private const val MINUTE = 60 * SECOND
        private const val HOUR = 60 * MINUTE
        private const val DAY = 24 * HOUR
        private const val WEEK = 7 * DAY
    }

}
