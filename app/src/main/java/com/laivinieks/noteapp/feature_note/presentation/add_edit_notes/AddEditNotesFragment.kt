package com.laivinieks.noteapp.feature_note.presentation.add_edit_notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.laivinieks.noteapp.R
import com.laivinieks.noteapp.databinding.FragmentAddEditNoteScreenBinding
import com.laivinieks.noteapp.feature_note.domain.util.Constants
import com.laivinieks.noteapp.other.Extentions.setStatusBarColor
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
        colorAdapter = ColorAdapter(Constants.colorList,requireContext(),binding.noteBackground,activity)

        val lmVertical =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.rwColors.apply {
            adapter = colorAdapter
            layoutManager = lmVertical
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        activity?.setStatusBarColor(R.color.primary_background, requireContext())
        _binding = null
    }

}