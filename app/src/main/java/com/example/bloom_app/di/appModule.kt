// di/appModule.kt
package com.example.bloom_app.di

import androidx.room.Room
import com.example.bloom_app.data.local.database.BloomDatabase
import com.example.bloom_app.ui.auth.AuthViewModel
import com.example.bloom_app.ui.screen.journal.JournalViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Database + DAO
    single {
        Room.databaseBuilder(
            androidContext(),
            BloomDatabase::class.java,
            "bloom_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<BloomDatabase>().discoveryDao() }

    // ViewModel
    viewModel { JournalViewModel(get()) }
    viewModel { AuthViewModel() }
}