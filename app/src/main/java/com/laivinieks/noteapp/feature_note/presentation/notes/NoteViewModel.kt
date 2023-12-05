package com.laivinieks.noteapp.feature_note.presentation.notes


import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistry
import com.laivinieks.noteapp.feature_note.domain.modal.Note
import com.laivinieks.noteapp.feature_note.domain.modal.NoteState
import com.laivinieks.noteapp.feature_note.domain.usecase.NoteUseCases
import com.laivinieks.noteapp.feature_note.domain.util.NoteOrder
import com.laivinieks.noteapp.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableLiveData(NoteState())
    val state: LiveData<NoteState> = _state

    private var recentlyDeletedNote: Note? = null
    private var getNotesJob: Job? = null


    init {

        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.Order -> {

                /**if user select already selected*/
                /**
                 * if we don't use ::class it will check referential equality - but their references different
                 * because we defined NoteOrders normal class not data class
                 * because of that we check if their class are the same
                 * */
                if (state.value!!.noteOrder::class == event.noteOrder::class && state.value!!.noteOrder.orderType == event.noteOrder.orderType) {
                    return
                }
                getNotes(event.noteOrder)
            }

            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.note)
                    recentlyDeletedNote = event.note
                }
            }

            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }

            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value!!.copy(
                    isOrderSectionVisible = !state.value!!.isOrderSectionVisible
                )
            }

            is NotesEvent.OpenNote -> {

            }
        }
    }

    //we use job here because ->  when we recall this function we want to cancel the old coroutine that is already observing database
    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotes(noteOrder)
            .onEach { notes ->

                _state.value = state.value!!.copy(
                    notes = notes,
                    noteOrder = noteOrder,
                )
            }.launchIn(viewModelScope)

    }

}