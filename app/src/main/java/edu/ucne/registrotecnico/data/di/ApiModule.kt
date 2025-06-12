package edu.ucne.registrotecnico.data.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.registrotecnico.data.remote.retenciones.RetencionManajerApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {
    private const val BASE_URL_Retenciones = "https://retencionesapi.azurewebsites.net/"
    private const val BASE_URL_Clientes = "https://clientesapi-g0a5d5ezhjcucwdy.eastus2-01.azurewebsites.net/swagger/index.html"



    @Provides
    @Singleton
    fun providesMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun providesRetencionManagerApi(moshi: Moshi): RetencionManajerApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_Retenciones)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(RetencionManajerApi::class.java)
    }

    //Hacer aqui lo mismo de retencion pero con clientes(Y EN LAS DEMAS CARPETAS PONER: DTO, MANAJER API,
// RESOURCE, ETC... DE CLIENTE COMO SE HIZO CON RETENCIONES!)

}
