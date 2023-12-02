package com.laivinieks.noteapp.feature_note.domain.modal

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.laivinieks.noteapp.R

@Entity
data class Note(
    @PrimaryKey
    val id: Int? = null,
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
) {
    companion object {
        val noteColors = listOf(R.color.RedOrange, R.color.LightGreen, R.color.Violet, R.color.BabyBlue, R.color.RedPink)
        val noteColors2 = listOf(R.color.primary,R.color.secondary,R.color.tertiary,R.color.light,R.color.dark)
        val noteColors3 = listOf(R.color.primary2,R.color.secondary2,R.color.tertiary2,R.color.light2,R.color.dark2)

    }
}

class InvalidNoteException(message:String): Exception(message)
