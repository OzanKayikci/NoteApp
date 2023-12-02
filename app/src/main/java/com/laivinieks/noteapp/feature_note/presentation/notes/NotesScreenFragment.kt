package com.laivinieks.noteapp.feature_note.presentation.notes

import android.animation.LayoutTransition
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.laivinieks.noteapp.R
import com.laivinieks.noteapp.databinding.FragmentNotesScreenBinding
import com.laivinieks.noteapp.feature_note.domain.util.NoteOrder
import com.laivinieks.noteapp.feature_note.domain.util.OrderType
import dagger.hilt.android.AndroidEntryPoint

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotesScreenBinding.inflate(inflater, container, false)
        binding.filtersLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        buttonsHandle()
        getFilters()
        filterListener()
        // applyFilter()
        observers()
        return binding.root
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
        viewModel.state.observe(viewLifecycleOwner) {
            Log.d("new filter", it.noteOrder.orderType.toString())
            applyFilter()
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