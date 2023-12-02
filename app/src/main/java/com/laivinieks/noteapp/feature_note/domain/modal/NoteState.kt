package com.laivinieks.noteapp.feature_note.domain.modal

import com.laivinieks.noteapp.feature_note.domain.util.NoteOrder
import com.laivinieks.noteapp.feature_note.domain.util.OrderType

data class NoteState(
    val notes:List<Note> = emptyList(),
     val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    var isOrderSectionVisible:Boolean = false
)
