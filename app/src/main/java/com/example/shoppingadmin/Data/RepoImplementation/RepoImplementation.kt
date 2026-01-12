package com.example.shoppingadmin.Data.RepoImplementation



import android.util.Log
import com.example.shoppingadmin.Common.ADMIN_PATH
import com.example.shoppingadmin.Common.BANNERMODEL_PATH
import com.example.shoppingadmin.Common.CATEGORY_PATH
import com.example.shoppingadmin.Common.ORDERS_PATH
import com.example.shoppingadmin.Common.PRODUCT_PATH
import com.example.shoppingadmin.Common.ResultState
import com.example.shoppingadmin.Common.USER_ORDERS_SUBCOLLECTION
import com.example.shoppingadmin.Common.USER_PATH
import com.example.shoppingadmin.Data.PushNotification.PushNotification
import com.example.shoppingadmin.Domain.Models.AdminDataModel
import com.example.shoppingadmin.Domain.Models.BannerDataModel
import com.example.shoppingadmin.Domain.Models.CategoryModel
import com.example.shoppingadmin.Domain.Models.OrderDataModel
import com.example.shoppingadmin.Domain.Models.ProductModel
import com.example.shoppingadmin.Domain.repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import io.ktor.http.ContentType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class RepoImplementation(
    private val firestore: FirebaseFirestore,
    client: SupabaseClient,
    private val pushNotification: PushNotification,
    private val firebaseAuth: FirebaseAuth

) : Repo {
    val storage = client.storage

    override  fun addCategpry(category: CategoryModel): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            try {
                firestore.collection(CATEGORY_PATH).add(category).addOnSuccessListener {
                    trySend(ResultState.Success("Category Added Successfully"))
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                }

            } catch (e: Exception) {
                trySend(ResultState.Error(e.message.toString()))
            }
            awaitClose {
                close()
            }
        }

    override  fun getCategories(): Flow<ResultState<List<CategoryModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        try {
            firestore.collection(CATEGORY_PATH).get().addOnSuccessListener {
                val categories = it.documents.mapNotNull { docs ->
                    docs.toObject(CategoryModel::class.java)
                }
                Log.d("RepoCategoryList", "${categories}")
                trySend(ResultState.Success(categories))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        } catch (e: Exception) {
            trySend(ResultState.Error(e.message.toString()))
        }
        awaitClose {
            close()
        }

    }

    override  fun addProduct(product: ProductModel): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            try {
                firestore.collection(PRODUCT_PATH).add(product).addOnSuccessListener {
                    trySend(ResultState.Success("Product Added Successfully"))
                    pushNotification.sendNotificationToAllUser(
                        productName = product.name,
                        imageUrl = product.image.toString()
                    )
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                }
            } catch (e: Exception) {
                trySend(ResultState.Error(e.message.toString()))
            }
            awaitClose {
                close()
            }
        }

    override  fun addBannerModel(bannerModel: BannerDataModel): Flow<ResultState<String>> = callbackFlow {
       trySend(ResultState.Loading)
        try {
            firestore.collection(BANNERMODEL_PATH).add(bannerModel).addOnSuccessListener {
                trySend(ResultState.Success("BannerModel Added Successfully"))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        }catch (e: Exception){
            trySend(ResultState.Error(e.message.toString()))
        }
        awaitClose{
            close()
        }
    }

    override  fun addProductPhoto(byteArray: ByteArray): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        try {
            // 1. First ensure bucket exists
            try {
                storage.createBucket("shopping_app") {
                    public = true
                    fileSizeLimit = 10.megabytes
                }
            } catch (e: Exception) {
                // Bucket may already exist - that's fine
                if (!e.message.orEmpty().contains("already exists")) {
                    trySend(ResultState.Error("Bucket creation failed: ${e.message}"))
                    awaitClose { close() }
                    return@callbackFlow
                }
            }

            // 2. Get reference to the bucket
            val bucket = storage["shopping_app"]
            val fileName = "product_${System.currentTimeMillis()}.jpg"

            // 3. Upload the file

            bucket.upload(
                path = fileName,
                data = byteArray,
                options = {
                    contentType =  ContentType.Image.JPEG
                }
            )

            // 4. Get and return public URL
            val publicUrl = bucket.publicUrl(fileName)
            trySend(ResultState.Success(publicUrl))


        } catch (e: Exception) {
            trySend(ResultState.Error(e.message ?: "Unknown error uploading photo"))
        }

        awaitClose {
            close()
        }
    }

    override  fun addBannerModelPhoto(byteArray: ByteArray): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            try {
                // 1. First ensure bucket exists
                try {
                    storage.createBucket("Banners"){
                        public = true
                        fileSizeLimit = 10.megabytes
                    }

                }catch (e: Exception){
                    if (! e.message.orEmpty().contains("already exists")){
                        trySend(ResultState.Error("Bucket creation failed: ${e.message}"))
                        awaitClose { close() }
                        return@callbackFlow
                    }
                }

                //2. Get refrence of the bucket
                val bucket = storage["Banners"]
                val fileName = "Banner_${System.currentTimeMillis()}.jpg"

                //3. Upload the file

                bucket.upload(
                    path = fileName,
                    data = byteArray,
                    options = {
                        contentType = ContentType.Image.JPEG
                    }
                )
                //after upload get publicUrl
                val publicUrl = bucket.publicUrl(fileName)
                trySend(ResultState.Success(publicUrl))

            }catch (e: Exception){
                trySend(ResultState.Error(e.message ?: "Unknown error uploading photo"))
            }
            awaitClose {
                close()
            }

        }

    override fun registerAdminWithEmailAndPassword(AdminData: AdminDataModel): Flow<ResultState<Boolean>> = callbackFlow {
        trySend(ResultState.Loading)
        try {
            firebaseAuth.createUserWithEmailAndPassword(AdminData.email, AdminData.password).addOnSuccessListener {
                firestore.collection(ADMIN_PATH).document(
                    it.user?.uid.toString()
                ).set(AdminData).addOnSuccessListener {
                    trySend(ResultState.Success(true))
                    close()
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                }
            }

        } catch (e: Exception){
            trySend(ResultState.Error(e.message.toString()))
        }
        awaitClose{
            close()
        }
    }

    override fun signInAdminWithEmailAndPassword(UserData: AdminDataModel): Flow<ResultState<Boolean>> = callbackFlow {
            trySend(ResultState.Loading)

        try {
                firebaseAuth.signInWithEmailAndPassword(UserData.email, UserData.password).addOnSuccessListener {
                    val uid  = it.user?.uid ?: " "
                 trySend(ResultState.Success(true))
                    close()
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                    close()
                }
        } catch (e: Exception){
            trySend(ResultState.Error(e.message.toString()))
        }
        awaitClose{
            close()
        }
    }

    override fun getCurrentAdmin(): Flow<ResultState<AdminDataModel>> = callbackFlow {
        trySend(ResultState.Loading)

        try {
            val currentUser = firebaseAuth.currentUser

            if (currentUser == null) {
                trySend(ResultState.Error("Admin not logged in"))
                close()
                awaitClose { close() }
                return@callbackFlow
            }

            val adminId = currentUser.uid

            firestore.collection(ADMIN_PATH)
                .document(adminId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val adminData = document.toObject(AdminDataModel::class.java)
                        if (adminData != null) {
                            trySend(ResultState.Success(adminData))
                        } else {
                            trySend(ResultState.Error("Invalid admin data"))
                        }
                    } else {
                        trySend(ResultState.Error("Admin not found"))
                    }
                    close()
                }
                .addOnFailureListener { e ->
                    trySend(ResultState.Error(e.message.toString()))
                    close()
                }


        }
        catch (e: Exception){
            trySend(ResultState.Error(e.message.toString()))
        }
        awaitClose {
            close()
        }

    }

    override fun UpdateAdimnData(AdminData: AdminDataModel): Flow<ResultState<String>> =callbackFlow {
        trySend(ResultState.Loading)
        try {
            firestore.collection(ADMIN_PATH).document(firebaseAuth.currentUser!!.uid)
                .set(AdminData).addOnSuccessListener {
                    trySend(ResultState.Success("User Data Updated Successfully"))
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString() ?: "User Data Not Updated"))
                }
        }
        catch (e: Exception){
            trySend(ResultState.Error(e.message.toString()))
        }
    }

    override fun adminSignOut(): Flow<ResultState<Boolean>> = callbackFlow{
        trySend(ResultState.Loading)
        try {
            firebaseAuth.signOut()
            trySend(ResultState.Success(true))
        }catch (e: Exception){
            trySend(ResultState.Error(e.message.toString()))
        }
            awaitClose{
                close()
            }
    }

    override fun adminAllProducts(): Flow<ResultState<List<ProductModel>>> = callbackFlow{
        trySend(ResultState.Loading)
        try{
            firestore.collection(PRODUCT_PATH).get().addOnSuccessListener {collectionSnapshot->
                val products = collectionSnapshot.documents.mapNotNull {documentSnapshot ->
                    documentSnapshot.toObject(ProductModel::class.java)?.apply {
                         productId = documentSnapshot.id

                    }


                }
                trySend(ResultState.Success(products))
                close()
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
                close()
            }

        } catch (e: Exception){
            trySend(ResultState.Error(e.message.toString()))
        }
        awaitClose{
            close()
        }
    }

    override fun removeProducts( productID: String): Flow<ResultState<Boolean>> = callbackFlow{
        trySend(ResultState.Loading)
        try {

            firestore.collection(PRODUCT_PATH).document(productID).delete().addOnSuccessListener {
                trySend(ResultState.Success(true))
                close()
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
                close()
            }

        } catch (e: Exception){
            trySend(ResultState.Error(e.message.toString()))

        }
        awaitClose{
            close()
        }
    }

    override fun getspecificProduct( productID: String): Flow<ResultState<ProductModel>> = callbackFlow{
        trySend(ResultState.Loading)
        try {

            firestore.collection(PRODUCT_PATH).document(productID).get().addOnSuccessListener {documentSnapshot->
                val product = documentSnapshot.toObject(ProductModel::class.java)
                product?.let {
                    it.productId =documentSnapshot.id
                    Log.d("repoProduct", "${productID}")
                    Log.d("repoProduct", "${product}")
                    trySend(ResultState.Success(product))
                    close()
                } ?:trySend(ResultState.Error("Product not found"))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
                close()
            }
        } catch (e: Exception){
            trySend(ResultState.Error(e.message.toString()))
        }
        awaitClose{
            close()
        }
    }

    override fun updateProductData(product: ProductModel): Flow<ResultState<Boolean>> = callbackFlow {
        trySend(ResultState.Loading)
        try {
            val productID =product.productId
            firestore.collection(PRODUCT_PATH).document(productID).set(product).addOnSuccessListener {
                trySend(ResultState.Success(true))
                close()
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
                close()
            }

        } catch (e: Exception){
            trySend(ResultState.Error(e.message.toString()))
        }
        awaitClose{
            close()
        }
    }

    override fun getProductsByCategory(CategoryName: String): Flow<ResultState<List<ProductModel>>> = callbackFlow{
       trySend(ResultState.Loading)
        try {
            firestore.collection(PRODUCT_PATH).whereEqualTo("category", CategoryName).get()
                .addOnSuccessListener { collectionSnapshot->
                    val products = collectionSnapshot.documents.mapNotNull {documentSnapshot->
                        documentSnapshot.toObject(ProductModel::class.java)?.apply {
                            productId = documentSnapshot.id
                        }
                    }
                    trySend(ResultState.Success(products))
                    close()
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                    close()
                }

        }catch (e: Exception){
            trySend(ResultState.Error(e.message.toString()))
        }
        awaitClose{
            close()
        }
    }

    override fun searchProduct(SearchQuery: String): Flow<ResultState<List<ProductModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        try {
            firestore.collection(PRODUCT_PATH).orderBy("name").startAt(SearchQuery)
                .endAt(SearchQuery + "\uf8ff")
                .get().addOnSuccessListener {

                    val productList = it.documents.mapNotNull {document->
                        val product = document.toObject(ProductModel::class.java)
                        product?.copy(productId = document.id)
                    }
                    trySend(ResultState.Success(productList))
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                }

        }catch (e: Exception){
            trySend(ResultState.Error(e.message.toString()))
        }
        awaitClose{
            close()
        }
    }

    override fun deleteBannerImage(imageUrl: String): Flow<ResultState<Boolean>> = callbackFlow {
        trySend(ResultState.Loading)
        try {
            val fileName =  imageUrl.substringAfterLast("/")
            if (fileName.isBlank()) {
                trySend(ResultState.Error("Invalid image URL"))
                return@callbackFlow
            }

            Log.d("SupabaseDelete", "Attempting to delete file: $fileName")

            // Get the bucket and delete the file
            val bucket = storage["Banners"]

            // Check if file exists before deleting
            val files = bucket.list()
            val fileExists = files.any { it.name == fileName }

            if (fileExists) {
                bucket.delete(fileName)
                Log.d("SupabaseDelete", "Successfully deleted file: $fileName")
                trySend(ResultState.Success(true))
            } else {
                Log.d("SupabaseDelete", "File not found, considering deletion successful")
                trySend(ResultState.Success(false)) // File doesn't exist, consider it deleted
            }
        } catch (e: Exception){
            Log.e("SupabaseDelete", "Error deleting image: ${e.message}")
            trySend(ResultState.Error(e.message ?: "Failed to delete image from Supabase"))
        }
        awaitClose{
            close()
        }

    }

    override fun deleteBanner(bannerId: String): Flow<ResultState<Boolean>> =callbackFlow{
            trySend(ResultState.Loading)
        try {
            Log.d("FirebaseDelete", "Attempting to delete banner: $bannerId")

            firestore.collection(BANNERMODEL_PATH).document(bannerId).delete()
                .addOnSuccessListener {
                    Log.d("FirebaseDelete", "Successfully deleted banner: $bannerId")
                    trySend(ResultState.Success(true))
                    close()
                }
                .addOnFailureListener { exception ->
                    Log.e("FirebaseDelete", "Error deleting banner: ${exception.message}")
                    trySend(ResultState.Error(exception.message.toString()))
                }

        }catch (e: Exception){
            trySend(ResultState.Error(e.message.toString()))
        }
        awaitClose{close()}
    }

    override fun getAllBanners(): Flow<ResultState<List<BannerDataModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        try{
            firestore.collection(BANNERMODEL_PATH).get().addOnSuccessListener { collectionSnapshot->
                val banners = collectionSnapshot.documents.mapNotNull {documentSnapshot->
                    documentSnapshot.toObject(BannerDataModel::class.java)?.apply {
                        bannerId = documentSnapshot.id
                    }
                }
                trySend(ResultState.Success(banners))

            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        }catch (e: Exception){
            trySend(ResultState.Error(e.message.toString()))
        }
        awaitClose{
            close()
        }

    }

    override fun getBannerById(bannerId: String): Flow<ResultState<BannerDataModel>> =callbackFlow{
     trySend(ResultState.Loading)
        try {
            firestore.collection(BANNERMODEL_PATH).document(bannerId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val banner = document.toObject(BannerDataModel::class.java)
                        if (banner != null) {
                            // Make sure your BannerDataModel has an id field or use the document ID
                            val bannerWithId = banner.copy(bannerId = document.id) // or bannerId = document.id
                            trySend(ResultState.Success(bannerWithId))
                        } else {
                            trySend(ResultState.Error("Banner data is null or invalid"))
                        }

                    } else {
                        trySend(ResultState.Error("Banner not found"))
                    }
                }
                .addOnFailureListener { exception ->
                    trySend(ResultState.Error(exception.message.toString()))
                }
        } catch (e: Exception){
            trySend(ResultState.Error(e.message.toString()))
        }
        awaitClose{
            close()
        }
    }

    override fun updateBanner(banner: BannerDataModel): Flow<ResultState<Boolean>> = callbackFlow {
        trySend(ResultState.Loading)
        try {
            val bannerId = banner.bannerId ?: ""
            if (bannerId.isBlank()) {
                trySend(ResultState.Error("Invalid banner ID"))
                return@callbackFlow
            }

            val bannerData = hashMapOf(
                "name" to banner.name,
                "imageUrl" to banner.imageUrl,
                "date" to banner.date
            )

            firestore.collection(BANNERMODEL_PATH).document(bannerId)
                .update(bannerData as Map<String, Any>)
                .addOnSuccessListener {
                    trySend(ResultState.Success(true))
                }
                .addOnFailureListener { exception ->
                    trySend(ResultState.Error(exception.message.toString()))
                }
        } catch (e: Exception) {
            trySend(ResultState.Error(e.message.toString()))
        }
        awaitClose { close() }
    }

    override fun getAllOrders(): Flow<ResultState<List<OrderDataModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        // Query the main orders collection (not user-specific)
        val listenerRegistration = firestore.collection(ORDERS_PATH)
            .orderBy("orderDate", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(ResultState.Error("Failed to listen for orders: ${error.message}"))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val ordersList = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(OrderDataModel::class.java)
                    }
                    // Admin can see all orders including cancelled ones
                    trySend(ResultState.Success(ordersList))
                }
            }

        awaitClose { listenerRegistration.remove() }
    }

