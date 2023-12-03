package com.laivinieks.noteapp.feature_note.presentation.add_edit_notes

sealed class AddEditNoteEvent {
    data class EnteredTitle(val value:String):AddEditNoteEvent()
    data class EnteredContent(val value:String):AddEditNoteEvent()
    data class ChangeColor(val value:Int):AddEditNoteEvent()
    object SaveNote:AddEditNoteEvent()
}