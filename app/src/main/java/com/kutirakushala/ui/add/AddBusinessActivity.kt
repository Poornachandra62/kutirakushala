package com.kutirakushala.ui.add

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.kutirakushala.KutiraKushalaApp
import com.kutirakushala.R
import com.kutirakushala.data.model.Business
import com.kutirakushala.data.model.CapacityStatus
import com.kutirakushala.data.model.ProductCategory
import com.kutirakushala.databinding.ActivityAddBusinessBinding
import com.kutirakushala.utils.ViewModelFactory
import com.kutirakushala.utils.toast
import kotlinx.coroutines.launch

class AddBusinessActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_BUSINESS_ID = "edit_business_id"
    }

    private lateinit var binding: ActivityAddBusinessBinding
    private lateinit var viewModel: AddBusinessViewModel
    private var selectedPhotoUri: String = ""
    private var editBusinessId: Int = -1
    private var existingBusiness: Business? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedPhotoUri = it.toString()
            binding.ivBusinessPhoto.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBusinessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editBusinessId = intent.getIntExtra(EXTRA_BUSINESS_ID, -1)
        val isEdit = editBusinessId != -1

        supportActionBar?.title = if (isEdit) "Edit Business" else "Register Business"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = ViewModelFactory((application as KutiraKushalaApp).repository)
        viewModel = ViewModelProvider(this, factory)[AddBusinessViewModel::class.java]

        setupCategorySpinner()

        if (isEdit) {
            lifecycleScope.launch {
                val db = (application as KutiraKushalaApp).database
                existingBusiness = db.businessDao().getBusinessById(editBusinessId)
                existingBusiness?.let { prefill(it) }
            }
        }

        binding.ivBusinessPhoto.setOnClickListener { pickImage.launch("image/*") }
        binding.btnPickPhoto.setOnClickListener { pickImage.launch("image/*") }

        binding.btnSaveBusiness.setOnClickListener { saveBusiness() }
    }

    private fun setupCategorySpinner() {
        val categories = ProductCategory.entries.map { it.displayName }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        binding.spinnerCategory.adapter = adapter
    }

    private fun prefill(b: Business) {
        binding.etBusinessName.setText(b.businessName)
        binding.etOwnerName.setText(b.ownerName)
        binding.etSkillArea.setText(b.skillArea)
        binding.etLocation.setText(b.location)
        binding.etDescription.setText(b.description)
        binding.etPhone.setText(b.phoneNumber)
        binding.etCapacityUnits.setText(b.capacityUnits.toString())
        selectedPhotoUri = b.photoUri

        val categoryIndex = ProductCategory.entries.indexOfFirst { it.name == b.category }
        if (categoryIndex >= 0) binding.spinnerCategory.setSelection(categoryIndex)

        if (b.photoUri.isNotEmpty()) {
            try { binding.ivBusinessPhoto.setImageURI(Uri.parse(b.photoUri)) } catch (_: Exception) {}
        }
    }

    private fun saveBusiness() {
        val name = binding.etBusinessName.text.toString().trim()
        val owner = binding.etOwnerName.text.toString().trim()
        val skill = binding.etSkillArea.text.toString().trim()
        val location = binding.etLocation.text.toString().trim()
        val desc = binding.etDescription.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val units = binding.etCapacityUnits.text.toString().trim().toIntOrNull() ?: 0
        val category = ProductCategory.entries[binding.spinnerCategory.selectedItemPosition].name

        if (name.isEmpty() || owner.isEmpty() || skill.isEmpty() || location.isEmpty() || phone.isEmpty()) {
            toast("Please fill all required fields")
            return
        }
        if (phone.length < 10) { toast("Enter a valid phone number"); return }

        val business = Business(
            id = if (editBusinessId != -1) editBusinessId else 0,
            businessName = name, ownerName = owner, skillArea = skill,
            location = location, description = desc, phoneNumber = phone,
            capacityUnits = units, category = category,
            photoUri = selectedPhotoUri,
            capacityStatus = CapacityStatus.AVAILABLE
        )

        if (editBusinessId != -1) {
            viewModel.updateBusiness(business) {
                runOnUiThread { toast("✅ Business updated!"); finish() }
            }
        } else {
            viewModel.saveBusiness(business) {
                runOnUiThread { toast("✅ Business registered!"); finish() }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed(); return true
    }
}
