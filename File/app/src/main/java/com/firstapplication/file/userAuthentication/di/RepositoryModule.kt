package com.firstapplication.file.userAuthentication.di


import com.firstapplication.file.userAuthentication.firestoredb.repository.FirestoreDbRepositoryImpl
import com.firstapplication.file.userAuthentication.firestoredb.repository.FirestoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule
{
    @Binds
    abstract fun providesFirestoreRepository(
        repo: FirestoreDbRepositoryImpl
    ): FirestoreRepository


}