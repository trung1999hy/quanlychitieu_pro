package com.example.quanlychitieu.ui.type_note.archive

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quanlychitieu.model.Archive
import com.example.quanlychitieu.model.Note
import com.example.quanlychitieu.utils.click
import com.thn.quanlychitieu.databinding.NoteItemBinding

class ArchiveAdapter(
    val itemClick: ((Archive) -> Unit)? = null,
    val itemMoreClick: ((Archive) -> Unit)? = null
) : RecyclerView.Adapter<ArchiveAdapter.ViewHolder>() {
    private var listData: ArrayList<Archive> = arrayListOf()

    inner class ViewHolder(
        val binding: NoteItemBinding,
        val itemMoreClick: ((Archive) -> Unit)? = null
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Archive) {
            binding.title.text = data.title
            binding.date.text = data.time
            binding.container.text = data.container
            binding.more.click {
                itemMoreClick?.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            NoteItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), itemMoreClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listData[position])
        holder.itemView.click {
            itemClick?.invoke(listData[position])
        }
    }

    override fun getItemCount(): Int = listData.size
    fun setAdapter(listData: ArrayList<Archive>) {
        this.listData = listData
        notifyDataSetChanged()
    }
}