package com.test.perqaratest.di

import android.app.Application
import androidx.room.Room
import com.test.perqaratest.data.local.GameDatabase
import com.test.perqaratest.data.remote.RawrService
import com.test.perqaratest.data.repository.GameRepositoryImpl
import com.test.perqaratest.domain.repository.GameRepository
import com.test.perqaratest.utils.DispatcherProvider
import com.test.perqaratest.utils.StandardDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDispatchers(): DispatcherProvider = StandardDispatchers()

    @Provides
    @Singleton
    fun provideService(): RawrService =
        Retrofit.Builder()
            .baseUrl(RawrService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RawrService::class.java)

    @Provides
    @Singleton
    fun provideGameDb(application: Application): GameDatabase =
        Room.databaseBuilder(
            application,
            GameDatabase::class.java,
            GameDatabase.dbName
        ).build()

    @Provides
    @Singleton
    fun provideGameRepository(service: RawrService, db: GameDatabase, dispatcherProvider: DispatcherProvider): GameRepository = GameRepositoryImpl(service, db.dao, dispatcherProvider.io)
}