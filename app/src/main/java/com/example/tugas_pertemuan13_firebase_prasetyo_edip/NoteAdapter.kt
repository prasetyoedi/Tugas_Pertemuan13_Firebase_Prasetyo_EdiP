package com.example.tugas_pertemuan13_firebase_prasetyo_edip

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas_pertemuan13_firebase_prasetyo_edip.databinding.ListNoteBinding

class NoteAdapter (private val context: Context, private val listData: List<Note>, private val onClickData: (Note) -> Unit) :
    RecyclerView.Adapter<NoteAdapter.ItemDataViewHolder>() {

    inner class ItemDataViewHolder(private val binding: ListNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Note) {
            with(binding) {
                listName.text = "Name : " + data.name
                listTitle.text = "Title Note : " + data.title
                listDetail.text = "Detail Note : " + data.details
                itemView.setOnClickListener {
                    onClickData(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDataViewHolder {
        val binding = ListNoteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ItemDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemDataViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size
}
