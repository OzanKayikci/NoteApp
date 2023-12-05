package com.laivinieks.noteapp.feature_note.presentation.add_edit_notes


import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.card.MaterialCardView
import com.laivinieks.noteapp.R
import com.laivinieks.noteapp.other.Extentions.setStatusBarColor


class ColorAdapter(
    private val list: Array<Int>,
    private val context: Context,
    private val pageLayout: ConstraintLayout,
    private val activity: Activity?,
    private val viewModel: AddEditNoteViewModel,
    private val defColor:Int
) :
    RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {


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


    init {
        differ.submitList(list.toList())
       // defColor = differ.currentList[0]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        return ColorViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.component_note_color_button, parent, false)
        )
    }

    private var oldSelection: MaterialCardView? = null

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val color = differ.currentList[position]
        val currColor = ColorStateList.valueOf(getColor(context, color))
        val blackColor = ColorStateList.valueOf(getColor(context, R.color.black))

        holder.colorButton.setStrokeColor(currColor)

        if (color == defColor) {
            activity?.setStatusBarColor(color, context)
            oldSelection = holder.colorButton
            holder.colorButton.setStrokeColor(blackColor)
            viewModel.onEvent(AddEditNoteEvent.ChangeColor(color))
            pageLayout.backgroundTintList = currColor
        }

        holder.itemView.backgroundTintList = currColor
        holder.itemView.setOnClickListener {
            oldSelection?.setStrokeColor(ColorStateList.valueOf(oldSelection!!.solidColor))

            viewModel.onEvent(AddEditNoteEvent.ChangeColor(color))

            holder.colorButton.setStrokeColor(blackColor)

            pageLayout.backgroundTintList = currColor

            activity?.setStatusBarColor(color, context)

            oldSelection = holder.colorButton


        }
        //holder.bind(color)
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}