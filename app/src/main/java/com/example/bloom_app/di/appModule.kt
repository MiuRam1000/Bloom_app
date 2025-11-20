
package com.example.bloom_app.di

import androidx.room.Room
import com.example.bloom_app.data.local.database.BloomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            BloomDatabase::class.java,
            "bloom_database"
        ).fallbackToDestructiveMigration()
            .build()
    }
    single { get<BloomDatabase>().discoveryDao() }
}