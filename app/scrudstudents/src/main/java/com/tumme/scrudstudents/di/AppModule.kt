package com.tumme.scrudstudents.di

import android.content.Context
import androidx.room.Room
import com.tumme.scrudstudents.data.local.AppDatabase
import com.tumme.scrudstudents.data.local.dao.CourseDao
import com.tumme.scrudstudents.data.local.dao.StudentDao
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext
import com.tumme.scrudstudents.data.local.dao.SubscribeDao

// Module Hilt définissant les dépendances injectables au niveau application (Singleton).
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Fournit l'instance unique de la base de données Room.
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "scrud-db").build()

    // Fournit le DAO Student depuis la base de données.
    @Provides fun provideStudentDao(db: AppDatabase): StudentDao = db.studentDao()

    // Fournit le DAO Course depuis la base de données.
    @Provides fun provideCourseDao(db: AppDatabase): CourseDao = db.courseDao()

    // Fournit le DAO Subscribe depuis la base de données.
    @Provides fun provideSubscribeDao(db: AppDatabase): SubscribeDao = db.subscribeDao()

    // Fournit le Repository unique avec les trois DAOs injectés.
    @Provides
    @Singleton
    fun provideRepository(studentDao: StudentDao, courseDao: CourseDao,
                          subscribeDao: SubscribeDao): SCRUDRepository =
        SCRUDRepository(studentDao, courseDao, subscribeDao)
}