package br.com.thiagozg.githubrepos.di.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import br.com.thiagozg.githubrepos.CustomApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AndroidModule {

    @Provides
    @Singleton
    fun provideContext(application: CustomApplication): Context = application.applicationContext

    @Provides
    @Singleton
    fun providesEncryptSharedPreferences(context: Context): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            SECRETE_SHARED_PREFS,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    companion object {
        private const val SECRETE_SHARED_PREFS = "secret_shared_prefs"
    }

}
