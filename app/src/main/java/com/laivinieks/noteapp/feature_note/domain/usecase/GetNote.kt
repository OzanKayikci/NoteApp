package com.laivinieks.noteapp.feature_note.domain.usecase

import com.laivinieks.noteapp.feature_note.domain.modal.Note
import com.laivinieks.noteapp.feature_note.domain.repository.NoteRepository

class GetNote(private val repository: NoteRepository) {
    suspend operator fun invoke(id: Int): Note? {
        return repository.getNoteById(id)
    }
}