//    ✅ Update order status (Admin)
override fun updateOrderStatus(order: OrderDataModel, status: String): Flow<ResultState<Boolean>> = callbackFlow {
    trySend(ResultState.Loading)

    val batch = firestore.batch()

    try {
        Log.d("FIREBASE_UPDATE", "🔄 Updating order: ${order.orderId}")
        Log.d("FIREBASE_UPDATE", "📁 User ID: '${order.userId}'")
        Log.d("FIREBASE_UPDATE", "📏 User ID length: ${order.userId.length}")

        // Update main orders collection
        val mainOrderRef = firestore.collection(ORDERS_PATH).document(order.orderId)
        Log.d("FIREBASE_UPDATE", "📦 Main path: ${mainOrderRef.path}")
        batch.update(mainOrderRef, "orderStatus", status)

        // User subcollection
        val userOrderRef = firestore.collection(USER_PATH)
            .document(order.userId)
            .collection(USER_ORDERS_SUBCOLLECTION)
            .document(order.orderId)
        Log.d("FIREBASE_UPDATE", "👤 User path: ${userOrderRef.path}")

        // ✅ FIX: Set the COMPLETE order with updated status
        val updatedOrder = order.copy(orderStatus = status)
        batch.set(userOrderRef, updatedOrder)  // This REPLACES the entire document

        Log.d("FIREBASE_UPDATE", "🚀 Committing batch...")

        // Execute both updates atomically
        batch.commit()
            .addOnSuccessListener {
                Log.d("FIREBASE_UPDATE", "✅ Batch update SUCCESSFUL")
                trySend(ResultState.Success(true))
                close()
            }
            .addOnFailureListener { e ->
                Log.d("FIREBASE_UPDATE", "❌ Batch update FAILED: ${e.message}")
                Log.d("FIREBASE_UPDATE", "🔍 Error details: $e")
                trySend(ResultState.Error("Update failed: ${e.message}"))
                close()
            }
    } catch (e: Exception) {
        Log.d("FIREBASE_UPDATE", "💥 EXCEPTION: ${e.message}")
        Log.d("FIREBASE_UPDATE", "🔍 Stacktrace: ${e.stackTraceToString()}")
        trySend(ResultState.Error("Error: ${e.message}"))
        close()
    }

    awaitClose { close() }
}
}