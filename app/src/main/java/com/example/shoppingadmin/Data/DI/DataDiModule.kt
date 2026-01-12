package com.example.shoppingadmin.Data.DI

import com.example.shoppingadmin.BuildConfig
import com.example.shoppingadmin.Data.PushNotification.PushNotification
import com.example.shoppingadmin.Data.RepoImplementation.RepoImplementation
import com.example.shoppingadmin.Domain.repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import org.koin.dsl.module


val dataModule = module {
    // Firebase
    single<FirebaseFirestore> { FirebaseFirestore.getInstance() }
    single<FirebaseAuth> { FirebaseAuth.getInstance() }
    single<FirebaseMessaging> { FirebaseMessaging.getInstance() }

    // Supabase
    single<SupabaseClient> {
        createSupabaseClient(
            supabaseUrl = "https://fvhaiudcdecafujfsibg.supabase.co",
            supabaseKey = BuildConfig.API_KEY
        ) { install(Storage) }
    }

    single {
        PushNotification(
            firestore = get(), // gets FirebaseFirestore from Koin
            context = get()    // gets Application Context from Koin
        )
    }
    // Repositories
    single<Repo> { RepoImplementation(
        firestore = get(), client = get(), pushNotification = get(),
        firebaseAuth = get()
    ) }
}