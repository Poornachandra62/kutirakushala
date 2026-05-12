package com.kutirakushala.ui.business

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kutirakushala.KutiraKushalaApp
import com.kutirakushala.R
import com.kutirakushala.data.model.CapacityStatus
import com.kutirakushala.databinding.ActivityBusinessDetailBinding
import com.kutirakushala.ui.add.AddBusinessActivity
import com.kutirakushala.ui.product.AddProductActivity
import com.kutirakushala.utils.ViewModelFactory
import com.kutirakushala.utils.displayName
import com.kutirakushala.utils.toCategoryDisplayName
import com.kutirakushala.utils.toast

class BusinessDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_BUSINESS_ID = "business_id"
    }

    private lateinit var binding: ActivityBusinessDetailBinding
    private lateinit var viewModel: BusinessDetailViewModel
    private lateinit var productAdapter: ProductAdapter
    private var businessId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusinessDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Business Profile"

        businessId = intent.getIntExtra(EXTRA_BUSINESS_ID, -1)
        if (businessId == -1) { finish(); return }

        val factory = ViewModelFactory((application as KutiraKushalaApp).repository)
        viewModel = ViewModelProvider(this, factory)[BusinessDetailViewModel::class.java]

        productAdapter = ProductAdapter(
            onEdit = { product ->
                startActivity(
                    Intent(this, AddProductActivity::class.java)
                        .putExtra(AddProductActivity.EXTRA_BUSINESS_ID, businessId)
                        .putExtra(AddProductActivity.EXTRA_PRODUCT_ID, product.id)
                )
            }
        )
        binding.rvProducts.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvProducts.adapter = productAdapter

        observeData()
        setupButtons()
    }

    private fun observeData() {
        viewModel.getBusinessWithProducts(businessId).observe(this) { bwp ->
            val biz = bwp.business
            supportActionBar?.title = biz.businessName

            binding.tvBusinessName.text = biz.businessName
            binding.tvOwnerName.text = biz.ownerName
            binding.tvSkillArea.text = biz.skillArea
            binding.tvLocation.text = "📍 ${biz.location}"
            binding.tvCategory.text = biz.category.toCategoryDisplayName()
            binding.tvDescription.text = biz.description
            binding.tvPhone.text = biz.phoneNumber
            binding.tvCapacityUnits.text = "Ready for ${biz.capacityUnits} units this week"

            val color = when (biz.capacityStatus) {
                CapacityStatus.AVAILABLE -> ContextCompat.getColor(this, R.color.capacity_green)
                CapacityStatus.FULL -> ContextCompat.getColor(this, R.color.capacity_red)
                CapacityStatus.PAUSED -> ContextCompat.getColor(this, R.color.capacity_grey)
            }
            binding.tvCapacityStatus.text = "● ${biz.capacityStatus.displayName()}"
            binding.tvCapacityStatus.setTextColor(color)
            binding.cardCapacity.setCardBackgroundColor(
                ContextCompat.getColor(this, when (biz.capacityStatus) {
                    CapacityStatus.AVAILABLE -> R.color.capacity_green_bg
                    CapacityStatus.FULL -> R.color.capacity_red_bg
                    CapacityStatus.PAUSED -> R.color.capacity_grey_bg
                })
            )

            if (biz.photoUri.isNotEmpty()) {
                Glide.with(this).load(biz.photoUri)
                    .placeholder(R.drawable.ic_business_placeholder)
                    .into(binding.ivBusinessPhoto)
            } else {
                binding.ivBusinessPhoto.setImageResource(R.drawable.ic_business_placeholder)
            }

            binding.btnCallNow.setOnClickListener {
                val uri = Uri.parse("tel:${biz.phoneNumber}")
                startActivity(Intent(Intent.ACTION_DIAL, uri))
            }

            binding.btnUpdateCapacity.setOnClickListener {
                showCapacityDialog(biz.id, biz.capacityUnits, biz.capacityStatus)
            }

            binding.btnEditBusiness.setOnClickListener {
                startActivity(
                    Intent(this, AddBusinessActivity::class.java)
                        .putExtra(AddBusinessActivity.EXTRA_BUSINESS_ID, biz.id)
                )
            }

            productAdapter.submitList(bwp.products)
            binding.tvProductCount.text = "${bwp.products.size} products in catalog"
        }
    }

    private fun setupButtons() {
        binding.btnAddProduct.setOnClickListener {
            startActivity(
                Intent(this, AddProductActivity::class.java)
                    .putExtra(AddProductActivity.EXTRA_BUSINESS_ID, businessId)
            )
        }
    }

    private fun showCapacityDialog(bizId: Int, currentUnits: Int, currentStatus: CapacityStatus) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_capacity, null)
        val etUnits = view.findViewById<android.widget.EditText>(R.id.etCapacityUnits)
        val rgStatus = view.findViewById<RadioGroup>(R.id.rgCapacityStatus)
        val rbAvailable = view.findViewById<RadioButton>(R.id.rbAvailable)
        val rbFull = view.findViewById<RadioButton>(R.id.rbFull)
        val rbPaused = view.findViewById<RadioButton>(R.id.rbPaused)

        etUnits.setText(currentUnits.toString())
        when (currentStatus) {
            CapacityStatus.AVAILABLE -> rbAvailable.isChecked = true
            CapacityStatus.FULL -> rbFull.isChecked = true
            CapacityStatus.PAUSED -> rbPaused.isChecked = true
        }

        AlertDialog.Builder(this)
            .setTitle("Update Capacity Meter")
            .setView(view)
            .setPositiveButton("Update") { _, _ ->
                val units = etUnits.text.toString().trim().toIntOrNull() ?: 0
                val status = when (rgStatus.checkedRadioButtonId) {
                    R.id.rbFull -> CapacityStatus.FULL
                    R.id.rbPaused -> CapacityStatus.PAUSED
                    else -> CapacityStatus.AVAILABLE
                }
                viewModel.updateCapacity(bizId, units, status)
                toast("✅ Capacity updated!")
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed(); return true
    }
}
