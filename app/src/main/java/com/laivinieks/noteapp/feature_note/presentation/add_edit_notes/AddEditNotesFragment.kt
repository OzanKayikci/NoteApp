package com.laivinieks.noteapp.feature_note.presentation.add_edit_notes

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.laivinieks.noteapp.R
import com.laivinieks.noteapp.databinding.FragmentAddEditNoteScreenBinding
import com.laivinieks.noteapp.feature_note.domain.util.Constants
import com.laivinieks.noteapp.feature_note.domain.util.NoteContentTextWatcher
import com.laivinieks.noteapp.other.Extentions.setStatusBarColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditNotesFragment : Fragment() {
    private var _binding: FragmentAddEditNoteScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var colorAdapter: ColorAdapter
    private var defColor: Int = 0
    private val viewModel by lazy {
        ViewModelProvider(this, defaultViewModelProviderFactory)[AddEditNoteViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddEditNoteScreenBinding.inflate(inflater, container, false)
        setupInputs()
        setupRecycleView()
        eventHandle()



        return binding.root
    }

    private fun setupInputs() {
        binding.tvTitle.setText(viewModel.noteTitle.value!!)
        binding.tvContent.setText(viewModel.noteContent.value!!)
        defColor = viewModel.noteColor.value!!
    }

    private fun eventHandle() {

        binding.tvTitle.addTextChangedListener(NoteContentTextWatcher() {


            viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
        })

        binding.tvContent.addTextChangedListener(NoteContentTextWatcher() {
            viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
        })

        binding.addNoteButton.setOnClickListener {

            viewModel.onEvent(AddEditNoteEvent.SaveNote).also {
                lifecycleScope.launch {
                    viewModel.eventFlow.collectLatest { event ->
                        when (event) {
                            is AddEditNoteViewModel.UiEvent.SaveNote -> {
                                Log.d("error", "savee")
                                findNavController().navigateUp()
                            }

                            is AddEditNoteViewModel.UiEvent.ShowSnackBar -> {
                                Log.d("error", event.message)
                                Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG).show()
                            }

                        }
                    }
                }


            }


        }
    }


    private fun setupRecycleView() {
        colorAdapter = ColorAdapter(Constants.colorList, requireContext(), binding.noteBackground, activity, viewModel, defColor)

        val lmVertical =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.rwColors.apply {
            adapter = colorAdapter
            layoutManager = lmVertical
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

        activity?.setStatusBarColor(R.color.primary_background, requireContext())
        _binding = null
    }

}