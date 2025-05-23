package edu.ucne.registrotecnico.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.registrotecnico.data.local.dao.MensajeDao
import edu.ucne.registrotecnico.data.local.database.TecnicoDb
import dagger.hilt.android.qualifiers.ApplicationContext

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext applicationContext: Context): TecnicoDb {
        return Room.databaseBuilder(
            applicationContext,
            TecnicoDb::class.java,
            "TecnicoDb"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideTecnicoDao(appDataDb: TecnicoDb) = appDataDb.tecnicoDao()

    @Provides
    @Singleton
    fun provideTicketDao(appDataDb: TecnicoDb) = appDataDb.ticketDao()


    @Provides
    fun provideMensajeDao(db: TecnicoDb): MensajeDao {
        return db.mensajeDao()
    }


}