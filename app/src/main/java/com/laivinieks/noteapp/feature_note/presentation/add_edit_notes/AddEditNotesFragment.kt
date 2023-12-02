package com.laivinieks.noteapp.feature_note.presentation.add_edit_notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.laivinieks.noteapp.databinding.FragmentAddEditNoteScreenBinding
import com.laivinieks.noteapp.feature_note.domain.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditNotesFragment : Fragment() {
    private var _binding: FragmentAddEditNoteScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var colorAdapter: ColorAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddEditNoteScreenBinding.inflate(inflater, container, false)
        setupRecycleView()
        return binding.root
    }

    private fun setupRecycleView() {
        colorAdapter = ColorAdapter(requireContext(),binding.noteBackground)
        colorAdapter.submitList(Constants.colorList)

        val lmVertical =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.rwColors.apply {
            adapter = colorAdapter
            layoutManager = lmVertical
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}