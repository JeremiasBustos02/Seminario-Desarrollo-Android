package ar.edu.unicen.seminario.di

import android.content.Context
import android.content.SharedPreferences
import ar.edu.unicen.seminario.BuildConfig
import ar.edu.unicen.seminario.data.api.GameApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Modulo de inyección de dependencias para la API de juegos.
 */
@Module
@InstallIn(SingletonComponent::class)
class GameModule {

    /**
     * Proporciona una instancia de Retrofit configurada con la URL base y el convertidor Gson.
     *
     * @return Instancia de Retrofit.
     */
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGamesApi(retrofit: Retrofit): GameApi {
        return retrofit.create(GameApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            "game_filters_prefs",
            Context.MODE_PRIVATE
        )
    }
}
