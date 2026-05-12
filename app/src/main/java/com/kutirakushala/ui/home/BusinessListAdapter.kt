package com.kutirakushala.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kutirakushala.R
import com.kutirakushala.data.model.Business
import com.kutirakushala.data.model.CapacityStatus
import com.kutirakushala.databinding.ItemBusinessCardBinding
import com.kutirakushala.utils.displayName
import com.kutirakushala.utils.toCategoryDisplayName

class BusinessListAdapter(
    private val onClick: (Business) -> Unit
) : ListAdapter<Business, BusinessListAdapter.BusinessVH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessVH {
        val b = ItemBusinessCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BusinessVH(b)
    }

    override fun onBindViewHolder(holder: BusinessVH, position: Int) =
        holder.bind(getItem(position))

    inner class BusinessVH(private val b: ItemBusinessCardBinding) :
        RecyclerView.ViewHolder(b.root) {

        fun bind(biz: Business) {
            b.tvBusinessName.text = biz.businessName
            b.tvOwnerName.text = "👤 ${biz.ownerName}"
            b.tvSkillArea.text = biz.skillArea
            b.tvLocation.text = "📍 ${biz.location}"
            b.tvCategory.text = biz.category.toCategoryDisplayName()
            b.tvCapacityUnits.text = "📦 ${biz.capacityUnits} units this week"

            val statusText = biz.capacityStatus.displayName()
            b.tvCapacityStatus.text = statusText
            val color = when (biz.capacityStatus) {
                CapacityStatus.AVAILABLE -> ContextCompat.getColor(b.root.context, R.color.capacity_green)
                CapacityStatus.FULL -> ContextCompat.getColor(b.root.context, R.color.capacity_red)
                CapacityStatus.PAUSED -> ContextCompat.getColor(b.root.context, R.color.capacity_grey)
            }
            b.tvCapacityStatus.setTextColor(color)
            b.viewCapacityDot.setBackgroundColor(color)

            if (biz.photoUri.isNotEmpty()) {
                Glide.with(b.root.context)
                    .load(biz.photoUri)
                    .circleCrop()
                    .placeholder(R.drawable.ic_business_placeholder)
                    .into(b.ivBusinessPhoto)
            } else {
                b.ivBusinessPhoto.setImageResource(R.drawable.ic_business_placeholder)
            }

            b.root.setOnClickListener { onClick(biz) }
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Business>() {
            override fun areItemsTheSame(o: Business, n: Business) = o.id == n.id
            override fun areContentsTheSame(o: Business, n: Business) = o == n
        }
    }
}
