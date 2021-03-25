package com.example.android.politicalpreparedness.di

import android.content.Context
import androidx.room.Room
import com.example.android.politicalpreparedness.database.ElectionDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {


    @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationCompenent (i.e. everywhere in the application)
    @Provides
    fun provideYourDatabase(
            @ApplicationContext context: Context
    ): ElectionDatabase {
        return Room.databaseBuilder(
                context.applicationContext,
                ElectionDatabase::class.java,
                "election_database"
        )
                .fallbackToDestructiveMigration()
                .build()


    }

    @Singleton
    @Provides
    fun provideYourDao(db: ElectionDatabase) = db.electionDao // The reason we can implement a Dao for the database


}