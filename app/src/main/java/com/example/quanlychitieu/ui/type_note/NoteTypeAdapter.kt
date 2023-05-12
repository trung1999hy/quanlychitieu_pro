package com.example.quanlychitieu.ui.type_note

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quanlychitieu.model.NoteType
import com.thn.quanlychitieu.databinding.ItemNoteTypeBinding

class NoteTypeAdapter(val itemLongClick: (NoteType) -> Unit, val itemOnCLick: (NoteType) -> Unit) :
    RecyclerView.Adapter<NoteTypeAdapter.ViewHolder>() {
    private var list: List<NoteType> = listOf()

    inner class ViewHolder(private val itemNoteTypeBinding: ItemNoteTypeBinding) :
        RecyclerView.ViewHolder(itemNoteTypeBinding.root) {
        fun bind(noteType: NoteType) {
            itemNoteTypeBinding.title.text = noteType.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemNoteTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnLongClickListener {
            itemLongClick.invoke(list.get(position))
            true
        }
        holder.itemView.setOnClickListener {
            itemOnCLick.invoke(list.get(position))
        }
    }

    fun setData(list: List<NoteType>) {
        this.list = list
        notifyDataSetChanged()
    }
}