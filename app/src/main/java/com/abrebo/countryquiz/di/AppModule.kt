package com.abrebo.countryquiz.di

import android.content.Context
import com.abrebo.countryquiz.data.datasource.DataSource
import com.abrebo.countryquiz.data.datasource.GameDataSource
import com.abrebo.countryquiz.data.repo.Repository
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    annotation class Game1Collection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Game2Collection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Game3Collection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Game4Collection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Game6Collection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Game7Collection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Game8Collection


    @Provides
    @Singleton
    fun provideDataSource(@UsersCollection collectionReference: CollectionReference):DataSource{
        return DataSource(collectionReference)
    }
    @Provides
    @Singleton
    fun provideGameDataSource(@Game1Collection collectionReferenceGame1: CollectionReference,
                              @Game2Collection collectionReferenceGame2: CollectionReference,
                              @Game3Collection collectionReferenceGame3: CollectionReference,
                              @Game4Collection collectionReferenceGame4: CollectionReference,
                              @Game6Collection collectionReferenceGame6: CollectionReference,
                              @Game7Collection collectionReferenceGame7: CollectionReference,
                              @Game8Collection collectionReferenceGame8: CollectionReference):GameDataSource{
        return GameDataSource(collectionReferenceGame1,collectionReferenceGame2,collectionReferenceGame3,
            collectionReferenceGame4,collectionReferenceGame6,collectionReferenceGame7,collectionReferenceGame8)
    }

    @Provides
    @Singleton
    fun provideRepository(dataSource: DataSource,gameDataSource: GameDataSource,@ApplicationContext context: Context):Repository{
        return Repository(dataSource,gameDataSource,context)
    }

    @Provides
    @Singleton
    @UsersCollection
    fun provideCollectionReference():CollectionReference{
        return Firebase.firestore.collection("Users")
    }
    @Provides
    @Singleton
    @Game1Collection
    fun provideCollectionReferenceGame1():CollectionReference{
        return Firebase.firestore.collection("Game1")
    }
    @Provides
    @Singleton
    @Game2Collection
    fun provideCollectionReferenceGame2():CollectionReference{
        return Firebase.firestore.collection("Game2")
    }
    @Provides
    @Singleton
    @Game3Collection
    fun provideCollectionReferenceGame3():CollectionReference{
        return Firebase.firestore.collection("Game3")
    }
    @Provides
    @Singleton
    @Game4Collection
    fun provideCollectionReferenceGame4():CollectionReference{
        return Firebase.firestore.collection("Game4")
    }
    @Provides
    @Singleton
    @Game6Collection
    fun provideCollectionReferenceGame6():CollectionReference{
        return Firebase.firestore.collection("Game6")
    }
    @Provides
    @Singleton
    @Game7Collection
    fun provideCollectionReferenceGame7():CollectionReference{
        return Firebase.firestore.collection("Game7")
    }
    @Provides
    @Singleton
    @Game8Collection
    fun provideCollectionReferenceGame8():CollectionReference{
        return Firebase.firestore.collection("Game8")
    }


}