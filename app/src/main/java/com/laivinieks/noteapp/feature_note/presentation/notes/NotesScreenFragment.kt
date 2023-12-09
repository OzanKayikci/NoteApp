package com.laivinieks.noteapp.feature_note.presentation.notes

import android.animation.LayoutTransition
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.laivinieks.noteapp.R
import com.laivinieks.noteapp.databinding.FragmentNotesScreenBinding
import com.laivinieks.noteapp.feature_note.domain.util.Constants
import com.laivinieks.noteapp.feature_note.domain.util.NoteOrder
import com.laivinieks.noteapp.feature_note.domain.util.OrderType
import com.laivinieks.noteapp.feature_note.domain.util.SnackBarButtonListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotesScreenFragment : Fragment() {
    private var _binding: FragmentNotesScreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this, defaultViewModelProviderFactory)[NoteViewModel::class.java]
    }

    private lateinit var titleFilter: RadioButton
    private lateinit var dateFilter: RadioButton
    private lateinit var colorFilter: RadioButton
    private lateinit var ascSort: RadioButton
    private lateinit var descSort: RadioButton

    private lateinit var bgFilters: RadioGroup
    private lateinit var bgSort: RadioGroup

    private lateinit var adapter: NotesAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotesScreenBinding.inflate(inflater, container, false)
        binding.filtersLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        buttonsHandle()
        getFilters()
        setupRecycleView()
        filterListener()
        applyFilter()
        observers()
        return binding.root
    }

    private fun noteOperation(notesEvent: NotesEvent) {

        viewModel.onEvent(notesEvent).also {
            when (notesEvent::class) {
                NotesEvent.DeleteNote::class -> {
                    Snackbar.make(requireView(), "Note Deleted", Snackbar.LENGTH_LONG).setAction("Undo", SnackBarButtonListener() {
                        if (it == Constants.UNDO_SNACKBAR) {
                            viewModel.onEvent(NotesEvent.RestoreNote)
                        }
                    }).show()
                }

                NotesEvent.OpenNote::class -> {

                    var bundle = bundleOf("noteId" to viewModel.openingNoteId!!)
                    findNavController().navigate(R.id.action_notesScreenFragment_to_addEditNotesFragment,bundle)
                }
            }
        }

    }

    private fun setupRecycleView() {
        adapter = NotesAdapter(requireContext(), ::noteOperation)
        val gridLayoutManager = GridLayoutManager(
            requireContext(),
            2,
            LinearLayoutManager.VERTICAL,
            false
        )

        binding.rwNotes.layoutManager = gridLayoutManager
        binding.rwNotes.setHasFixedSize(true)
        binding.rwNotes.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun buttonsHandle() {
        val radioButtons = binding.includeFilters.root
        binding.filtersLayout.setOnClickListener {
            if (radioButtons.visibility == View.GONE) {
                binding.filtersButtons.animate().rotation(binding.filtersButtons.rotation + 180f).setDuration(500).start()
                radioButtons.visibility = View.VISIBLE
            } else {
                binding.filtersButtons.animate().rotation(binding.filtersButtons.rotation - 180f).setDuration(500).start()
                radioButtons.visibility = View.GONE

            }
        }

        binding.addNoteButton.setOnClickListener {
            findNavController().navigate(R.id.action_notesScreenFragment_to_addEditNotesFragment)
        }
    }

    private fun filterListener() {

        bgFilters.setOnCheckedChangeListener { _, checkedId ->

            val btnFilter = binding.root.findViewById<RadioButton>(checkedId)
            val btnSort = binding.root.findViewById<RadioButton>(bgSort.checkedRadioButtonId)

            setFilters(btnFilter, btnSort)

        }

        bgSort.setOnCheckedChangeListener { _, checkedId ->

            val btnSort = binding.root.findViewById<RadioButton>(checkedId)
            val btnFilter = binding.root.findViewById<RadioButton>(bgFilters.checkedRadioButtonId)

            setFilters(btnFilter, btnSort)

        }
    }

    private fun setFilters(filterBtn: RadioButton, sortButton: RadioButton) {
        var orderType: OrderType = when (sortButton) {
            ascSort -> OrderType.Ascending
            descSort -> OrderType.Descending
            else -> OrderType.Ascending
        }


        (when (filterBtn) {
            titleFilter -> NoteOrder.Title(orderType)
            colorFilter -> NoteOrder.Color(orderType)
            dateFilter -> NoteOrder.Date(orderType)

            else -> {
                NoteOrder.Title(OrderType.Descending)
            }
        }).also {
            onOrderChange(it)
        }
    }


    private fun observers() {
        lifecycleScope.launch {
            viewModel.state.observe(viewLifecycleOwner) {
                adapter.submitList(it.notes)
                applyFilter()
            }
        }


    }

    private fun getFilters() {
        bgFilters = binding.root.findViewById(R.id.bgFilters)
        bgSort = binding.root.findViewById(R.id.bgSort)

        titleFilter = binding.root.findViewById(R.id.rbTitle)
        colorFilter = binding.root.findViewById(R.id.rbColor)
        dateFilter = binding.root.findViewById(R.id.rbDate)
        ascSort = binding.root.findViewById(R.id.rbAscending)
        descSort = binding.root.findViewById(R.id.rbDescending)
    }

    private fun applyFilter() {
        val noteOrder = viewModel.state.value!!.noteOrder
        titleFilter.apply {
            isChecked = noteOrder is NoteOrder.Title

        }
        dateFilter.apply {
            isChecked = noteOrder is NoteOrder.Date
        }
        colorFilter.apply {
            isChecked = noteOrder is NoteOrder.Color
        }

        ascSort.apply {
            isChecked = noteOrder.orderType is OrderType.Ascending
        }
        descSort.apply {
            isChecked = noteOrder.orderType is OrderType.Descending
        }
    }

    private fun onOrderChange(noteOrder: NoteOrder) {

        var noteEvent = NotesEvent.Order(noteOrder)
        viewModel.onEvent(noteEvent)
    }
}