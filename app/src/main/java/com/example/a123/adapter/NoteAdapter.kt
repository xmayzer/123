package com.example.a123.adapter


import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.a123.R
import com.example.a123.databinding.NoteLayoutBinding
import com.example.a123.fragments.EditFragment
import com.example.a123.fragments.NotesFragmentDirections
import com.example.a123.model.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class NoteAdapter(private val fragmentManager: FragmentManager): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(val itemBinding: NoteLayoutBinding): RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Note>(){
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.noteDesc == newItem.noteDesc &&
                    oldItem.noteTitle == newItem.noteTitle
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
    
    val differ = AsyncListDiffer(this,differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = differ.currentList[position]

        holder.itemBinding.noteTitle.text = currentNote.noteTitle
        holder.itemBinding.noteDesc.text = currentNote.noteDesc

        val formattedDate = formatDate(currentNote.date)

        holder.itemBinding.date.text = formattedDate

        holder.itemView.setOnClickListener {
            // Создаем новый экземпляр EditFragment и передаем данные
            val editFragment = EditFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(
                        "note",
                        currentNote
                    ) // Предполагается, что Note реализует Parcelable
                }
            }

            // Начинаем транзакцию для замены фрагмента
            fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, editFragment) // Убедитесь, что ID контейнера совпадает
                .addToBackStack(null) // Добавляем в стек для возможности возврата
                .commit()
        }
    }
    private fun formatDate(timestamp: Long): String {
        val date = Date(timestamp)
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) // Формат: день, месяц, год
        return sdf.format(date)
    }
}