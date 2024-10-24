package com.abrebo.countryquiz.di

import com.abrebo.countryquiz.data.datasource.DataSource
import com.abrebo.countryquiz.data.repo.CountriesPopulationRepository
import com.abrebo.countryquiz.data.repo.Repository
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class UsersCollection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class UserScoresCollection


    @Provides
    @Singleton
    fun provideDataSource(@UsersCollection collectionReference: CollectionReference,
                          @UserScoresCollection collectionReferenceUserScores: CollectionReference):DataSource{
        return DataSource(collectionReference,collectionReferenceUserScores)
    }

    @Provides
    @Singleton
    fun provideRepository(dataSource: DataSource):Repository{
        return Repository(dataSource)
    }
    @Provides
    @Singleton
    fun provideCountriesPopulationRepository(): CountriesPopulationRepository {
        return CountriesPopulationRepository()
    }

    @Provides
    @Singleton
    @UsersCollection
    fun provideCollectionReference():CollectionReference{
        return Firebase.firestore.collection("Users")
    }
    @Provides
    @Singleton
    @UserScoresCollection
    fun provideCollectionReferenceUserScores():CollectionReference{
        return Firebase.firestore.collection("UserScores")
    }

}