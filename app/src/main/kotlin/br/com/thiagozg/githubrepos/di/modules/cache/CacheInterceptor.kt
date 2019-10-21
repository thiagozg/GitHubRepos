package br.com.thiagozg.githubrepos.di.modules.cache

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import br.com.thiagozg.githubrepos.base.isOnline
import com.bumptech.glide.Glide
import okhttp3.Interceptor
import okhttp3.Request

/*
 * Created by Thiago Zagui Giacomini on 20/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
object CacheInterceptor {

    const val SECOND = 1000L
    const val MINUTE = 60 * SECOND
    const val HOUR = 60 * MINUTE
    const val DAY = 24 * HOUR
    const val WEEK = 7 * DAY

    fun requestInterceptor(
        context: Context,
        encryptedSharedPreferences: SharedPreferences
    ) = Interceptor { chain ->
        chain.request().run {
            val request = if (context.isOnline()) {
                if (isPreviousCacheExpired(5 * MINUTE, encryptedSharedPreferences)) {
                    Glide.get(context).clearDiskCache()
                    storeCacheTime(encryptedSharedPreferences)
                    newBuilder()
                        .header("Cache-Control", "public, must-revalidate, max-age=" + 2 * MINUTE)
                        .removeHeader("Pragma")
                        .build()
                } else this
            } else {
                newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=$WEEK")
                    .removeHeader("Pragma")
                    .build()
            }
            chain.proceed(request)
        }
    }

    fun responseInterceptor() = Interceptor { chain ->
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

}