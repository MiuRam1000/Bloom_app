// di/appModule.kt
package com.example.bloom_app.di

import androidx.room.Room
import com.example.bloom_app.data.local.dao.DiscoveryDao
import com.example.bloom_app.data.local.database.BloomDatabase
import com.example.bloom_app.ui.auth.AuthViewModel
import com.example.bloom_app.ui.screen.journal.JournalViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Database AVEC allowMainThreadQueries pour debug
    single<BloomDatabase> {
        Room.databaseBuilder(
            androidContext(),
            BloomDatabase::class.java,
            "bloom_database"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()  // ← CRITIQUE pour debug
            .build()
    }

    // DAO explicite
    single<DiscoveryDao> { get<BloomDatabase>().discoveryDao() }

    // ViewModels
    viewModel { JournalViewModel(get()) }
    viewModel { AuthViewModel() }
}
