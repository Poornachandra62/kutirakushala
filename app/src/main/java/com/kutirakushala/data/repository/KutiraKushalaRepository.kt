package com.kutirakushala.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.kutirakushala.data.db.BusinessDao
import com.kutirakushala.data.db.ProductDao
import com.kutirakushala.data.model.*

class KutiraKushalaRepository(
    private val businessDao: BusinessDao,
    private val productDao: ProductDao
) {
    // ── Businesses ────────────────────────────────────────────────
    val allBusinesses: LiveData<List<Business>> = businessDao.getAllBusinesses()

    fun getBusinessWithProducts(id: Int) = businessDao.getBusinessWithProducts(id)

    fun searchBusinesses(query: String) = businessDao.search(query)

    fun filterBusinesses(category: String?, location: String?): LiveData<List<Business>> {
        return when {
            !category.isNullOrBlank() && !location.isNullOrBlank() ->
                businessDao.getByCategoryAndLocation(category, location)
            !category.isNullOrBlank() ->
                businessDao.getByCategory(category)
            !location.isNullOrBlank() ->
                businessDao.getByLocation(location)
            else ->
                businessDao.getAllBusinesses()
        }
    }

    suspend fun insertBusiness(business: Business): Long = businessDao.insertBusiness(business)

    suspend fun updateBusiness(business: Business) = businessDao.updateBusiness(business)

    suspend fun deleteBusiness(business: Business) = businessDao.deleteBusiness(business)

    suspend fun updateCapacity(id: Int, units: Int, status: CapacityStatus) =
        businessDao.updateCapacity(id, units, status)

    suspend fun seedSampleDataIfEmpty() {
        if (businessDao.getCount() > 0) return

        val b1 = businessDao.insertBusiness(
            Business(
                ownerName = "Savitha Devi",
                businessName = "Savitha Baskets",
                skillArea = "Basket Weaving",
                location = "Tumkur, Karnataka",
                description = "Hand-woven bamboo and cane baskets. 15 years experience. Bulk orders welcome.",
                phoneNumber = "9876543210",
                category = ProductCategory.CRAFT.name,
                capacityUnits = 200,
                capacityStatus = CapacityStatus.AVAILABLE
            )
        )
        productDao.insertProduct(Product(businessId = b1.toInt(), name = "Bamboo Basket (Small)", unit = "per piece", wholesalePrice = 45.0, minimumOrder = 100, description = "Hand-woven small bamboo basket, natural finish"))
        productDao.insertProduct(Product(businessId = b1.toInt(), name = "Bamboo Basket (Large)", unit = "per piece", wholesalePrice = 90.0, minimumOrder = 50, description = "Large bamboo basket with handle"))
        productDao.insertProduct(Product(businessId = b1.toInt(), name = "Cane Tray", unit = "per piece", wholesalePrice = 120.0, minimumOrder = 30))

        val b2 = businessDao.insertBusiness(
            Business(
                ownerName = "Meenakshi Bai",
                businessName = "Meenakshi Agarbatti Works",
                skillArea = "Agarbatti Rolling",
                location = "Ramanagara, Karnataka",
                description = "Fresh-scented agarbatti sticks rolled daily. Sandalwood, jasmine, rose varieties.",
                phoneNumber = "9845001234",
                category = ProductCategory.HOME_CARE.name,
                capacityUnits = 5000,
                capacityStatus = CapacityStatus.AVAILABLE
            )
        )
        productDao.insertProduct(Product(businessId = b2.toInt(), name = "Sandalwood Agarbatti", unit = "per 100 sticks", wholesalePrice = 35.0, minimumOrder = 500, description = "Pure sandalwood fragrance, 8 inch"))
        productDao.insertProduct(Product(businessId = b2.toInt(), name = "Jasmine Agarbatti", unit = "per 100 sticks", wholesalePrice = 30.0, minimumOrder = 500))
        productDao.insertProduct(Product(businessId = b2.toInt(), name = "Rose Agarbatti", unit = "per 100 sticks", wholesalePrice = 32.0, minimumOrder = 500))

        val b3 = businessDao.insertBusiness(
            Business(
                ownerName = "Lakshmi S",
                businessName = "Lakshmi Papad House",
                skillArea = "Papad Making",
                location = "Mandya, Karnataka",
                description = "Crispy rice and urad dal papads. Sun-dried, no preservatives. Hotels and canteens supplied.",
                phoneNumber = "8880012345",
                category = ProductCategory.FOOD.name,
                capacityUnits = 2000,
                capacityStatus = CapacityStatus.AVAILABLE
            )
        )
        productDao.insertProduct(Product(businessId = b3.toInt(), name = "Urad Dal Papad", unit = "per kg", wholesalePrice = 180.0, minimumOrder = 10, description = "Thin, crispy, sun-dried"))
        productDao.insertProduct(Product(businessId = b3.toInt(), name = "Rice Papad", unit = "per kg", wholesalePrice = 150.0, minimumOrder = 10))
        productDao.insertProduct(Product(businessId = b3.toInt(), name = "Masala Papad", unit = "per kg", wholesalePrice = 200.0, minimumOrder = 5))

        val b4 = businessDao.insertBusiness(
            Business(
                ownerName = "Rekha Patil",
                businessName = "Rekha Bead Jewellery",
                skillArea = "Bead Jewellery",
                location = "Dharwad, Karnataka",
                description = "Handcrafted bead necklaces, earrings, bangles. Custom designs on request.",
                phoneNumber = "9741112233",
                category = ProductCategory.JEWELLERY.name,
                capacityUnits = 150,
                capacityStatus = CapacityStatus.FULL
            )
        )
        productDao.insertProduct(Product(businessId = b4.toInt(), name = "Bead Necklace", unit = "per piece", wholesalePrice = 120.0, minimumOrder = 20))
        productDao.insertProduct(Product(businessId = b4.toInt(), name = "Bead Earrings", unit = "per pair", wholesalePrice = 60.0, minimumOrder = 30))

        val b5 = businessDao.insertBusiness(
            Business(
                ownerName = "Geetha Rao",
                businessName = "Geetha Natural Soaps",
                skillArea = "Handmade Soaps",
                location = "Mysuru, Karnataka",
                description = "Cold-process handmade soaps with coconut oil, turmeric, neem. Zero chemicals.",
                phoneNumber = "9632587410",
                category = ProductCategory.HOME_CARE.name,
                capacityUnits = 300,
                capacityStatus = CapacityStatus.PAUSED
            )
        )
        productDao.insertProduct(Product(businessId = b5.toInt(), name = "Turmeric Soap", unit = "per bar (100g)", wholesalePrice = 55.0, minimumOrder = 50))
        productDao.insertProduct(Product(businessId = b5.toInt(), name = "Neem Soap", unit = "per bar (100g)", wholesalePrice = 50.0, minimumOrder = 50))
    }

    // ── Products ──────────────────────────────────────────────────
    fun getProductsForBusiness(businessId: Int) = productDao.getProductsForBusiness(businessId)

    suspend fun insertProduct(product: Product) = productDao.insertProduct(product)

    suspend fun updateProduct(product: Product) = productDao.updateProduct(product)

    suspend fun deleteProduct(product: Product) = productDao.deleteProduct(product)
}
