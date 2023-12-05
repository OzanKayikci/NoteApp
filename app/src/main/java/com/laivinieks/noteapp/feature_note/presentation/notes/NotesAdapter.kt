package com.laivinieks.noteapp.feature_note.presentation.notes

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.laivinieks.noteapp.R
import com.laivinieks.noteapp.feature_note.domain.modal.Note

class NotesAdapter(private val context: Context,  private val noteOperation: (NotesEvent) -> Unit) :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val noteTitle: TextView = itemView.findViewById(R.id.cardTitle)
        val noteContent: TextView = itemView.findViewById(R.id.cardContent)
        val cardView = itemView.findViewById<CardView>(R.id.noteCard)
        val trashBtn: ImageView = itemView.findViewById(R.id.trash)
        fun bind(note: Note) {
            noteTitle.text = note.title
            noteContent.text = note.content
            cardView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, note.color))
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }
    }
    private val differ = AsyncListDiffer(this, diffCallback)
    fun submitList(list: List<Note>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.component_note_preview_card, parent, false)
        )
    }


    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = differ.currentList[position]

        holder.bind(note)
        holder.cardView.setOnClickListener {
            noteOperation(NotesEvent.OpenNote(note))

        }
        holder.trashBtn.setOnClickListener {

            noteOperation(NotesEvent.DeleteNote(note))
        }
        // navigateToFragment(holder.noteTitle, note.id!!)
        // navigateToFragment(holder.noteContent, note.id!!)

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}