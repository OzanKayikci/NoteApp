package com.laivinieks.noteapp.feature_note.presentation.notes

import com.laivinieks.noteapp.feature_note.domain.modal.Note
import com.laivinieks.noteapp.feature_note.domain.util.NoteOrder

sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder) : NotesEvent()
    data class  DeleteNote(val note: Note):NotesEvent()
    data class  OpenNote(val note: Note):NotesEvent()

    object  RestoreNote:NotesEvent()
    object ToggleOrderSection:NotesEvent()
}
