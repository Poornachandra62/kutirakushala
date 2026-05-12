package com.kutirakushala.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kutirakushala.KutiraKushalaApp
import com.kutirakushala.databinding.ActivityMainBinding
import com.kutirakushala.ui.add.AddBusinessActivity
import com.kutirakushala.ui.business.BusinessDetailActivity
import com.kutirakushala.ui.search.SearchActivity
import com.kutirakushala.utils.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: BusinessListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory((application as KutiraKushalaApp).repository)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        viewModel.init()

        adapter = BusinessListAdapter { business ->
            startActivity(
                Intent(this, BusinessDetailActivity::class.java)
                    .putExtra(BusinessDetailActivity.EXTRA_BUSINESS_ID, business.id)
            )
        }

        binding.rvBusinesses.layoutManager = LinearLayoutManager(this)
        binding.rvBusinesses.adapter = adapter

        viewModel.businesses.observe(this) { list ->
            adapter.submitList(list)
            binding.tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            binding.tvCount.text = "${list.size} businesses listed"
        }

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddBusinessActivity::class.java))
        }

        binding.btnSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh list on return from add/edit
        viewModel.businesses.value?.let { adapter.submitList(it) }
    }
}
