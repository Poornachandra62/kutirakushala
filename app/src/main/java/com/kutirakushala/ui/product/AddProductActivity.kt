package com.kutirakushala.ui.product

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.kutirakushala.KutiraKushalaApp
import com.kutirakushala.data.model.Product
import com.kutirakushala.databinding.ActivityAddProductBinding
import com.kutirakushala.utils.ViewModelFactory
import com.kutirakushala.utils.toast
import kotlinx.coroutines.launch

class AddProductActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_BUSINESS_ID = "product_business_id"
        const val EXTRA_PRODUCT_ID = "edit_product_id"
    }

    private lateinit var binding: ActivityAddProductBinding
    private lateinit var viewModel: AddProductViewModel
    private var selectedPhotoUri = ""
    private var businessId = -1
    private var productId = -1
    private var existingProduct: Product? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { selectedPhotoUri = it.toString(); binding.ivProductPhoto.setImageURI(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        businessId = intent.getIntExtra(EXTRA_BUSINESS_ID, -1)
        productId = intent.getIntExtra(EXTRA_PRODUCT_ID, -1)
        val isEdit = productId != -1

        supportActionBar?.title = if (isEdit) "Edit Product" else "Add Product"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = ViewModelFactory((application as KutiraKushalaApp).repository)
        viewModel = ViewModelProvider(this, factory)[AddProductViewModel::class.java]

        if (isEdit) {
            lifecycleScope.launch {
                val db = (application as KutiraKushalaApp).database
                val allProducts = db.productDao().getProductsForBusinessOnce(businessId)
                existingProduct = allProducts.firstOrNull { it.id == productId }
                existingProduct?.let { prefill(it) }
            }
        }

        binding.ivProductPhoto.setOnClickListener { pickImage.launch("image/*") }
        binding.btnPickPhoto.setOnClickListener { pickImage.launch("image/*") }
        binding.btnSaveProduct.setOnClickListener { saveProduct() }

        if (isEdit) {
            binding.btnDeleteProduct.visibility = android.view.View.VISIBLE
            binding.btnDeleteProduct.setOnClickListener {
                existingProduct?.let { p ->
                    viewModel.deleteProduct(p) {
                        runOnUiThread { toast("Product deleted"); finish() }
                    }
                }
            }
        }
    }

    private fun prefill(p: Product) {
        binding.etProductName.setText(p.name)
        binding.etUnit.setText(p.unit)
        binding.etWholesalePrice.setText(p.wholesalePrice.toString())
        binding.etMinOrder.setText(p.minimumOrder.toString())
        binding.etProductDesc.setText(p.description)
        selectedPhotoUri = p.photoUri
        if (p.photoUri.isNotEmpty()) {
            try { binding.ivProductPhoto.setImageURI(Uri.parse(p.photoUri)) } catch (_: Exception) {}
        }
    }

    private fun saveProduct() {
        val name = binding.etProductName.text.toString().trim()
        val unit = binding.etUnit.text.toString().trim()
        val price = binding.etWholesalePrice.text.toString().trim().toDoubleOrNull()
        val minOrder = binding.etMinOrder.text.toString().trim().toIntOrNull() ?: 1
        val desc = binding.etProductDesc.text.toString().trim()

        if (name.isEmpty() || unit.isEmpty() || price == null) {
            toast("Name, unit and price are required"); return
        }

        val product = Product(
            id = if (productId != -1) productId else 0,
            businessId = businessId,
            name = name, unit = unit, wholesalePrice = price,
            minimumOrder = minOrder, photoUri = selectedPhotoUri, description = desc
        )

        if (productId != -1) {
            viewModel.updateProduct(product) {
                runOnUiThread { toast("✅ Product updated!"); finish() }
            }
        } else {
            viewModel.saveProduct(product) {
                runOnUiThread { toast("✅ Product added!"); finish() }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed(); return true
    }
}
