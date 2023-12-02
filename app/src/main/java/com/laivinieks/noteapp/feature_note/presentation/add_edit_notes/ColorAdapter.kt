package com.laivinieks.noteapp.feature_note.presentation.add_edit_notes


import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.laivinieks.noteapp.R
import com.laivinieks.noteapp.feature_note.domain.util.Constants.colorList

class ColorAdapter(private val context: Context, private val pageLayout: ConstraintLayout) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    inner class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val colorButton: MaterialCardView = itemView.findViewById(R.id.noteColorButton)


        fun bind(color: Int) {

            //TODO("bind to views")
        }
    }


    private val diffCallback = object : DiffUtil.ItemCallback<Int>() {

        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    fun submitList(list: Array<Int>) = differ.submitList(list.toList())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        return ColorViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.component_note_color_button, parent, false)
        )
    }

    private var oldSelection: MaterialCardView? = null
    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val color = differ.currentList[position]
        val currColor = ColorStateList.valueOf(getColor(context, color))
        holder.itemView.backgroundTintList = currColor
        holder.colorButton.setStrokeColor(currColor)
        holder.itemView.setOnClickListener {
            oldSelection?.setStrokeColor(ColorStateList.valueOf(oldSelection!!.solidColor))

            holder.colorButton.setStrokeColor(ColorStateList.valueOf(getColor(context, R.color.black)))
            pageLayout.backgroundTintList = currColor
            oldSelection = holder.colorButton

        }
        //holder.bind(color)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}