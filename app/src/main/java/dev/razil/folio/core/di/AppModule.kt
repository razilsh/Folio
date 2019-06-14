/*
 * MIT License
 *
 * Copyright (postChannel) 2019 Razil
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.razil.folio.core.di

import androidx.room.Room
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dev.razil.folio.BuildConfig
import dev.razil.folio.Folio
import dev.razil.folio.core.repository.PostRepository
import net.dean.jraw.android.AndroidHelper
import net.dean.jraw.android.ManifestAppInfoProvider
import net.dean.jraw.android.SharedPreferencesTokenStore
import net.dean.jraw.oauth.AccountHelper
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import timber.log.Timber
import java.util.*
import javax.inject.Singleton


@Module(includes = [ViewModelModule::class])
object AppModule {

    @Provides
    @JvmStatic
    @Singleton
    fun provideJsonParser(): Moshi = Moshi.Builder().build()

    @Provides
    @JvmStatic
    @Singleton
    fun provideOkHttpClient(appContext: Folio): OkHttpClient {
        val loggingInterceptor: HttpLoggingInterceptor =
            HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                Timber.i(it)
            }).apply {
                level = if (BuildConfig.DEBUG) BODY else NONE
            }
        return OkHttpClient
            .Builder()
            .addInterceptor(loggingInterceptor)
            .cache(Cache(appContext.cacheDir, 10 * 1024 * 1024))
            .build()
    }

    @Provides
    @JvmStatic
    @Singleton
    fun provideAppInfoProvider(appContext: Folio): ManifestAppInfoProvider {
        return ManifestAppInfoProvider(appContext)
    }

    @Provides
    @JvmStatic
    @Singleton
    fun provideTokenStore(appContext: Folio): SharedPreferencesTokenStore {
        return SharedPreferencesTokenStore(appContext)
    }

    @Provides
    @JvmStatic
    @Singleton
    fun provideAccountHelper(
        appInfoProvider: ManifestAppInfoProvider,
        tokenStore: SharedPreferencesTokenStore
    ): AccountHelper {
        val deviceUuid = UUID.randomUUID()
        return AndroidHelper.accountHelper(appInfoProvider, deviceUuid, tokenStore)
    }

    @Provides
    @JvmStatic
    @Singleton
    fun provideSubRepository(
        accountHelper: AccountHelper
    ): PostRepository {
        return PostRepository(accountHelper)
    }
}