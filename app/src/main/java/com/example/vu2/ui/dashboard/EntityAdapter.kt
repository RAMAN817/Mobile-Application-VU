package com.example.vu2.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vu2.databinding.ItemEntityBinding

class EntityAdapter(
    private val onClick: (Map<String, String>) -> Unit
) : ListAdapter<Map<String, String>, EntityAdapter.EntityViewHolder>(DiffCallback) {

    inner class EntityViewHolder(
        private val binding: ItemEntityBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entity: Map<String, String>) {
            // Show all fields except "description"
            val displayFields = entity.filter { it.key != "description" }

            // Use first field as title, rest as subtitle
            val entries = displayFields.entries.toList()
            binding.tvTitle.text = entries.firstOrNull()?.let { "${formatKey(it.key)}: ${it.value}" } ?: "Entity"
            binding.tvSubtitle.text = entries.drop(1).joinToString(" • ") { "${formatKey(it.key)}: ${it.value}" }

            binding.root.setOnClickListener { onClick(entity) }
        }

        private fun formatKey(key: String): String =
            key.replaceFirstChar { it.uppercase() }.replace("_", " ")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityViewHolder {
        val binding = ItemEntityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EntityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EntityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Map<String, String>>() {
        override fun areItemsTheSame(oldItem: Map<String, String>, newItem: Map<String, String>) =
            oldItem == newItem
        override fun areContentsTheSame(oldItem: Map<String, String>, newItem: Map<String, String>) =
            oldItem == newItem
    }
}
