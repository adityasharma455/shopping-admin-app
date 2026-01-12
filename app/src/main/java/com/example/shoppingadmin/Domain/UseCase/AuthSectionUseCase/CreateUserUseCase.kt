package com.example.shoppingadmin.Domain.UseCase.AuthSectionUseCase

import com.example.shoppingadmin.Domain.Models.AdminDataModel
import com.example.shoppingadmin.Domain.repo.Repo

class CreateUserUseCase (private val repo: Repo) {
    suspend fun registerAdmin(AdminData: AdminDataModel) = repo.registerAdminWithEmailAndPassword(AdminData)

    suspend fun signUpAdmin(AdminData: AdminDataModel)= repo.signInAdminWithEmailAndPassword(AdminData)

    suspend fun AdminSignOut() = repo.adminSignOut()

    suspend fun getCurrentAdmin () = repo.getCurrentAdmin()

    suspend fun updateAdminData(AdminData: AdminDataModel) = repo.UpdateAdimnData(AdminData)
}