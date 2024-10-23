package com.eventapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.eventapp.data.response.ListEventsItem
import com.eventapp.databinding.HorizontalRowEventBinding
import com.eventapp.databinding.ItemRowEventBinding
import com.eventapp.ui.detail.DetailActivity
import com.eventapp.utils.loadImage

class ListEventAdapter(
    private val listEvent: List<ListEventsItem>,
    private val horizontal: Boolean = false
) :
    RecyclerView.Adapter<ListEventAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = (if (horizontal) HorizontalRowEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ) else ItemRowEventBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listEvent[position]
        if (horizontal) {
            val binding = holder.binding as HorizontalRowEventBinding
            binding.cardTitle.text = data.name
            binding.cardSummary.text = data.summary
            binding.cardCover.loadImage(data.imageLogo)
        } else {
            val binding = holder.binding as ItemRowEventBinding
            binding.cardTitle.text = data.name
            binding.cardSummary.text = data.summary
            binding.cardCover.loadImage(data.imageLogo)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_EVENT_ID, data.id)
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = listEvent.size

}