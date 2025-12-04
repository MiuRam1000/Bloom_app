package com.example.bloom_app.di

import androidx.room.Room
import com.example.bloom_app.R
import com.example.bloom_app.data.local.dao.DiscoveryDao
import com.example.bloom_app.data.local.database.BloomDatabase
import com.example.bloom_app.data.repository.DiscoveryRepositoryImpl
import com.example.bloom_app.domaine.repository.DiscoveryRepository
import com.example.bloom_app.domaine.usecase.*
import com.example.bloom_app.ui.screen.auth.AuthViewModel
import com.example.bloom_app.ui.screen.capture.CaptureViewModel
import com.example.bloom_app.ui.screen.detail.DetailViewModel
import com.example.bloom_app.ui.screen.journal.JournalViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // DATABASE
    single<BloomDatabase> {
        Room.databaseBuilder(
            androidContext(),
            BloomDatabase::class.java,
            "bloom_database"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    // TYPES EXPLICITES POUR KOIN
    single<DiscoveryDao> { get<BloomDatabase>().discoveryDao() }
    single<DiscoveryRepository> { DiscoveryRepositoryImpl(get()) }

    // USE CASES (TYPES EXPLICITES)
    single<GetDiscoveriesUseCase> { GetDiscoveriesUseCase(get()) }
    single<AddDiscoveryUseCase> { AddDiscoveryUseCase(get()) }
    single<DeleteDiscoveryUseCase> { DeleteDiscoveryUseCase(get()) }

    // GPT (type explicite)
    single<AnalyzeImageUseCase> {
        GrokAnalyzeImageUseCase(androidContext().getString(R.string.grok_api_key))
    }
    // VIEW MODELS (types explicites)
    viewModel { AuthViewModel() }
    viewModel {
        JournalViewModel(
            get<GetDiscoveriesUseCase>(),
            get<DeleteDiscoveryUseCase>()
        )
    }
    viewModel {
        CaptureViewModel(
            get<AnalyzeImageUseCase>(),
            get<AddDiscoveryUseCase>()
        )
    }
    viewModel {
        DetailViewModel(
            get<GetDiscoveriesUseCase>(),
            get<DeleteDiscoveryUseCase>()
        )
    }
}
