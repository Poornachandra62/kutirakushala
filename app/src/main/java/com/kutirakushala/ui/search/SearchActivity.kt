package com.kutirakushala.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kutirakushala.KutiraKushalaApp
import com.kutirakushala.data.model.ProductCategory
import com.kutirakushala.databinding.ActivitySearchBinding
import com.kutirakushala.ui.business.BusinessDetailActivity
import com.kutirakushala.ui.home.BusinessListAdapter
import com.kutirakushala.utils.ViewModelFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: BusinessListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "🔍 Search Businesses"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = ViewModelFactory((application as KutiraKushalaApp).repository)
        viewModel = ViewModelProvider(this, factory)[SearchViewModel::class.java]

        adapter = BusinessListAdapter { biz ->
            startActivity(
                Intent(this, BusinessDetailActivity::class.java)
                    .putExtra(BusinessDetailActivity.EXTRA_BUSINESS_ID, biz.id)
            )
        }
        binding.rvResults.layoutManager = LinearLayoutManager(this)
        binding.rvResults.adapter = adapter

        setupSearchBar()
        setupCategoryChips()
        observeResults()
    }

    private fun setupSearchBar() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val q = s?.toString()?.trim() ?: ""
                if (q.isEmpty()) viewModel.clearFilters()
                else viewModel.setQuery(q)
            }
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, a: Int) {}
        })
    }

    private fun setupCategoryChips() {
        val allCategories = listOf("All") + ProductCategory.entries.map { it.displayName }
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, allCategories)
        binding.spinnerCategory.adapter = spinnerAdapter

        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                val location = binding.etLocation.text.toString().trim()
                val category = if (pos == 0) null else ProductCategory.entries[pos - 1].name
                viewModel.setFilter(category, location)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.btnApplyFilter.setOnClickListener {
            val location = binding.etLocation.text.toString().trim()
            val pos = binding.spinnerCategory.selectedItemPosition
            val category = if (pos == 0) null else ProductCategory.entries[pos - 1].name
            viewModel.setFilter(category, location)
        }

        binding.btnClear.setOnClickListener {
            binding.etSearch.text?.clear()
            binding.etLocation.text?.clear()
            binding.spinnerCategory.setSelection(0)
            viewModel.clearFilters()
        }
    }

    private fun observeResults() {
        viewModel.results.observe(this) { list ->
            adapter.submitList(list)
            binding.tvResultCount.text = "${list.size} results found"
            binding.tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed(); return true
    }
}
