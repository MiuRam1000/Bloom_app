package com.example.bloom_app.di

import androidx.room.Room
import com.example.bloom_app.data.local.dao.DiscoveryDao
import com.example.bloom_app.data.local.database.BloomDatabase
import com.example.bloom_app.data.repository.DiscoveryRepositoryImpl  // ✅ Import manquant
import com.example.bloom_app.domaine.repository.DiscoveryRepository
import com.example.bloom_app.domaine.usecase.AddDiscoveryUseCase
import com.example.bloom_app.domaine.usecase.AnalyzeImageWithGeminiUseCase
import com.example.bloom_app.domaine.usecase.DeleteDiscoveryUseCase
import com.example.bloom_app.domaine.usecase.GetDiscoveriesUseCase
import com.example.bloom_app.ui.screen.auth.AuthViewModel
import com.example.bloom_app.ui.screen.capture.CaptureViewModel
import com.example.bloom_app.ui.screen.detail.DetailViewModel
import com.example.bloom_app.ui.screen.journal.JournalViewModel
import com.google.ai.client.generativeai.GenerativeModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // =================== DATABASE ===================
    single<BloomDatabase> {
        Room.databaseBuilder(
            androidContext(),
            BloomDatabase::class.java,
            "bloom_database"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()  // Debug only
            .build()
    }

    // DAO
    single<DiscoveryDao> { get<BloomDatabase>().discoveryDao() }

    // =================== REPOSITORY ===================
    single<DiscoveryRepository> {
        DiscoveryRepositoryImpl(get())  // ✅ Correct
    }

    // =================== USE CASES ===================
    single { GetDiscoveriesUseCase(get()) }
    single { AddDiscoveryUseCase(get()) }
    single { DeleteDiscoveryUseCase(get()) }
    single { AnalyzeImageWithGeminiUseCase(get()) }  // ✅ CORRIGÉ : get() au lieu de ()

    // =================== GEMINI AI ===================
    single<GenerativeModel> {
        GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = androidContext().getString(com.example.bloom_app.R.string.gemini_api_key)  // ✅ secrets.xml !
        )
    }

    // =================== VIEW MODELS ===================
    viewModel { AuthViewModel() }
    viewModel { JournalViewModel(get(), get()) }
    viewModel { CaptureViewModel(get(), get()) }
    viewModel { DetailViewModel(get(), get()) }
}
