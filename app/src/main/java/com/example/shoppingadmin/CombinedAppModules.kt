package com.example.shoppingadmin

import com.example.shoppingadmin.Data.DI.dataModule
import com.example.shoppingadmin.Domain.Di.domainModule
import com.example.shoppingadmin.Presentation.DI.presentationModule

// ✅ Koin Module (DI Container)
val CombinedappModules = listOf(
    dataModule,
    domainModule,
    presentationModule
)