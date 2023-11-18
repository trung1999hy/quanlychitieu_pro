package com.example.quanlychitieu.ui.type_note.note

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quanlychitieu.model.Note
import com.example.quanlychitieu.utils.click
import com.thn.quanlychitieu.databinding.NoteItemBinding

class NoteAdapter(
    val itemClick: ((Note) -> Unit)? = null,
    val itemMoreClick: ((Note) -> Unit)? = null
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    private var listData: ArrayList<Note> = arrayListOf()

    inner class ViewHolder(
        val binding: NoteItemBinding,
        val itemMoreClick: ((Note) -> Unit)? = null
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Note) {
            if (data.password.isNullOrEmpty()) {
                binding.viewLock.visibility = View.GONE
                binding.viewLayout.visibility = View.VISIBLE
            }else{
                binding.viewLock.visibility = View.VISIBLE
                binding.viewLayout.visibility = View.GONE
            }
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
    fun setAdapter(listData: ArrayList<Note>) {
        this.listData = listData
        notifyDataSetChanged()
    }
}