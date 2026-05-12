package com.kutirakushala.ui.business

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kutirakushala.R
import com.kutirakushala.data.model.Product
import com.kutirakushala.databinding.ItemProductCardBinding

class ProductAdapter(
    private val onEdit: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductVH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductVH {
        val b = ItemProductCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductVH(b)
    }

    override fun onBindViewHolder(holder: ProductVH, position: Int) =
        holder.bind(getItem(position))

    inner class ProductVH(private val b: ItemProductCardBinding) :
        RecyclerView.ViewHolder(b.root) {
        fun bind(p: Product) {
            b.tvProductName.text = p.name
            b.tvPrice.text = "₹${p.wholesalePrice} ${p.unit}"
            b.tvMinOrder.text = "Min: ${p.minimumOrder} units"
            b.tvProductDesc.text = p.description.ifEmpty { "—" }

            if (p.photoUri.isNotEmpty()) {
                Glide.with(b.root.context).load(p.photoUri)
                    .placeholder(R.drawable.ic_product_placeholder)
                    .into(b.ivProductPhoto)
            } else {
                b.ivProductPhoto.setImageResource(R.drawable.ic_product_placeholder)
            }

            b.root.setOnClickListener { onEdit(p) }
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(o: Product, n: Product) = o.id == n.id
            override fun areContentsTheSame(o: Product, n: Product) = o == n
        }
    }
}
