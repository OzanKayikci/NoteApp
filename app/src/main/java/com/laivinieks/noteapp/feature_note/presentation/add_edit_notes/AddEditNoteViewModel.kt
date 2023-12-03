package com.laivinieks.noteapp.feature_note.presentation.add_edit_notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laivinieks.noteapp.feature_note.domain.modal.InvalidNoteException
import com.laivinieks.noteapp.feature_note.domain.modal.Note
import com.laivinieks.noteapp.feature_note.domain.usecase.NoteUseCases
import com.laivinieks.noteapp.feature_note.domain.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewMode @Inject constructor(private val noteUseCases: NoteUseCases,savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _noteTitle = MutableLiveData<String>("")
    val noteTitle: LiveData<String> = _noteTitle!!

    private val _noteContent = MutableLiveData<String>("")
    val noteContent: LiveData<String> = _noteContent!!

    private val _noteColor = MutableLiveData<Int>(Constants.colorList[0])
    val noteColor: LiveData<Int> = _noteColor!!

    private val _eventFLow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFLow.asSharedFlow()

    private var currentNoteId: Int? = null
    init {

        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1){
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also {
                        currentNoteId  = it.id
                        _noteTitle.value = it.title
                        _noteContent.value = it.content
                    }
                }
            }
        }
    }
    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = event.value
            }

            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = event.value
            }

            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = event.value
            }

            is AddEditNoteEvent.SaveNote -> {
                var newNote = Note(currentNoteId, noteTitle.value!!, noteContent.value!!, System.currentTimeMillis(), noteColor.value!!)
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(newNote)
                        _eventFLow.emit(UiEvent.SaveNote)

                    } catch (e: InvalidNoteException) {
            _eventFLow.emit(UiEvent.ShowSnackBar(message = e.message?:"Couldn't save note"))
                    }

                }
            }


        }
    }


    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }
}