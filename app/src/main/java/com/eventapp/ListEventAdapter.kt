package com.eventapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eventapp.data.response.ListEventsItem
import com.eventapp.ui.detail.DetailActivity

class ListEventAdapter(
    private val listEvent: List<ListEventsItem>,
    private val horizontal: Boolean = false
) :
    RecyclerView.Adapter<ListEventAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardTitle: TextView = itemView.findViewById(R.id.card_title)
        val cardSummary: TextView = itemView.findViewById(R.id.card_summary)
        val cardCover: ImageView = itemView.findViewById(R.id.card_cover)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(
                if (horizontal) R.layout.horizontal_row_event else R.layout.item_row_event,
                parent,
                false
            )
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listEvent[position]
        holder.cardTitle.text = data.name
        holder.cardSummary.text = data.summary
        Glide.with(holder.itemView.context)
            .load(data.imageLogo)
            .into(holder.cardCover)


        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_EVENT_ID, data.id)
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = listEvent.size
}