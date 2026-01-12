package com.example.shoppingadmin.Presentation.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingadmin.Common.ResultState
import com.example.shoppingadmin.Domain.Models.AdminDataModel
import com.example.shoppingadmin.Domain.Models.BannerDataModel
import com.example.shoppingadmin.Domain.Models.CategoryModel
import com.example.shoppingadmin.Domain.Models.OrderDataModel
import com.example.shoppingadmin.Domain.Models.ProductModel
import com.example.shoppingadmin.Domain.UseCase.AuthSectionUseCase.CreateUserUseCase
import com.example.shoppingadmin.Domain.UseCase.BannerUseCase.AddBannerModelPhotoUserUseCase
import com.example.shoppingadmin.Domain.UseCase.BannerUseCase.AddBannerUserUserCase
import com.example.shoppingadmin.Domain.UseCase.BannerUseCase.DeleteBannerImgaeUseCase
import com.example.shoppingadmin.Domain.UseCase.BannerUseCase.DeleteBannerUseCase
import com.example.shoppingadmin.Domain.UseCase.BannerUseCase.GetAllBannersUseCase
import com.example.shoppingadmin.Domain.UseCase.BannerUseCase.GetBannerByIdUseCase
import com.example.shoppingadmin.Domain.UseCase.BannerUseCase.UpdateBannerUseCase
import com.example.shoppingadmin.Domain.UseCase.CategoryUseCase.AddCategoryUseCase
import com.example.shoppingadmin.Domain.UseCase.CategoryUseCase.GetAllCategoryUseCase
import com.example.shoppingadmin.Domain.UseCase.OrderUseCase.GetAllOrdersUseCase
import com.example.shoppingadmin.Domain.UseCase.OrderUseCase.UpdateOrderStatusUseCase
import com.example.shoppingadmin.Domain.UseCase.ProductUseCase.AddProductPhotoUseCase
import com.example.shoppingadmin.Domain.UseCase.ProductUseCase.AddProductUseCase
import com.example.shoppingadmin.Domain.UseCase.ProductUseCase.AllAdminProductsUseCase
import com.example.shoppingadmin.Domain.UseCase.ProductUseCase.GetProductsByCategory
import com.example.shoppingadmin.Domain.UseCase.ProductUseCase.GetSpecificProductAdminUseCase
import com.example.shoppingadmin.Domain.UseCase.ProductUseCase.RemoveSpecificProductAdminUseCaes
import com.example.shoppingadmin.Domain.UseCase.ProductUseCase.UpdateProductDataAdminUseCase
import com.example.shoppingadmin.Domain.UseCase.SearchSectionUseCase.SearchProductUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class MyViewModel(
    private val addCategoryUseCase: AddCategoryUseCase,
    private val getCategoryUseCase: GetAllCategoryUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val addProductPhotoUseCase: AddProductPhotoUseCase,
    private val addBannerUserUserCase: AddBannerUserUserCase,
    private val addBannerModelPhotoUserUseCase: AddBannerModelPhotoUserUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val getAllAdminProductsUseCase: AllAdminProductsUseCase,
    private val getSpecificProductAdminUseCase: GetSpecificProductAdminUseCase,
    private val removeSpecificProductAdminUseCaes: RemoveSpecificProductAdminUseCaes,
    private val updateProductDataAdminUseCase: UpdateProductDataAdminUseCase,
    private val getProductsByCategory: GetProductsByCategory,
    private val getSearchProductUseCase: SearchProductUseCase,
    private val getAllBannersUseCase: GetAllBannersUseCase,
    private val getBannerByIdUseCase: GetBannerByIdUseCase,
    private val deleteBannerImgaeUseCase: DeleteBannerImgaeUseCase,
    private val deleteBannerUseCase: DeleteBannerUseCase,
    private val updateBannerUseCase: UpdateBannerUseCase,
    private val getAllOrdersUseCase: GetAllOrdersUseCase,
    private  val updateOrderUseCase: UpdateOrderStatusUseCase
) : ViewModel() {

    val _SearchQuery = MutableStateFlow("")

    private val _getAllBannersState  = MutableStateFlow(GetAllBannersScreenState())
    val getAllBannersState = _getAllBannersState.asStateFlow()

    private val _updateBannerState = MutableStateFlow(UpdateBannerScreenState())
    val updateBannerState = _updateBannerState.asStateFlow()

    private val _searchProductState = MutableStateFlow(SearchProductScreen())
    val searchProductState = _searchProductState.asStateFlow()

    private val _getCurrentAdminState = MutableStateFlow(GetCurrentAdminScreenState())
    val getCurrentAdminState= _getCurrentAdminState.asStateFlow()

    private val _addCategory = MutableStateFlow(AddCategoryState())
    val addCategory = _addCategory.asStateFlow()

    private val _getProductsByCategoryState = MutableStateFlow(GetProductsByCategoryScreenState())
    val getProductsByCategoryState = _getProductsByCategoryState.asStateFlow()

    private val _getSpecificProductState = MutableStateFlow(GetSpecificProductScreenState())
    val getSpecificProductState = _getSpecificProductState.asStateFlow()

    private val _getCategory = MutableStateFlow(GetCategoryState())
    val getCategory = _getCategory.asStateFlow()

    private val _addProduct = MutableStateFlow(AddProductState())
    val addProduct = _addProduct.asStateFlow()

    private val _addProductPhoto = MutableStateFlow(AddProductPhotoState())
    val addProductPhoto = _addProductPhoto.asStateFlow()

    private val _addBannerModelState = MutableStateFlow(AddBannerScreenState())
    val addBannerModelState = _addBannerModelState.asStateFlow()

    private val _addBannerModelPhotoState = MutableStateFlow(AddBannerScreenPhotoState())
    val addBannerModelPhotoState = _addBannerModelPhotoState.asStateFlow()

    private val _authScreenState = MutableStateFlow<AuthScreenState>(AuthScreenState.Idle)
     val authScreenState = _authScreenState.asStateFlow()

    private val _allAdminProductsState = MutableStateFlow(AllAdminBrandProductsScreenState())
    val allAdminProductsState = _allAdminProductsState.asStateFlow()

    private val _updateAdminState = MutableStateFlow(UpdateAdminScreenState())
    val updateAdminState = _updateAdminState.asStateFlow()
    private val _removeSpecificProductState = MutableStateFlow(RemoveSpecificProductScreenState())
    val removeSpecificProductState = _removeSpecificProductState.asStateFlow()

    private val _updateProductDataState = MutableStateFlow(UpdateProductDataScreenState())
    val updateProductDataState = _updateProductDataState.asStateFlow()

    // State flows
    private val _deleteBannerState = MutableStateFlow(DeleteBannerScreenState())
    val deleteBannerState = _deleteBannerState.asStateFlow()

    private val _bannerDetailsState = MutableStateFlow(BannerDetailsScreenState())
    val bannerDetailsState = _bannerDetailsState.asStateFlow()

    private val _getAllOrdersState = MutableStateFlow(GetAllOrderScreenState())
    val getAllOrdersState = _getAllOrdersState.asStateFlow()

    private val _updateOrderState = MutableStateFlow(UpdateOrderState())
    val updateOrderState = _updateOrderState.asStateFlow()

    fun updateOrderStatus(order: OrderDataModel, status: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updateOrderUseCase.updateOrderStatusUseCase(order, status).collectLatest { result ->
                Log.d("AdminViewModel", "🎯 Update result: $result")

                when (result) {
                    is ResultState.Loading -> {
                        _updateOrderState.value = UpdateOrderState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _updateOrderState.value = UpdateOrderState(isSuccess = true)
                        // Refresh list automatically
                        getAllOrders()
                    }
                    is ResultState.Error -> {
                        _updateOrderState.value = UpdateOrderState(
                            Error = result.message ?: "Error updating order"
                        )
                    }
                }
            }
        }
    }

    fun resetUpdateOrderState() {
        _updateOrderState.value = UpdateOrderState()
    }

    fun getCurrentAdmin(){
        viewModelScope.launch {
            createUserUseCase.getCurrentAdmin().collect { result->
                when(result){
                    is ResultState.Loading ->{
                        _getCurrentAdminState.value = GetCurrentAdminScreenState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _getCurrentAdminState.value  = GetCurrentAdminScreenState(isSuccess = result.data)
                    }
                    is ResultState.Error ->{
                        _getCurrentAdminState.value = GetCurrentAdminScreenState(Error = result.message.toString())
                    }
                }
            }
        }
    }

    fun updateCurrentAdmin(admin: AdminDataModel) {
        viewModelScope.launch {
            createUserUseCase.updateAdminData(admin).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _updateAdminState.value = UpdateAdminScreenState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _updateAdminState.value =
                            UpdateAdminScreenState(isSuccess = result.data)
                    }
                    is ResultState.Error -> {
                        _updateAdminState.value =
                            UpdateAdminScreenState(Error = result.message)
                    }
                }
            }
        }
    }


    fun getAllOrders(){
        viewModelScope.launch(Dispatchers.IO) {
            getAllOrdersUseCase.getAllOrdersUseCase().collectLatest{result->
                when(result){
                    ResultState.Loading -> {
                        _getAllOrdersState.value = GetAllOrderScreenState(isLoading = true)
                    }
                    is ResultState.Success<List<OrderDataModel>> -> {
                        _getAllOrdersState.value = GetAllOrderScreenState(
                            isSuccess = result.data,
                            isLoading = false
                        )
                    }

                    is ResultState.Error -> {
                        _getAllOrdersState.value = GetAllOrderScreenState(
                            Error = result.message.toString(),
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun getAllBanners(){
        viewModelScope.launch(Dispatchers.IO) {


            getAllBannersUseCase.getAllBannersUseCase().collectLatest { result->
                when(result){
                    is ResultState.Loading->{
                        _getAllBannersState.value = GetAllBannersScreenState(isLoading = true)
                    }
                    is ResultState.Success->{
                        _getAllBannersState.value = GetAllBannersScreenState(
                            isSuccess = result.data,
                            isLoading = false
                        )
                    }
                    is ResultState.Error->{
                        _getAllBannersState.value = GetAllBannersScreenState(
                            Error = result.message.toString(),
                            isLoading = false
                        )
                    }
                }
            }

        }
    }
    // Functions
    fun getBannerById(bannerId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getBannerByIdUseCase.getBannerByIdUseCase(bannerId).collectLatest { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _bannerDetailsState.value = BannerDetailsScreenState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _bannerDetailsState.value = BannerDetailsScreenState(
                            isSuccess = result.data,
                            isLoading = false
                        )
                    }
                    is ResultState.Error -> {
                        _bannerDetailsState.value = BannerDetailsScreenState(
                            Error = result.message.toString(),
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun deleteBanner(banner: BannerDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            // Check if bannerId is valid
            if (banner.bannerId.isNullOrBlank()) {
                _deleteBannerState.value = DeleteBannerScreenState(
                    Error = "Invalid banner ID",
                    isLoading = false
                )
                return@launch
            }

            _deleteBannerState.value = DeleteBannerScreenState(isLoading = true)

            // First delete from Supabase (image)
            deleteBannerImgaeUseCase.deleteBannerImage(banner.imageUrl.toString()).collectLatest { supabaseResult ->
                when (supabaseResult) {
                    is ResultState.Success -> {
                        // Then delete from Firebase (document)
                       deleteBannerUseCase.deleteBannerUseCase(banner.bannerId.toString()).collectLatest { firebaseResult ->
                            when (firebaseResult) {
                                is ResultState.Success -> {
                                    _deleteBannerState.value = DeleteBannerScreenState(
                                        isSuccess = true,
                                        isLoading = false
                                    )
                                    // Refresh the banners list
                                    getAllBanners()
                                }
                                is ResultState.Error -> {
                                    _deleteBannerState.value = DeleteBannerScreenState(
                                        Error = "Firebase deletion failed: ${firebaseResult.message}",
                                        isLoading = false
                                    )
                                }
                                else -> {}
                            }
                        }
                    }
                    is ResultState.Error -> {
                        _deleteBannerState.value = DeleteBannerScreenState(
                            Error = "Supabase deletion failed: ${supabaseResult.message}",
                            isLoading = false
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    fun resetDeleteBannerState() {
        _deleteBannerState.value = DeleteBannerScreenState()
    }

    fun resetBannerDetailsState() {
        _bannerDetailsState.value = BannerDetailsScreenState()
    }

    fun onSearchQueryChanged(query: String) {
        _SearchQuery.value = query
    }

    fun searchQuery(){
        viewModelScope.launch(Dispatchers.IO) {
            _SearchQuery.debounce(500L).distinctUntilChanged().collect {
                SearchProduct(it)
            }
        }
    }

    fun SearchProduct(SearchQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getSearchProductUseCase.searchProductUseCase(SearchQuery).collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _searchProductState.value = SearchProductScreen(isLoading = true)
                    }

                    is ResultState.Success<List<ProductModel>> -> {
                        _searchProductState.value = SearchProductScreen(
                            isSuccess = it.data,
                            isLoading = false
                        )

                    }

                    is ResultState.Error -> {
                        _searchProductState.value = SearchProductScreen(
                            Error = it.message.toString(),
                            isLoading = false
                        )
                    }

                }
            }
        }
    }
    // Add this function to your MyViewModel
    fun clearSearchResults() {
        _searchProductState.value = SearchProductScreen()
        _SearchQuery.value = ""
    }

    fun getAllProductByCategory(CategoryName: String){
        viewModelScope.launch(Dispatchers.IO) {
            getProductsByCategory.getProductsByCategory(CategoryName).collectLatest { result->
                when(result){
                    is ResultState.Loading -> {
                        _getProductsByCategoryState.value = GetProductsByCategoryScreenState(isLoading = true)
                    }
                    is ResultState.Success<List<ProductModel>> -> {
                        _getProductsByCategoryState.value = GetProductsByCategoryScreenState(
                            isSuccess = result.data,
                            isLoading = false
                        )
                    }
                    is ResultState.Error ->{
                        _getProductsByCategoryState.value = GetProductsByCategoryScreenState(
                            Error = result.message.toString(),
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    fun updateProductDate(product: ProductModel){
        viewModelScope.launch(Dispatchers.IO) {
            updateProductDataAdminUseCase.updateProductDataAdminUseCase(product).collectLatest {
                when(it){
                    is ResultState.Loading ->{
                        _updateProductDataState.value = UpdateProductDataScreenState(isLoading = true)
                    }
                    is ResultState.Success<Boolean> -> {
                        _updateProductDataState.value = UpdateProductDataScreenState(
                            isSuccess = it.data,
                            isLoading = false
                        )
                    }

                    is ResultState.Error ->{
                        _updateProductDataState.value = UpdateProductDataScreenState(
                            Error = it.message.toString(),
                            isLoading = false
                        )
                    }

                }
            }
        }

        fun resetUpdateProductDataState(){
            _updateProductDataState.value = UpdateProductDataScreenState()
        }

    }

    fun getSpecificProduct( productID: String){
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("MyViewModel", "getSpecificProduct called with ID: $productID")

            // Check if the productID is valid
            if (productID.isBlank()) {
                Log.e("MyViewModel", "Product ID is blank!")
                _getSpecificProductState.value = GetSpecificProductScreenState(
                    Error = "Product ID is empty",
                    isLoading = false
                )
                return@launch
            }
            getSpecificProductAdminUseCase.getSpecificProduct(productID).collectLatest {
                when(it){
                    is ResultState.Loading ->{
                        Log.d("MyViewModel", "Loading product...")
                        _getSpecificProductState.value = GetSpecificProductScreenState(isLoading = true)
                    }
                    is ResultState.Success<ProductModel> -> {
                        if (it.data != null) {
                            Log.d("MyViewModel", "Product loaded successfully: ${it.data.name} (ID: ${it.data.productId})")
                            _getSpecificProductState.value = GetSpecificProductScreenState(
                                isSuccess = it.data,
                                isLoading = false
                            )
                        } else {
                            Log.e("MyViewModel", "Product data is null!")
                            _getSpecificProductState.value = GetSpecificProductScreenState(
                                Error = "Product data is null",
                                isLoading = false
                            )
                        }
                    }
                    is ResultState.Error -> {
                        Log.e("MyViewModelError", "Error loading product: ${it.message}")

                        _getSpecificProductState.value = GetSpecificProductScreenState(
                            Error = it.message.toString(),
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun removeSpecificProduct( productID: String){
        viewModelScope.launch(Dispatchers.IO) {
            removeSpecificProductAdminUseCaes.removeSpecificProduct(productID).collectLatest { result->
               when(result){
                   is ResultState.Loading -> {
                            _removeSpecificProductState.value = RemoveSpecificProductScreenState(isLoading = false)
                   }
                   is ResultState.Success<Boolean> -> {
                       _removeSpecificProductState.value = RemoveSpecificProductScreenState(
                           isSuccess = result.data,
                           isLoading = false
                       )

                   }
                   is ResultState.Error -> {
                       _removeSpecificProductState.value = RemoveSpecificProductScreenState(
                           Error = result.message.toString(),
                           isLoading = false
                       )
                   }


               }
           }
        }
    }

    fun resetRemoveState(){
        _removeSpecificProductState.value = RemoveSpecificProductScreenState()
    }

    fun getAllAdminBrandProducts(){
        viewModelScope.launch(Dispatchers.IO) {
            getAllAdminProductsUseCase.allAdminProductsUseCase().collectLatest { result ->
                when(result){
                is ResultState.Loading -> {
                    _allAdminProductsState.value = AllAdminBrandProductsScreenState(isLoading = true)
                }
                is ResultState.Success<List<ProductModel>> ->{
                    _allAdminProductsState.value = AllAdminBrandProductsScreenState(
                        isSuccess = result.data,
                        isLoading = false
                    )
                }
                    is ResultState.Error ->{
                        _allAdminProductsState.value = AllAdminBrandProductsScreenState(
                            Error = result.message.toString(),
                            isLoading = false
                        )
                    }

                }
            }
        }
    }

    fun addBannerModel(BannerModel: BannerDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addBannerUserUserCase.addBannerUserUserCase(bannerModel = BannerModel).collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _addBannerModelState.value = AddBannerScreenState(isLoading = true)
                    }

                    is ResultState.Error -> {
                        _addBannerModelState.value =
                            AddBannerScreenState(Error = it.message.toString(), isLoading = false)
                    }

                    is ResultState.Success -> {
                        _addBannerModelState.value =
                            AddBannerScreenState(isSuccess = it.data, isLoading = false)
                    }
                }
            }

        }
    }

    fun addBannerModelPhoto(
        byteArray: ByteArray,
        onSuccess: (String) -> Unit = {},
        onError: (String) -> Unit = {}){
        viewModelScope.launch(Dispatchers.IO){
            addBannerModelPhotoUserUseCase.addBannerModelPhotoUserUseCase(byteArray).collectLatest { result ->
                when(result){
                    is ResultState.Loading -> {
                        _addBannerModelPhotoState.value = AddBannerScreenPhotoState(isLoading = true)
                    }
                    is ResultState.Success ->{
                        _addBannerModelPhotoState.value = AddBannerScreenPhotoState(
                            isSuccess = result.data,
                            isLoading = false
                        )
                        result.data?.let {url ->
                            onSuccess(url)
                        } ?: run {
                            onError("Upload succeeded but no URL returned")
                        }
                    }
                    is ResultState.Error -> {
                        _addBannerModelPhotoState.value = AddBannerScreenPhotoState(
                            Error = result.message.toString(),
                            isLoading = false
                        )
                        onError(result.message ?: "Unknown upload error")
                    }
                }
            }

        }
    }

    fun addProductPhoto(
                        byteArray: ByteArray,
                        onSuccess: (String) -> Unit = {},
                        onError: (String) -> Unit = {}
    ){
        viewModelScope.launch(Dispatchers.IO){
            addProductPhotoUseCase.addProductPhotoUseCase(byteArray).collect {result ->
                when(result){
                    is ResultState.Loading -> {
                        _addProductPhoto.value = AddProductPhotoState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _addProductPhoto.value = AddProductPhotoState(
                            isSuccess = result.data,
                            isLoading = false)
                        result.data?.let {url ->
                            onSuccess(url)
                        }  ?: run {
                            onError("Upload succeeded but no URL returned")
                        }
                    }
                    is ResultState.Error -> {
                        _addProductPhoto.value = AddProductPhotoState(
                            Error = result.message,
                            isLoading = false)
                        onError(result.message ?: "Unknown upload error")
                    }
                }

            }
        }
    }

    fun addCategory(category: CategoryModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addCategoryUseCase.addCategoryUseCase(category).collect { it ->
                when (it) {
                    is ResultState.Loading -> {
                        _addCategory.value = AddCategoryState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _addCategory.value =
                            AddCategoryState(isSuccess = it.data, isLoading = false)
                    }

                    is ResultState.Error -> {
                        Log.e("ERROR", _addCategory.value.Error ?: "Error")
                        _addCategory.value = AddCategoryState(Error = it.message, isLoading = false)
                    }
                }
            }
        }
    }

    fun getAllCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            getCategoryUseCase.getAllCategoryUseCase().collect {
                when (it) {
                    is ResultState.Loading -> {
                        _getCategory.value = GetCategoryState(isLoading = true)
                        Log.d("ViewModelCatergoryList", "Loading...}")
                    }

                    is ResultState.Success -> {
                        _getCategory.value =
                            GetCategoryState(isSuccess = it.data, isLoading = false)
                        Log.d("ViewModelCatergoryList", "${it.data}")

                    }

                    is ResultState.Error -> {
                        _getCategory.value =
                            GetCategoryState(Error = it.message.toString(), isLoading = false)
                        Log.d("ViewModelCatergoryListError", "${it.message.toString()}")
                    }
                }
            }
        }
    }

    fun addProduct(product: ProductModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addProductUseCase.addProductUseCase(product).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _addProduct.value = AddProductState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _addProduct.value = AddProductState(isSuccess = it.data, isLoading = false)
                    }

                    is ResultState.Error -> {
                        _addProduct.value =
                            AddProductState(Error = it.message.toString(), isLoading = false)
                    }
                }

            }
        }
    }

    // Expose public method to reset state
    fun resetAddProductState() {
        _addProduct.value = AddProductState()
    }

    fun resetAddCategoryState() {
        _addCategory.value = AddCategoryState()
    }

    fun resetAddBannerModelState(){
        _addBannerModelState.value = AddBannerScreenState()
    }

    fun createAdmin(AdminData: AdminDataModel){
        viewModelScope.launch(Dispatchers.IO) {
            createUserUseCase.registerAdmin(AdminData).collectLatest { result->
                when(result){
                    is ResultState.Loading -> {
                        _authScreenState.value = AuthScreenState.Loading
                    }
                    is ResultState.Success -> {
                        if (result.data == true) {
                            _authScreenState.value = AuthScreenState.RegistrationSuccess(true)

                        } else {
                            _authScreenState.value = AuthScreenState.Error("Registration returned false")
                        }
                    }
                    is ResultState.Error -> {
                        _authScreenState.value = AuthScreenState.Error(result.message ?: "Registration failed")
                    }
                }

            }
        }
    }

    fun logInAdmin(AdminData: AdminDataModel){
        viewModelScope.launch(Dispatchers.IO) {
            createUserUseCase.signUpAdmin(AdminData).collectLatest {
                when(it){
                    is ResultState.Loading -> {
                        _authScreenState.value = AuthScreenState.Loading
                    }
                    is ResultState.Success<Boolean> ->{
                        if (it.data == true) {
                            _authScreenState.value = AuthScreenState.LoginSuccess(true)

                        } else {
                            _authScreenState.value = AuthScreenState.Error("LogIn returned false")
                        }
                    }
                    is ResultState.Error -> {
                        _authScreenState.value = AuthScreenState.Error(it.message.toString())
                    }
                }
            }
        }
    }

    fun AdminSignOut(){
        viewModelScope.launch(Dispatchers.IO) {
            _authScreenState.value = AuthScreenState.Loading
            createUserUseCase.AdminSignOut().collectLatest {
                _authScreenState.value = when (it) {
                    is ResultState.Success -> AuthScreenState.SignedOut
                    is ResultState.Error -> AuthScreenState.Error(it.message.toString())
                    else -> AuthScreenState.Idle
                }


            }
        }
    }

    fun resetAdminState() {
        _authScreenState.value = AuthScreenState.Idle
    }

    fun updateBanner(banner: BannerDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            updateBannerUseCase.updateBannerUseCase(banner).collectLatest { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _updateBannerState.value = UpdateBannerScreenState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _updateBannerState.value = UpdateBannerScreenState(
                            isSuccess = result.data,
                            isLoading = false
                        )
                        // Refresh banners list after update
                        getAllBanners()
                    }
                    is ResultState.Error -> {
                        _updateBannerState.value = UpdateBannerScreenState(
                            Error = result.message.toString(),
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun resetUpdateBannerState() {
        _updateBannerState.value = UpdateBannerScreenState()
    }


}

data class GetProductsByCategoryScreenState(
    val isLoading: Boolean = false,
    val isSuccess: List<ProductModel>? = emptyList<ProductModel>(),
    val Error: String? = null
)

data class UpdateProductDataScreenState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean? =null,
    val Error: String? = null
)

data class AddCategoryState(
    var isLoading: Boolean = false,
    var Error: String? = null,
    var isSuccess: String? = null
)

data class GetSpecificProductScreenState(
    var isLoading: Boolean = false,
    var Error: String? = null,
    var isSuccess: ProductModel? = null
)

data class GetCategoryState(
    var isLoading: Boolean = false,
    var isSuccess: List<CategoryModel>? = null,
    var Error: String? = null
)

data class AddProductState(
    var isLoading: Boolean = false,
    var isSuccess: String? = null,
    var Error: String? = null
)

data class AddProductPhotoState(
    var isLoading: Boolean = false,
    var isSuccess: String? = null,
    var Error: String? = null
)

data class AddBannerScreenState(
    val isLoading: Boolean = false,
    var isSuccess: String? = null,
    var Error: String? = null
)

data class UpdateBannerScreenState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean? = null,
    val Error: String? = null
)

data class AddBannerScreenPhotoState(
    val isLoading: Boolean = false,
    var isSuccess: String? = null,
    var Error: String? = null
)

data class AllAdminBrandProductsScreenState(
    val isLoading: Boolean = false,
    val isSuccess: List<ProductModel> = emptyList(),
    val Error: String? = null
)

data class RemoveSpecificProductScreenState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean? = null,
    val Error: String? = null
)

sealed class AuthScreenState {
    // Initial state
    object Idle : AuthScreenState()

    // Loading state
    object Loading : AuthScreenState()

    // Success states (with boolean result)
    data class RegistrationSuccess(val isSuccess: Boolean) : AuthScreenState()
    data class LoginSuccess(val isSuccess: Boolean) : AuthScreenState()

    object SignedOut : AuthScreenState()


    // Error state
    data class Error(val message: String) : AuthScreenState()


}

data class SearchProductScreen(
    val isLoading: Boolean = false,
    val isSuccess: List<ProductModel>? = emptyList<ProductModel>(),
    val Error: String? = null
)

// State classes
data class DeleteBannerScreenState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean? = null,
    val Error: String? = null
)

data class BannerDetailsScreenState(
    val isLoading: Boolean = false,
    val isSuccess: BannerDataModel? = null,
    val Error: String? = null
)

data class GetAllBannersScreenState(
    val isLoading: Boolean = false,
    val isSuccess: List<BannerDataModel>? = emptyList<BannerDataModel>(),
    val Error: String? = null
)

data class GetAllOrderScreenState(
    val isLoading: Boolean = false,
    val isSuccess: List<OrderDataModel>?= emptyList<OrderDataModel>(),
    val Error: String? = null
)

data class UpdateOrderState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val Error: String? = null
)

data class GetCurrentAdminScreenState(
    val isLoading: Boolean = false,
    val isSuccess: AdminDataModel? = null,
    val Error: String? = null
)

data class UpdateAdminScreenState(
    val isLoading: Boolean = false,
    val isSuccess: String? = null,
    val Error: String? = null
)